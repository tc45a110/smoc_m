/**
 * @desc
 * 
 */
package com.protocol.access.manager;

import com.base.common.constant.FixedConstant;
import com.base.common.worker.SuperMapWorker;
import com.protocol.access.vo.MessageInfo;
import com.protocol.access.worker.SubmitWorker;

public class SubmitWorkerManager extends SuperMapWorker<Integer, SubmitWorker>{
	
	private static SubmitWorkerManager manager = new SubmitWorkerManager();
	
	private SubmitWorkerManager(){
		//启动cpu的数量*8的系数
		for(int i=0;i<FixedConstant.CPU_NUMBER*8;i++){
			SubmitWorker submitWorker = new SubmitWorker();
			submitWorker.setName(new StringBuilder("SubmitWorker-").append(i+1).toString());
			submitWorker.start();
			add(i, submitWorker);
		}
		this.start();
	}
	
	public static SubmitWorkerManager getInstance(){
		return manager;
	}
	
	
	public void doRun() throws Exception {
		sleep(INTERVAL);
	}
	
	/**
	 * 处理提交数据，随机存放到线程中
	 * @param businessRouteValue
	 */
	public void process(MessageInfo vo){
		superMap.get((int)(superMap.size() * Math.random())).put(vo.getMessageId(), vo);
	}
	
	/**
	 * 退出线程
	 */
	public void exit(){
		for(SubmitWorker submitWorker : superMap.values()){
			submitWorker.exit();
		}
	}
	
}


