package com.inse.manager;

import com.base.common.worker.SuperQueueWorker;
import com.inse.message.StatusMessage;
import com.inse.util.DAO;


public class TemplateStatusManager extends SuperQueueWorker<StatusMessage> {
	private static TemplateStatusManager manager = new TemplateStatusManager();

	public static TemplateStatusManager getInstance(){
		return manager;
	}

	@Override
	protected void doRun() throws Exception {
		StatusMessage message = poll();
		if(message!=null) {
			// 更新模板状态
			String status = "10".equals(message.getCheckState()) ? "2" : "8";
			DAO.updateAccountChannelTemplateInfo(message, status);
			logger.info("运营商模板：" + message.getTemplateId());
		}
	}

	public void process(StatusMessage message) {
		add(message);
	}

}
