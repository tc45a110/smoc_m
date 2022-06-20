/**
 * @desc
 * 
 */
package com.business.access.manager;

import com.base.common.constant.FixedConstant;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperQueueWorker;
import com.business.access.worker.BusinessWorker;

public class BusinessWorkerManager extends SuperQueueWorker<BusinessRouteValue>{
	
	private static BusinessWorkerManager manager = new BusinessWorkerManager();
	
	
	private BusinessWorkerManager(){
		//启动cpu的数量*8的系数
		for(int i=0;i<FixedConstant.CPU_NUMBER*8;i++){
			BusinessWorker businessWorker = new BusinessWorker(superQueue);
			businessWorker.setName(new StringBuilder("BusinessWorker-").append(i).toString());
			businessWorker.start();
		}
		
		this.start();
	}
	
	public static BusinessWorkerManager getInstance(){
		return manager;
	}
	
	/**
	 * 添加到共享队列中，由业务线程主动竞争获取
	 * @param businessRouteValue
	 */
	public void process(BusinessRouteValue businessRouteValue){
		add(businessRouteValue);
	}

	
	public void doRun() throws Exception {
		logger.info("业务缓存队列数量{}",size());
		Thread.sleep(FixedConstant.COMMON_MONITOR_INTERVAL_TIME);
	}
	
	/**
	 * 获取业务缓存队列数量
	 * @return
	 */
	public int getSize(){
		return size();
	}
}


