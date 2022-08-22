/**
 * @desc
 * 
 */
package com.business.proxy.manager;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.base.common.constant.FixedConstant;
import com.base.common.worker.SuperMapWorker;
import com.business.proxy.worker.ResponsePullWorker;

public class ResponsePullWorkerManager extends SuperMapWorker<String, ResponsePullWorker>{
	
	private static ResponsePullWorkerManager manager = new ResponsePullWorkerManager();
	
	private ThreadPoolExecutor threadPoolExecutor;
		
	private ResponsePullWorkerManager(){
		threadPoolExecutor = new ThreadPoolExecutor(FixedConstant.CPU_NUMBER,FixedConstant.CPU_NUMBER*2, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		//启动cpu的数量*8的系数
		for(int i=0;i<FixedConstant.CPU_NUMBER*2;i++){
			ResponsePullWorker responsePullWorker = new ResponsePullWorker(threadPoolExecutor);
			responsePullWorker.setName(new StringBuilder("ResponsePullWorker-").append(i+1).toString());
			responsePullWorker.start();
			add(String.valueOf(i), responsePullWorker);
		}
		this.start();
	}
	
	public static ResponsePullWorkerManager getInstance(){
		return manager;
	}
	
	
	public void doRun() throws Exception {
		sleep(INTERVAL);
	}
	
	/**
	 * 退出线程
	 */
	public void exit(){
		for(ResponsePullWorker responsePullWorker : superMap.values()){
			responsePullWorker.exit();
		}
		System.out.println("线程池停止前待处理响应数据条数:"+threadPoolExecutor.getQueue().size());
		while(true){
			if(threadPoolExecutor.getQueue().size() == 0){
				threadPoolExecutor.shutdown();
				break;
			}	
		}
		System.out.println("线程池停止后待处理响应数据条数:"+threadPoolExecutor.getQueue().size());
	}
	
}


