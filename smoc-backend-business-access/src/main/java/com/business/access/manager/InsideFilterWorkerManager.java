/**
 * @desc
 * 
 */
package com.business.access.manager;

import com.base.common.constant.FixedConstant;
import com.base.common.manager.BusinessDataManager;
import com.base.common.manager.ResourceManager;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperQueueWorker;
import com.business.access.worker.InsideFilterWorker;

/**
 * 内部过滤线程管理
 */
public class InsideFilterWorkerManager extends SuperQueueWorker<BusinessRouteValue>{
	//内部过滤线程数与cpu的倍数
	private static int insideFilterWorkerMultiple = ResourceManager.getInstance().getIntValue("inside.filter.worker.multiple");

	private static InsideFilterWorkerManager manager = new InsideFilterWorkerManager();

	private InsideFilterWorkerManager(){
		
		if(insideFilterWorkerMultiple == 0){
			insideFilterWorkerMultiple = 1;
		}

		//预估50毫秒处理一次http请求，启动cpu的数量*64的系数，并发请求为 cpu数量*64*10 当cpu=8时，并发量为5000
		for(int i=0;i<FixedConstant.CPU_NUMBER*insideFilterWorkerMultiple;i++){
			InsideFilterWorker insideFilterWorker = new InsideFilterWorker(superQueue);
			insideFilterWorker.setName(new StringBuilder("InsideFilterWorker-").append(i+1).toString());
			insideFilterWorker.start();
		}
		System.out.println("内部过滤线程数:"+FixedConstant.CPU_NUMBER*insideFilterWorkerMultiple);
		this.start();
	}
	
	public static InsideFilterWorkerManager getInstance(){
		return manager;
	}
	
	/**
	 * 判断缓存是否低于阈值
	 * @param messageLoadMaxNumber
	 * @return
	 */
	public boolean isBelowCacheThreshold(){
		//内部过滤缓存数与单次加载条数的倍数
		int insideFilterCacheSizeMultiple = ResourceManager.getInstance().getIntValue("inside.filter.cache.size.multiple");
		if(insideFilterCacheSizeMultiple == 0){
			insideFilterCacheSizeMultiple = 10;
		}
		int threshold = BusinessDataManager.getInstance().getMessageLoadMaxNumber()*insideFilterCacheSizeMultiple;
		int size = size() ;

		if( size < threshold){
			return true;
		}
		logger.warn("内部过滤缓存队列数量{},超过阈值{}",size,threshold);
		return false;
	}
	
	//判断过滤缓存队列数量，避免造成内存溢出	
	public void process(BusinessRouteValue businessRouteValue){
		add(businessRouteValue);
	}
	
	/**
	 * 对外提供缓存队列大小
	 * @return
	 */
	public int getSize(){
		return  size();
	}
	
	public void doRun() throws Exception {
		logger.info("内部过滤缓存队列数量{}",size());
		Thread.sleep(FixedConstant.COMMON_MONITOR_INTERVAL_TIME);
	}
	
}


