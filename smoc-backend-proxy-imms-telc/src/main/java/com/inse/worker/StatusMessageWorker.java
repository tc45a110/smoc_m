package com.inse.worker;
import com.base.common.worker.SuperQueueWorker;
import com.inse.message.StatusMessage;
import com.inse.util.DAO;

public class StatusMessageWorker extends SuperQueueWorker<StatusMessage> {
	private String channelID;

	public StatusMessageWorker(String channelID) {
		this.channelID = channelID;
		this.start();
		

	}

	@Override
	protected void doRun() throws Exception {
		StatusMessage message = take();
		if(message!=null) {
		// 更新模板状态
		String status = "10".equals(message.getCheckState()) ? "2" : "8";
		DAO.updateAccountChannelTemplateInfo(message, status, channelID);
		logger.info("运营商模板：" + message.getTemplateId());
		}

	}

}
