package com.protocol.access.manager;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import com.base.common.constant.FixedConstant;
import com.base.common.manager.ChannelInfoManager;
import com.base.common.worker.SuperCacheWorker;
import com.base.common.worker.SuperConcurrentMapWorker;
import com.protocol.access.vo.Report;

public class ReportTimerTaskWorkerManager extends SuperConcurrentMapWorker<String, TimerTask> {

	private static ReportTimerTaskWorkerManager manager = new ReportTimerTaskWorkerManager();
	
	private BlockingQueue<String> msgIds = new LinkedBlockingQueue<String>();
	
	private Timer timer;

	private ReportTimerTaskWorkerManager() {
		timer = new Timer();
		this.start();
		for(int i = 0;i < FixedConstant.CPU_NUMBER;i++) {
			ReportTimerTaskWorker worker = new ReportTimerTaskWorker();
			worker.setName("ReportTimerTaskWorker-"+(i+1));
			worker.start();
		}
	}

	public static ReportTimerTaskWorkerManager getInstance() {
		return manager;
	}

	public void removeReportTaskByResponse(String messageID) {
		msgIds.add(messageID);
	}

	public void addReportTimeoutTask(Report report) {
		String messageID = report.getMessageId();
		TimerTask responseTimeoutTask = new ResponseTimeoutTask(messageID,report);
		add(messageID, responseTimeoutTask);
		//长链接：获取提交等待响应的超时时间，一般是通用时间，可以根据单个通道在通道扩展参数中设置responseTimeout
		long delay = ChannelInfoManager.getInstance().getResponseTimeout(report.getAccountId());
		timer.schedule(responseTimeoutTask, delay);
	}
	
	@Override
	protected void doRun() throws Exception {
		logger.info("等待状态报告响应数量:{},待匹配状态报告响应数量:{}",superMap.size(),msgIds.size());
		sleep(FixedConstant.COMMON_MONITOR_INTERVAL_TIME);
	}
	
	class ReportTimerTaskWorker extends SuperCacheWorker {

		@Override
		protected void doRun() throws Exception {
			String messageID = msgIds.take();
			logger.debug("待处理已接收响应的状态报告数{}:", msgIds.size());
			ResponseTimeoutTask responseTimeoutTask = (ResponseTimeoutTask) remove(messageID);

			if (responseTimeoutTask == null) {
				logger.info(new StringBuilder().append("未找到状态报告信息").append("{}messageID={}").toString(),
						FixedConstant.SPLICER, messageID);
				return;
			}
			responseTimeoutTask.cancel();
		}
	}
	
	/**
	 * 在设置的超时时间内没有
	 */
	class ResponseTimeoutTask extends TimerTask {

		private String messageID;
		private Report report;

		public ResponseTimeoutTask(String messageID,Report report) {
			super();
			this.messageID = messageID;
			this.report = report;
		}

		@Override
		public void run() {
			try {
				remove(messageID);
				logger.info(
						new StringBuilder().append("推送状态报告未收到状态报告响应信息")
						.append("{}accountID={}")
						.append("{}phoneNumber={}")
						.append("{}submitTime={}")
						.append("{}statusCode={}")
						.append("{}accountSRCID={}")
						.append("{}messageID={}")
						.toString(),
						FixedConstant.SPLICER,report.getAccountId(),
						FixedConstant.SPLICER,report.getPhoneNumber(),
						FixedConstant.SPLICER,report.getSubmitTime(),
						FixedConstant.SPLICER,report.getStatusCode(),
						FixedConstant.SPLICER,report.getAccountSrcId(),
						FixedConstant.SPLICER,report.getMessageId()
						);
				
				//未收到响应 保存状态报告
				ReportManager.getIntance().addPushFailReport(report);
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
		}
	}

}