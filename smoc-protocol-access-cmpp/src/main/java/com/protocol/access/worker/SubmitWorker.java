 package com.protocol.access.worker;

import java.util.ArrayList;
import java.util.List;

import com.base.common.manager.BusinessDataManager;
import com.base.common.worker.SuperQueueWorker;
import com.protocol.access.util.DAO;
import com.protocol.access.vo.MessageInfo;

public class SubmitWorker extends SuperQueueWorker<MessageInfo>{
	
	@Override
	protected void doRun() throws Exception {
		if(superQueue.size() > 0) {
			long start = System.currentTimeMillis();
			//临时数据
			List<MessageInfo> routeMessageMtInfoVoList;
			synchronized (lock) {
				routeMessageMtInfoVoList = new ArrayList<MessageInfo>(superQueue);
				superQueue.clear();
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


