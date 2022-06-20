/**
 * @desc
 * 
 */
package com.business.access.manager;

import org.apache.commons.lang3.StringUtils;

import com.base.common.constant.FixedConstant;
import com.base.common.manager.BusinessDataManager;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperQueueWorker;
import com.business.access.worker.ExternalBlacklistFilterWorker;

/**
 * 外部过滤线程管理
 */
public class ExternalBlacklistFilterWorkerManager extends SuperQueueWorker<BusinessRouteValue>{
	
	private static ExternalBlacklistFilterWorkerManager manager = new ExternalBlacklistFilterWorkerManager();
		
	private ExternalBlacklistFilterWorkerManager(){
		//预估100毫秒处理一次http请求，启动cpu的数量*64的系数，并发请求为 cpu数量*64*10 当cpu=8时，并发量为5000
		for(int i=0;i<FixedConstant.CPU_NUMBER*64;i++){
			ExternalBlacklistFilterWorker externalFilterWorker = new ExternalBlacklistFilterWorker(superQueue);
			externalFilterWorker.setName(new StringBuilder("ExternalFilterWorker-").append(i+1).toString());
			externalFilterWorker.start();
		}
		this.start();
	}
	
	public static ExternalBlacklistFilterWorkerManager getInstance(){
		return manager;
	}
	
	public void process(BusinessRouteValue businessRouteValue){
		String blackListLevelFiltering = BusinessDataManager.getInstance().getBlackListLevelFiltering(businessRouteValue.getAccountID());
		//需要过外部黑名单
		if(StringUtils.isNotEmpty(blackListLevelFiltering) && blackListLevelFiltering.startsWith("HIGH")){
			add(businessRouteValue);
		}else{
			ChannelWorkerManager.getInstance().process(businessRouteValue);
		}
	}
	
	public void doRun() throws Exception {
		logger.info("外部黑名单过滤信息缓存数量{}",size());
		Thread.sleep(FixedConstant.COMMON_MONITOR_INTERVAL_TIME);
	}
	
}


