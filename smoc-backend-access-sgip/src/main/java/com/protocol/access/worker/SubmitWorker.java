 package com.protocol.access.worker;

import java.util.ArrayList;
import java.util.List;

import com.base.common.manager.BusinessDataManager;
import com.base.common.worker.SuperConcurrentMapWorker;
import com.protocol.access.util.DAO;
import com.protocol.access.vo.MessageInfo;

public class SubmitWorker extends SuperConcurrentMapWorker<String, MessageInfo>{
	
	public void put(String messageId, MessageInfo messageInfo) {
		add(messageId, messageInfo);
	}
	
	@Override
	protected void doRun() throws Exception {
		if(superMap.size() > 0) {
			long start = System.currentTimeMillis();
			//临时数据
			List<MessageInfo> routeMessageMtInfoVoList = new ArrayList<MessageInfo>(superMap.values());
			for(MessageInfo info : routeMessageMtInfoVoList) {
				remove(info.getMessageId());
			}
			
			DAO.saveRouteMessageMtInfoList(routeMessageMtInfoVoList);
			long interval = System.currentTimeMillis() - start;
			logger.info("本次保存数据条数{},耗时{}毫秒",routeMessageMtInfoVoList.size(),interval);
		}else {
			//无数据时，线程暂停时间
			long interval = BusinessDataManager.getInstance().getMessageSaveIntervalTime();
			Thread.sleep(interval);
		}
	}
	

}


