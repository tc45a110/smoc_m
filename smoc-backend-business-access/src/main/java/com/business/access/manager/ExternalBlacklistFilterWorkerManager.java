/**
 * @desc
 * 
 */
package com.business.access.manager;

import org.apache.commons.lang3.StringUtils;

import com.base.common.constant.FixedConstant;
import com.base.common.manager.AlarmManager;
import com.base.common.manager.BusinessDataManager;
import com.base.common.manager.ResourceManager;
import com.base.common.vo.AlarmMessage;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperQueueWorker;
import com.business.access.worker.ExternalBlacklistFilterWorker;

/**
 * 外部过滤线程管理
 */
public class ExternalBlacklistFilterWorkerManager extends SuperQueueWorker<BusinessRouteValue>{
	//外部黑名单过滤线程数与cpu的倍数
	private static int externalBlacklistFilterWorkerMultiple = ResourceManager.getInstance().getIntValue("external.blacklist.filter.worker.multiple");

	private static ExternalBlacklistFilterWorkerManager manager = new ExternalBlacklistFilterWorkerManager();
	

		
	private ExternalBlacklistFilterWorkerManager(){
		
		if(externalBlacklistFilterWorkerMultiple == 0){
			externalBlacklistFilterWorkerMultiple = 1;
		}
		
		//预估100毫秒处理一次http请求，启动cpu的数量*64的系数，并发请求为 cpu数量*64*10 当cpu=8时，并发量为5000
		for(int i=0;i<FixedConstant.CPU_NUMBER*externalBlacklistFilterWorkerMultiple;i++){
			ExternalBlacklistFilterWorker externalFilterWorker = new ExternalBlacklistFilterWorker(superQueue);
			externalFilterWorker.setName(new StringBuilder("ExternalFilterWorker-").append(i+1).toString());
			externalFilterWorker.start();
		}
		System.out.println("外部黑名单过滤线程数:"+FixedConstant.CPU_NUMBER*externalBlacklistFilterWorkerMultiple);
		this.start();
	}
	
	public static ExternalBlacklistFilterWorkerManager getInstance(){
		return manager;
	}
	
	public void process(BusinessRouteValue businessRouteValue){
		String blackListLevelFiltering = BusinessDataManager.getInstance().getBlackListLevelFiltering(businessRouteValue.getAccountID());
		//需要过外部黑名单
		if(StringUtils.isNotEmpty(blackListLevelFiltering) && blackListLevelFiltering.startsWith("HIGH")){
			//缓存队列数量低于阈值添加处理队列
			if(isBelowCacheThreshold()){
				add(businessRouteValue);
			}else{
				ExternalBlacklistFilterPersistenceWorkerManager.getInstance().process(businessRouteValue);
			}
		}else{
			ChannelWorkerManager.getInstance().process(businessRouteValue);
		}
	}
	
	/**
	 * 处理持久层的外部黑名单过滤数据
	 * @param businessRouteValue
	 */
	public void processFromPersistence(BusinessRouteValue businessRouteValue){
		String blackListLevelFiltering = BusinessDataManager.getInstance().getBlackListLevelFiltering(businessRouteValue.getAccountID());
		//外部调用需判断缓存容量大小
		if(StringUtils.isNotEmpty(blackListLevelFiltering) && blackListLevelFiltering.startsWith("HIGH")){
			add(businessRouteValue);
		}else{
			ChannelWorkerManager.getInstance().process(businessRouteValue);
		}

	}
	
	/**
	 * 判断缓存是否低于阈值
	 * @param messageLoadMaxNumber
	 * @return
	 */
	boolean isBelowCacheThreshold(){
		//外部过滤缓存数与单次加载条数的倍数
		int externalBlacklistFilterCacheSizeMultiple = ResourceManager.getInstance().getIntValue("external.blacklist.filter.cache.size.multiple");
		if(externalBlacklistFilterCacheSizeMultiple == 0){
			externalBlacklistFilterCacheSizeMultiple = 1000;
		}
		int threshold = BusinessDataManager.getInstance().getMessageLoadMaxNumber()*externalBlacklistFilterCacheSizeMultiple;
		int size = size() ;
		//当超过阈值时返回false
		if(size < threshold){
			return true;
		}
		logger.warn("外部黑名单过滤缓存队列数量{},超过缓存阈值{}",size,threshold);
		return false;
	}
	
	/**
	 * 对外提供缓存队列大小
	 * @return
	 */
	public int getSize(){
		return  size();
	}
	
	public void doRun() throws Exception {
		isOverCacheThreshold();
		Thread.sleep(FixedConstant.COMMON_MONITOR_INTERVAL_TIME);
	}
	
	/**
	 * 超过阈值
	 * @return
	 */
	private void isOverCacheThreshold(){
		//外部过滤缓存数与单次加载条数的倍数
		int externalBlacklistFilterCacheSizeMultiple = ResourceManager.getInstance().getIntValue("external.blacklist.filter.cache.size.multiple");
		if(externalBlacklistFilterCacheSizeMultiple == 0){
			externalBlacklistFilterCacheSizeMultiple = 1000;
		}
		int threshold = BusinessDataManager.getInstance().getMessageLoadMaxNumber()*externalBlacklistFilterCacheSizeMultiple;
		int size = size() ;
		logger.info("外部黑名单过滤缓存队列数量{}",size);
		if(size > threshold){
			logger.warn("外部黑名单过滤缓存队列数量{},超过缓存阈值{}",size,threshold);
			AlarmManager.getInstance().process(new AlarmMessage(AlarmMessage.AlarmKey.ExternalBlacklistFilterWorker,String.valueOf(size)));
		}
		
	}
	
}


