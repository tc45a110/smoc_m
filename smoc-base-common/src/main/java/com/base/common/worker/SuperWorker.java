/**
 * @desc
 * 
 */
package com.base.common.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.constant.FixedConstant;

abstract class SuperWorker extends Thread{
	
	protected static final Logger logger = LoggerFactory.getLogger(SuperWorker.class);
	
	protected Object lock = new Object();
	
	protected long INTERVAL = FixedConstant.COMMON_EFFECTIVE_TIME;
	
	/**
	 * 运行标识
	 */
	private boolean runFlag = true;

	public void run() {
		
		while(runFlag){
			try {
				doRun();
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
		}
		logger.info("线程退出循环");
		
	}
	
	/**
	 * 退出线程
	 */
	public void exit(){
		runFlag = false;
	}
	
	/**
	 * 执行run方法需要处理的逻辑，由子类具体实现
	 * @throws Exception
	 */
	protected abstract void doRun() throws Exception;
	
	/**
	 * 控制提交速率
	 * @param interval
	 * @param costTime
	 */
	protected void controlSubmitSpeed(long interval,long costTime) throws Exception{
		if(interval > costTime){
			Thread.sleep(interval-costTime);
		}
	}
}


