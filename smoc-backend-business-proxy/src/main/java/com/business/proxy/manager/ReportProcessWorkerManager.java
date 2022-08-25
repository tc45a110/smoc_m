/**
 * @desc
 * 
 */
package com.business.proxy.manager;

import com.base.common.constant.FixedConstant;
import com.base.common.manager.ResourceManager;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperMapWorker;
import com.business.proxy.worker.ReportProcessWorker;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ReportProcessWorkerManager extends SuperMapWorker<Integer, ReportProcessWorker>{
	
	private static int reportProcessWorkerNumber = ResourceManager.getInstance().getIntValue("report.process.worker.number");
	
	private static ReportProcessWorkerManager manager = new ReportProcessWorkerManager();
	
	private ThreadPoolExecutor threadPoolExecutor;
		
	private ReportProcessWorkerManager(){
		if(reportProcessWorkerNumber == 0) {
			reportProcessWorkerNumber = FixedConstant.CPU_NUMBER;
		}
		threadPoolExecutor = new ThreadPoolExecutor(FixedConstant.CPU_NUMBER,FixedConstant.CPU_NUMBER*2, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		//启动cpu的数量*8的系数 按手机号尾号后3位平均分配
		for(int i=0;i<reportProcessWorkerNumber;i++){
			ReportProcessWorker reportProcessWorker = new ReportProcessWorker(threadPoolExecutor);
			reportProcessWorker.setName(new StringBuilder("ReportProcessWorker-").append(i+1).toString());
			reportProcessWorker.start();
			add(i, reportProcessWorker);
		}
		this.start();
	}
	
	/**
	 * 处理状态报告：避免同一条状态报告重复记录，通过手机尾号分发
	 * @param businessRouteValueReport
	 */
	public void process(BusinessRouteValue businessRouteValueReport){
		 String phoneNumber = businessRouteValueReport.getPhoneNumber();
		 //截取后3位
		 get(Integer.parseInt(phoneNumber.substring(phoneNumber.length() - 3, phoneNumber.length())) % size()).add(businessRouteValueReport);
	}
	
	public static ReportProcessWorkerManager getInstance(){
		return manager;
	}
	
	
	public void doRun() throws Exception {
		sleep(INTERVAL);
	}
	
	/**
	 * 退出线程
	 */
	public void exit(){
		for(ReportProcessWorker reportPullWorker : superMap.values()){
			reportPullWorker.exit();
		}
	}
	
}


