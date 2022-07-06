/**
 * @desc
 * 
 */
package com.business.proxy.manager;

import com.base.common.constant.FixedConstant;
import com.base.common.worker.SuperMapWorker;
import com.business.proxy.worker.ResponsePullWorker;

public class ResponsePullWorkerManager extends SuperMapWorker<String, ResponsePullWorker>{
	
	private static ResponsePullWorkerManager manager = new ResponsePullWorkerManager();
		
	private ResponsePullWorkerManager(){
		//启动cpu的数量*8的系数
		for(int i=0;i<FixedConstant.CPU_NUMBER*2;i++){
			ResponsePullWorker responsePullWorker = new ResponsePullWorker();
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
	}
	
}


