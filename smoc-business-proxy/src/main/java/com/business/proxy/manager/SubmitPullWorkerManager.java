/**
 * @desc
 * 
 */
package com.business.proxy.manager;

import java.util.Set;

import com.base.common.manager.ChannelInfoManager;
import com.base.common.worker.SuperMapWorker;
import com.business.proxy.worker.SubmitPullWorker;

public class SubmitPullWorkerManager extends SuperMapWorker<String, SubmitPullWorker>{
	
	private static SubmitPullWorkerManager manager = new SubmitPullWorkerManager();
		
	private SubmitPullWorkerManager(){
		this.start();
	}
	
	public static SubmitPullWorkerManager getInstance(){
		return manager;
	}
	
	
	public void doRun() throws Exception {
		//加载所有的有效通道，当map中不存在该线程时，需要进行加载。
		Set<String> channelIDSet = ChannelInfoManager.getInstance().getAvailableChannelIDs();
		for(String channelID : channelIDSet){
			SubmitPullWorker submitPullWorker = get(channelID);
			if(submitPullWorker == null){
				submitPullWorker = new SubmitPullWorker(channelID);
				add(channelID, submitPullWorker);
			}
		}
		Thread.sleep(INTERVAL);
	}
	
	/**
	 * 退出线程
	 */
	public void exit(){
		for(SubmitPullWorker submitPullWorker : superMap.values()){
			submitPullWorker.exit();
		}
	}
	
}


