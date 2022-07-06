/**
 * @desc
 * 
 */
package com.business.proxy.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import com.base.common.constant.FixedConstant;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperQueueWorker;

public class LongChannelRepairWorkerManager{
	
	private static final Logger logger = LoggerFactory.getLogger(LongChannelRepairWorkerManager.class);
	
	private Map<String,Map<Integer,BusinessRouteValue>> map = new HashMap<String, Map<Integer,BusinessRouteValue>>();
	private Map<String,TimerTask> taskMap = new HashMap<String,TimerTask>();
	
	private static LongChannelRepairWorkerManager manager = new LongChannelRepairWorkerManager();
	
	CombinationWorker combinationWorker;
	Worker worker;
	Timer timer;
	Object lock = new Object();
	
	public static LongChannelRepairWorkerManager getInstance(){
		return manager;
	}
	
	private LongChannelRepairWorkerManager(){
		timer = new Timer();
		combinationWorker = new CombinationWorker();
		combinationWorker.setName("long-report-combination-worker");
		combinationWorker.start();	
		worker = new Worker();
		worker.setName("long-report-worker");
		worker.start();

	}
	public void process(BusinessRouteValue businessRouteValue){
		worker.add(businessRouteValue);
	}
	
	public BusinessRouteValue match(Map<Integer,BusinessRouteValue> map){
	
		BusinessRouteValue businessRouteValue = map.get(1).clone();
		int size = map.size();
		for(int i=2;i<=size;i++){
			businessRouteValue.setAccountMessageIDs(businessRouteValue.getAccountMessageIDs().concat(FixedConstant.SPLICER).concat(map.get(i).getAccountMessageIDs()));
		}
		return businessRouteValue;
	}
	
	class Worker extends SuperQueueWorker<BusinessRouteValue>{
		
		@Override
		protected void doRun() throws Exception {
			BusinessRouteValue businessRouteValue = take();
			process(businessRouteValue);
		}
		
		//添加一次，执行一次match,match到的要删除
		private void process(BusinessRouteValue businessRouteValue){
			String md5 =  DigestUtils.md5DigestAsHex(businessRouteValue.getMessageContent().getBytes());
			String key = new StringBuffer(String.valueOf(businessRouteValue.getChannelTotal()))
										.append(FixedConstant.SPLICER).append(businessRouteValue.getPhoneNumber())
										.append(FixedConstant.SPLICER).append(businessRouteValue.getAccountID())
										.append(FixedConstant.SPLICER).append(businessRouteValue.getBusinessMessageID())
										.append(FixedConstant.SPLICER).append(md5).toString();
			logger.info(new StringBuilder().append("长短信状态报告需合成处理")
						.append("{}accountID={}")
						.append("{}phoneNumber={}")
						.append("{}total={}")
						.append("{}number={}")
						.append("{}messageContent={}")
						.append("{}businessMessageID={}")
						.append("{}key={}").toString()
						,
						FixedConstant.LOG_SEPARATOR,businessRouteValue.getAccountID(),
						FixedConstant.LOG_SEPARATOR,businessRouteValue.getPhoneNumber(),
						FixedConstant.LOG_SEPARATOR,businessRouteValue.getChannelTotal(),
						FixedConstant.LOG_SEPARATOR,businessRouteValue.getChannelIndex(),
						FixedConstant.LOG_SEPARATOR,businessRouteValue.getMessageContent(),
						FixedConstant.LOG_SEPARATOR,businessRouteValue.getBusinessMessageID(),
						FixedConstant.LOG_SEPARATOR,key
						);
			
			//synchronized (lock) {
				Map<Integer,BusinessRouteValue> temp = map.get(key);
				if(temp == null){
					temp = new HashMap<Integer,BusinessRouteValue>();
					map.put(key, temp);
					TimerTask task = new RemindTask(key);
					timer.schedule(task, 60000);
					taskMap.put(key,task);
				}
				if(!temp.containsKey(businessRouteValue.getChannelIndex())){
					temp.put(businessRouteValue.getChannelIndex(), businessRouteValue);
				}
				
				//当满足条件则匹配成功
				if(temp.size() == Integer.parseInt(key.substring(0, key.indexOf(FixedConstant.SPLICER)))){
					combinationWorker.add(temp);
					map.remove(key);
					TimerTask task = taskMap.remove(key);
					if(task != null){
						task.cancel();
					}
					logger.info("剩余未匹配长短信状态报告条数：{}",map.size());
				}
			//}

		}
		
	}
	
	class CombinationWorker extends  SuperQueueWorker<Map<Integer,BusinessRouteValue>>{
		
		@Override
		protected void doRun() throws Exception {
			// TODO Auto-generated method stub
			Map<Integer,BusinessRouteValue> map = superQueue.take();
			logger.info("待合成数:"+size());
			BusinessRouteValue businessRouteValue = match(map);
			logger.info(new StringBuilder().append("长短信状态报告匹配成功")
						.append("{}accountID={}")
						.append("{}phoneNumber={}")
						.append("{}messageContent={}").toString()
						,
						FixedConstant.LOG_SEPARATOR,businessRouteValue.getAccountID(),
						FixedConstant.LOG_SEPARATOR,businessRouteValue.getPhoneNumber(),
						FixedConstant.LOG_SEPARATOR,businessRouteValue.getMessageContent());
			ChannelRepairWorkerManager.getInstance().saveBusinessRouteValue(businessRouteValue.getChannelID(), businessRouteValue);
			
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
			logger.debug("删除task:"+key);
			Map<Integer,BusinessRouteValue> temp = map.remove(key);
			if(temp != null){
				for(Map.Entry<Integer,BusinessRouteValue> entry:temp.entrySet()){
					logger.warn(new StringBuilder().append("删除未成功合成长短信状态报告的切片")
								.append("{}accountID={}")
								.append("{}phoneNumber={}")
								.append("{}total={}")
								.append("{}number={}")
								.append("{}messageContent={}").toString()
								,
								FixedConstant.LOG_SEPARATOR,entry.getValue().getAccountID(),
								FixedConstant.LOG_SEPARATOR,entry.getValue().getPhoneNumber(),
								FixedConstant.LOG_SEPARATOR,entry.getValue().getChannelTotal(),
								FixedConstant.LOG_SEPARATOR,entry.getValue().getChannelName(),
								FixedConstant.LOG_SEPARATOR,entry.getValue().getMessageContent());
					//SimulationReportManager.getInstance().simulation(entry.getValue(),Commons.REPEATED, Commons.REPEATED);
				}
			}
			//logger.info("删除submit"+temp);
		}
		 
	 }
	
}


