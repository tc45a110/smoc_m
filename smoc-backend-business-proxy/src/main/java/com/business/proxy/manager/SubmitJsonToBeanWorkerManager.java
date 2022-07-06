package com.business.proxy.manager;

import java.util.concurrent.BlockingQueue;

import com.base.common.constant.FixedConstant;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperCacheWorker;
import com.base.common.worker.SuperQueueWorker;
import com.business.proxy.vo.SubmitJson;

/**
 * 提交信息并发方式往redis队列保存
 */
public class SubmitJsonToBeanWorkerManager extends SuperQueueWorker<SubmitJson>{

	private static SubmitJsonToBeanWorkerManager manager = new SubmitJsonToBeanWorkerManager();
	public static SubmitJsonToBeanWorkerManager getInstance(){
		return manager;
	}
	
	private SubmitJsonToBeanWorkerManager(){
		//启动cpu的数量*8的系数
		for(int i=0;i<FixedConstant.CPU_NUMBER;i++){
			SubmitJsonToBeanWorker submitJsonToBeanWorker = new SubmitJsonToBeanWorker(superQueue);
			submitJsonToBeanWorker.setName(new StringBuilder("SubmitJsonToBeanWorker-").append(i).toString());
			submitJsonToBeanWorker.start();
		}
		this.start();
	}
	
	/**
	 * 处理没有成功下发到通道的信息
	 * @param businessRouteValue
	 */
	public void process(SubmitJson submitJson){
		add(submitJson);
	}
	
	@Override
	protected void doRun() throws Exception {
		Thread.sleep(FixedConstant.COMMON_MONITOR_INTERVAL_TIME);
	}
	
	class SubmitJsonToBeanWorker extends SuperCacheWorker{
		
		private BlockingQueue<SubmitJson> submitJsonQueue; 
		
		public SubmitJsonToBeanWorker(
				BlockingQueue<SubmitJson> submitJsonQueue) {
			super();
			this.submitJsonQueue = submitJsonQueue;
		}

		@Override
		public void doRun() throws Exception {
			SubmitJson submitJson = submitJsonQueue.take();
			long startTime = System.currentTimeMillis();
			BusinessRouteValue businessRouteValue = BusinessRouteValue.toObject(submitJson.getMessageJson());
			businessRouteValue.setMessageContent(submitJson.getMessageContent());
			businessRouteValue.setTableSubmitTime(submitJson.getTableSubmitTime());
			logger.info(
					new StringBuilder().append("数据封装")
					.append("{}accountID={}")
					.append("{}phoneNumber={}")
					.append("{}messageContent={}")
					.append("{}耗时={}")
					.toString(),
					FixedConstant.SPLICER,businessRouteValue.getAccountID(),
					FixedConstant.SPLICER,businessRouteValue.getPhoneNumber(),
					FixedConstant.SPLICER,businessRouteValue.getMessageContent(),
					FixedConstant.SPLICER,(System.currentTimeMillis() - startTime)
					);
			SubmitSaveWorkerManager.getInstance().process(businessRouteValue);
		}

	}

}


