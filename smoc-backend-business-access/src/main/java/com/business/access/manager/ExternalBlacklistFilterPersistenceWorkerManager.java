/**
 * @desc
 * 
 */
package com.business.access.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.base.common.constant.FixedConstant;
import com.base.common.manager.BusinessDataManager;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperConcurrentMapWorker;
import com.base.common.worker.SuperMapWorker;
import com.base.common.worker.SuperQueueWorker;
import com.business.access.dao.ExternalBlacklistFilterDAO;
import com.business.access.manager.ExternalBlacklistFilterPersistenceWorkerManager.ExternalBlacklistFilterPersistenceWorker;

public class ExternalBlacklistFilterPersistenceWorkerManager extends SuperMapWorker<Integer,ExternalBlacklistFilterPersistenceWorker>{
	
	private static ExternalBlacklistFilterPersistenceWorkerManager manager = new ExternalBlacklistFilterPersistenceWorkerManager();
	ExternalBlacklistFilterLoadWorker externalBlacklistFilterLoadWorker;
	private ExternalBlacklistFilterPersistenceWorkerManager(){
		for(int i=0;i<FixedConstant.CPU_NUMBER;i++){
			ExternalBlacklistFilterPersistenceWorker externalBlacklistFilterPersistenceWorker = new ExternalBlacklistFilterPersistenceWorker();
			externalBlacklistFilterPersistenceWorker.setName(new StringBuilder("ExternalBlacklistFilterPersistenceWorker-").append(i+1).toString());
			externalBlacklistFilterPersistenceWorker.start();
			add(i, externalBlacklistFilterPersistenceWorker);
		}
		this.start();
		externalBlacklistFilterLoadWorker = new ExternalBlacklistFilterLoadWorker();
		externalBlacklistFilterLoadWorker.setName("ExternalBlacklistFilterLoadWorker");
		externalBlacklistFilterLoadWorker.start();
	}
	
	public static ExternalBlacklistFilterPersistenceWorkerManager getInstance(){
		return manager;
	}
	
	public void process(BusinessRouteValue businessRouteValue){
		superMap.get((int)(superMap.size() * Math.random())).process(businessRouteValue.getBusinessMessageID(), businessRouteValue);
	}

	@Override
	public void doRun() throws Exception {
		Thread.sleep(FixedConstant.COMMON_MONITOR_INTERVAL_TIME);
	}
	
	public int getSize(){
		int count = 0;
		Collection<ExternalBlacklistFilterPersistenceWorker> collection = superMap.values();
		for(ExternalBlacklistFilterPersistenceWorker externalBlacklistFilterPersistenceWorker : collection){
			count = count+ externalBlacklistFilterPersistenceWorker.getSize();
		}
		return count;
	}
	
	public void exit(){
		externalBlacklistFilterLoadWorker.exit();
		
		Collection<ExternalBlacklistFilterPersistenceWorker> collection = superMap.values();
		for(ExternalBlacklistFilterPersistenceWorker externalBlacklistFilterPersistenceWorker : collection){
			externalBlacklistFilterPersistenceWorker.exit();
		}
	
	}
	
	/**
	 * 加载持久化的过外部黑名单信息
	 */
	class ExternalBlacklistFilterLoadWorker extends SuperQueueWorker<BusinessRouteValue>{

		@Override
		protected void doRun() throws Exception {
			long startTime = System.currentTimeMillis();
			long costTime = 0;
			int messageLoadMaxNumber = BusinessDataManager.getInstance().getMessageLoadMaxNumber();
			
			//当外部黑名单缓存队列的容量低于阈值时从数据库获取数据
			if(ExternalBlacklistFilterWorkerManager.getInstance().isBelowCacheThreshold()){
				List<BusinessRouteValue> businessRouteValueList = ExternalBlacklistFilterDAO.loadRouteMessageBlacklistFilterInfo(messageLoadMaxNumber);
				if(businessRouteValueList != null && businessRouteValueList.size() > 0 ){
					costTime = System.currentTimeMillis() - startTime;
					logger.info("本次加载外部黑名单过滤数据条数{},耗时{}毫秒",businessRouteValueList.size(),costTime);

					for(BusinessRouteValue businessRouteValue : businessRouteValueList){
						ExternalBlacklistFilterWorkerManager.getInstance().processFromPersistence(businessRouteValue);
					}
					
					costTime = System.currentTimeMillis() - startTime;
					logger.info("本次加载及分发外部黑名单过滤数据条数{},耗时{}毫秒",businessRouteValueList.size(),costTime);
				}
			}

			//每次加载都需按照时间暂停，避免缓存数量过大，造成内存溢出	
			controlSubmitSpeed(BusinessDataManager.getInstance().getMessageLoadIntervalTime(),costTime);
			
		}
		
	}
	
	class ExternalBlacklistFilterPersistenceWorker extends SuperConcurrentMapWorker<String, BusinessRouteValue>{

		@Override
		public void doRun() throws Exception {

			if(size() > 0){
				long startTime = System.currentTimeMillis();
				//临时数据
				List<BusinessRouteValue> businessRouteValueList =  new ArrayList<BusinessRouteValue>(superMap.values());
				
				//将已经取走的数据在原始缓存中进行删除
				for(BusinessRouteValue businessRouteValue : businessRouteValueList){
					remove(businessRouteValue.getBusinessMessageID());
				}
				
				ExternalBlacklistFilterDAO.saveRouteMessageBlacklistFilterInfo(businessRouteValueList);
				long interval = System.currentTimeMillis() - startTime;
				logger.info("外部黑名单过滤保存数据条数{},耗时{}毫秒",businessRouteValueList.size(),interval);
			}else{
				//当没有数据时，需要暂停一会
				long interval = BusinessDataManager.getInstance().getMessageSaveIntervalTime();
				Thread.sleep(interval);
			}
		
		}
		
		public void process(String businessMessageID,BusinessRouteValue businessRouteValue){
			add(businessMessageID, businessRouteValue);
		}
		
		public int getSize(){
			return size();
		}
		
	}

	
}


