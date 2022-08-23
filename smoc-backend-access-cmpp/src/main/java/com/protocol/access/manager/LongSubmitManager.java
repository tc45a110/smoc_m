/**
 * @desc
 * @author ma
 * @date 2017年11月3日
 * 
 */
package com.protocol.access.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.constant.FixedConstant;
import com.base.common.constant.InsideStatusCodeConstant;
import com.base.common.manager.BusinessDataManager;

import com.protocol.access.vo.MessageInfo;

public class LongSubmitManager {
	private static final Logger logger = LoggerFactory.getLogger(LongSubmitManager.class);
	private Map<String, Map<Integer, MessageInfo>> map = new HashMap<String, Map<Integer, MessageInfo>>();
	private Map<String, TimerTask> taskMap = new HashMap<String, TimerTask>();

	Map<Integer, Worker> workerMap = new HashMap<Integer, Worker>();
	Map<Integer, CombinationWorker> combinationWorkerMap = new HashMap<Integer, CombinationWorker>();

	Timer timer;
	Object lock = new Object();
	
	private int threadTotal = FixedConstant.CPU_NUMBER;


	public LongSubmitManager(String accountID) {
		timer = new Timer();
		
		for (int i = 0; i < threadTotal; i++) {
			Worker worker = new Worker();
			worker.setName(accountID+"-long-sms-worker-" + i);
			workerMap.put(i, worker);
			worker.start();

			CombinationWorker combinationWorker = new CombinationWorker();
			combinationWorker.setName(accountID+"-long-sms-combination-worker-" + i);
			combinationWorkerMap.put(i, combinationWorker);
			combinationWorker.start();
		}
	}

	public void process(MessageInfo messageInfo) {
		int index = Math.abs(messageInfo.getLongsmid()) % threadTotal;
		workerMap.get(index).add(messageInfo);
	}

	// 添加一次，执行一次match,match到的要删除
	public void persistence(MessageInfo vo) {
		String key = new StringBuffer(String.valueOf(vo.getTotal())).append(",").append(vo.getPhoneNumber()).append(",")
				.append(vo.getLongsmid()).append(",").append(vo.getAccountId()).toString();
		logger.info(new StringBuilder().append("长短信需合成处理:")
					.append("client={}")
					.append("{}mobile={}")
					.append("{}longsmid={}")
					.append("{}total={}")
					.append("{}number={}")
					.append("{}content={}")
					.append("{}key={}").toString()
					,
					vo.getAccountId(),
					FixedConstant.LOG_SEPARATOR,vo.getPhoneNumber(),
					FixedConstant.LOG_SEPARATOR,vo.getLongsmid(),
					FixedConstant.LOG_SEPARATOR,vo.getTotal(),
					FixedConstant.LOG_SEPARATOR,vo.getNumber(),
					FixedConstant.LOG_SEPARATOR,vo.getMessageContent(),
					FixedConstant.LOG_SEPARATOR,key);

		Map<Integer, MessageInfo> temp = map.get(key);
		if (temp == null) {
			temp = new HashMap<Integer, MessageInfo>();
			map.put(key, temp);
			TimerTask task = new RemindTask(key);
			timer.schedule(task, 300000);
			taskMap.put(key, task);
		}

		if (!temp.containsKey(vo.getNumber())) {
			temp.put(vo.getNumber(), vo);
		} else {
			logger.warn(new StringBuilder().append("丢弃长短信重复切片:")
						.append("client={}")
						.append("{}mobile={}")
						.append("{}longsmid={}")
						.append("{}total={}")
						.append("{}number={}")
						.append("{}content={}").toString(),
						vo.getAccountId(), 
						FixedConstant.LOG_SEPARATOR,vo.getPhoneNumber(),
						FixedConstant.LOG_SEPARATOR,vo.getLongsmid(),
						FixedConstant.LOG_SEPARATOR,vo.getTotal(),
						FixedConstant.LOG_SEPARATOR,vo.getNumber(),
						FixedConstant.LOG_SEPARATOR,vo.getMessageContent()
						);
			ReportManager.getIntance().addSendFailReport(vo, InsideStatusCodeConstant.StatusCode.MISSING.name());
			return;
		}

		// 当满足条件则匹配成功
		if (temp.size() == Integer.parseInt(key.substring(0, key.indexOf(",")))) {
			int index = Math.abs(vo.getLongsmid()) % threadTotal;

			combinationWorkerMap.get(index).add(temp);
			map.remove(key);
			TimerTask task = taskMap.remove(key);
			if (task != null) {
				task.cancel();
			}
			logger.debug("剩余未匹配长短信条数：{}", map.size());
		}
	}

	public MessageInfo match(Map<Integer, MessageInfo> map) {

		MessageInfo vo = new MessageInfo(map.get(1));
		int size = map.size();
		for (int i = 2; i <= size; i++) {
			vo.setMessageContent(vo.getMessageContent().concat(map.get(i).getMessageContent()));
			vo.setMessageId(vo.getMessageId().concat(FixedConstant.SPLICER).concat(map.get(i).getMessageId()));
		}
		return vo;
	}

	class Worker extends Thread {
		private BlockingQueue<MessageInfo> workerSMSQueue = new LinkedBlockingQueue<MessageInfo>();

		@Override
		public void run() {
			logger.info(this.getName() + "启动!");
			while (true) {
				try {
					MessageInfo vo = workerSMSQueue.take();
					logger.info("长短信待处理量:{}",size());
					persistence(vo);
				} catch (Exception e) {
					logger.error("Worker error " + e.getMessage(), e);
				}
			}
		}

		public boolean add(MessageInfo e) {
			return workerSMSQueue.add(e);
		}

		/**
		 * 取得队列中存在对象数量
		 * 
		 * @return 队列中对象数量
		 */
		public int size() {
			return workerSMSQueue.size();
		}

		/**
		 * 取得可用空间
		 * 
		 * @return 可用空间
		 */
		public int freeSize() {
			return workerSMSQueue.remainingCapacity();
		}
	}

	class CombinationWorker extends Thread {
		private BlockingQueue<Map<Integer, MessageInfo>> workerSMSQueue = new LinkedBlockingQueue<Map<Integer, MessageInfo>>();

		@Override
		public void run() {
			logger.info(this.getName() + "启动!");
			while (true) {
				try {
					Map<Integer, MessageInfo> map = workerSMSQueue.take();
					logger.info("待合成数:{}", size());
					MessageInfo vo = match(map);
					logger.debug(new StringBuilder().append("长短信匹配成功:")
							.append("client={}")
							.append("{}mobile={}")
							.append("{}content={}").toString(),
							vo.getAccountId(), 
							FixedConstant.LOG_SEPARATOR,vo.getPhoneNumber(),
							FixedConstant.LOG_SEPARATOR,vo.getMessageContent()
							);
					
					// 增加对长短信的长度校验
					int charLength = BusinessDataManager.getInstance().getMaxSmsTextLength();
					if (vo.getMessageContent().length() > charLength) {
						logger.warn(new StringBuilder().append("短信长度超过限制:")
								.append("client={}")
								.append("{}mobile={}")
								.append("{}length={}")
								.append("{}maxlength={}")
								.append("{}total={}").toString(),
								vo.getAccountId(), 
								FixedConstant.LOG_SEPARATOR,vo.getPhoneNumber(),
								FixedConstant.LOG_SEPARATOR,vo.getMessageContent().getBytes("UTF-8").length,
								FixedConstant.LOG_SEPARATOR,charLength,
								FixedConstant.LOG_SEPARATOR,vo.getTotal()
								);
						ReportManager.getIntance().addSendFailReport(vo, InsideStatusCodeConstant.StatusCode.OVERLEN.name());
						continue;							
					}
					SubmitWorkerManager.getInstance().process(vo);

				} catch (Exception e) {
					logger.error("Worker error " + e.getMessage(), e);
				}
			}
		}

		public boolean add(Map<Integer, MessageInfo> e) {
			return workerSMSQueue.add(e);
		}

		/**
		 * 取得队列中存在对象数量
		 * 
		 * @return 队列中对象数量
		 */
		public int size() {
			return workerSMSQueue.size();
		}

		/**
		 * 取得可用空间
		 * 
		 * @return 可用空间
		 */
		public int freeSize() {
			return workerSMSQueue.remainingCapacity();
		}
	}

	class RemindTask extends TimerTask {

		private String key;

		public RemindTask(String key) {
			super();
			this.key = key;
		}

		@Override
		public void run() {
			taskMap.remove(key);
			logger.debug("删除task:" + key);
			Map<Integer, MessageInfo> temp = map.remove(key);
			if (temp != null) {
				for (Map.Entry<Integer, MessageInfo> entry : temp.entrySet()) {
					logger.warn(new StringBuilder().append("删除未成功合成长短信的切片:")
							.append("client={}")
							.append("{}mobile={}")
							.append("{}longsmid={}")
							.append("{}total={}")
							.append("{}number={}")
							.append("{}content={}").toString(),
							entry.getValue().getAccountId(), 
							FixedConstant.LOG_SEPARATOR,entry.getValue().getPhoneNumber(),
							FixedConstant.LOG_SEPARATOR,entry.getValue().getLongsmid(),
							FixedConstant.LOG_SEPARATOR,entry.getValue().getTotal(),
							FixedConstant.LOG_SEPARATOR,entry.getValue().getNumber(),
							FixedConstant.LOG_SEPARATOR,entry.getValue().getMessageContent()
							);
					
					ReportManager.getIntance().addSendFailReport(entry.getValue(), InsideStatusCodeConstant.StatusCode.MISSING.name());
				}
			}

		}

	}

}
