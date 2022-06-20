/**
 * @desc
 * 
 */
package com.business.access.manager;

import com.base.common.constant.FixedConstant;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperQueueWorker;
import com.business.access.worker.InsideFilterWorker;

/**
 * 内部过滤线程管理
 */
public class InsideFilterWorkerManager extends SuperQueueWorker<BusinessRouteValue>{
	
	private static InsideFilterWorkerManager manager = new InsideFilterWorkerManager();
		
	private InsideFilterWorkerManager(){
		//预估100毫秒处理一次http请求，启动cpu的数量*64的系数，并发请求为 cpu数量*64*10 当cpu=8时，并发量为5000
		for(int i=0;i<FixedConstant.CPU_NUMBER*64;i++){
			InsideFilterWorker insideFilterWorker = new InsideFilterWorker(superQueue);
			insideFilterWorker.setName(new StringBuilder("InsideFilterWorker-").append(i+1).toString());
			insideFilterWorker.start();
		}
		this.start();
	}
	
	public static InsideFilterWorkerManager getInstance(){
		return manager;
	}
	
	public void process(BusinessRouteValue businessRouteValue){
		add(businessRouteValue);
	}
	
	public void doRun() throws Exception {
		logger.info("内部过滤缓存队列数量{}",size());
		Thread.sleep(FixedConstant.COMMON_MONITOR_INTERVAL_TIME);
	}
	
}


