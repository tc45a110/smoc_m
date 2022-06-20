/**
 * @desc
 * 
 */
package com.business.access.manager;

import com.base.common.constant.FixedConstant;
import com.base.common.worker.SuperMapWorker;
import com.business.access.worker.ReportPullWorker;
import com.business.access.worker.ReportStoreWorker;

public class ReportPullWorkerManager extends SuperMapWorker<String, ReportPullWorker>{
	
	private static ReportPullWorkerManager manager = new ReportPullWorkerManager();
	
	private ReportStoreWorker reportStoreWorker;
		
	private ReportPullWorkerManager(){
		
		reportStoreWorker = new ReportStoreWorker();
		reportStoreWorker.start();
		
		//启动cpu的数量*8的系数
		for(int i=0;i<FixedConstant.CPU_NUMBER*8;i++){
			ReportPullWorker reportPullWorker = new ReportPullWorker(reportStoreWorker);
			reportPullWorker.setName(new StringBuilder("ReportPullWorker-").append(i+1).toString());
			reportPullWorker.start();
			add(String.valueOf(i), reportPullWorker);
		}
		this.start();
	}
	
	public static ReportPullWorkerManager getInstance(){
		return manager;
	}
	
	
	public void doRun() throws Exception {
		sleep(INTERVAL);
	}
	
	/**
	 * 退出线程
	 */
	public void exit(){
		for(ReportPullWorker reportPullWorker : superMap.values()){
			reportPullWorker.exit();
		}
		reportStoreWorker.exit();
	}
	
}


