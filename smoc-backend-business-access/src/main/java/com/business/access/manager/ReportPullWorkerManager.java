/**
 * @desc
 * 
 */
package com.business.access.manager;

import com.base.common.constant.FixedConstant;
import com.base.common.manager.ResourceManager;
import com.base.common.worker.SuperMapWorker;
import com.business.access.worker.ReportPullWorker;
import com.business.access.worker.ReportStoreWorker;

public class ReportPullWorkerManager extends SuperMapWorker<String, ReportPullWorker>{
	
	//启动线程数
	private static int reportPullWorkerThreadSize = ResourceManager.getInstance().getIntValue("report.pull.worker.thread.size");

	
	private static ReportPullWorkerManager manager = new ReportPullWorkerManager();
	
	private ReportStoreWorker reportStoreWorker;
		
	private ReportPullWorkerManager(){
		
		reportStoreWorker = new ReportStoreWorker();
		reportStoreWorker.start();
		
		//当未配置时按住cpu的核数
		if(reportPullWorkerThreadSize == 0){
			reportPullWorkerThreadSize = FixedConstant.CPU_NUMBER;
		}
		
		//启动cpu的数量*8的系
		for(int i=0;i<reportPullWorkerThreadSize;i++){
			ReportPullWorker reportPullWorker = new ReportPullWorker(reportStoreWorker);
			reportPullWorker.setName(new StringBuilder("ReportPullWorker-").append(i+1).toString());
			reportPullWorker.start();
			add(String.valueOf(i), reportPullWorker);
		}
		System.out.println("状态报告拉取线程数:"+reportPullWorkerThreadSize);
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


