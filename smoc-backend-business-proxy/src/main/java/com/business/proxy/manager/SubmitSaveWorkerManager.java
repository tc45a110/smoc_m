package com.business.proxy.manager;

import java.util.concurrent.BlockingQueue;

import com.base.common.cache.CacheBaseService;
import com.base.common.constant.FixedConstant;
import com.base.common.util.DateUtil;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperCacheWorker;
import com.base.common.worker.SuperQueueWorker;

/**
 * 提交信息并发方式往redis队列保存
 */
public class SubmitSaveWorkerManager extends SuperQueueWorker<BusinessRouteValue>{

	private static SubmitSaveWorkerManager manager = new SubmitSaveWorkerManager();
	public static SubmitSaveWorkerManager getInstance(){
		return manager;
	}
	
	private SubmitSaveWorkerManager(){
		//启动cpu的数量*8的系数
		for(int i=0;i<FixedConstant.CPU_NUMBER*2;i++){
			SubmitSaveWorker submitSaveWorker = new SubmitSaveWorker(superQueue);
			submitSaveWorker.setName(new StringBuilder("SubmitSaveWorker-").append(i).toString());
			submitSaveWorker.start();
		}
		this.start();
	}
	
	/**
	 * 处理没有成功下发到通道的信息
	 * @param businessRouteValue
	 */
	public void process(BusinessRouteValue businessRouteValue){
		add(businessRouteValue);
	}
	
	@Override
	protected void doRun() throws Exception {
		Thread.sleep(FixedConstant.COMMON_MONITOR_INTERVAL_TIME);
	}
	
	class SubmitSaveWorker extends SuperCacheWorker{
		
		private BlockingQueue<BusinessRouteValue> businessRouteValueQueue; 
		
		public SubmitSaveWorker(
				BlockingQueue<BusinessRouteValue> businessRouteValueQueue) {
			super();
			this.businessRouteValueQueue = businessRouteValueQueue;
		}

		@Override
		public void doRun() throws Exception {
			BusinessRouteValue businessRouteValue = businessRouteValueQueue.take();
			long startTime = System.currentTimeMillis();
			//保存在下发队列中
			businessRouteValue.setQueueSubmitTime(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI));
			CacheBaseService.saveSubmitToMiddlewareCache(businessRouteValue.getChannelID(), businessRouteValue);
			logger.info(
					new StringBuilder().append("通道状态正常,存入通道队列")
					.append("{}accountID={}")
					.append("{}phoneNumber={}")
					.append("{}messageContent={}")
					.append("{}channelID={}")
					.append("{}耗时={}")
					.toString(),
					FixedConstant.SPLICER,businessRouteValue.getAccountID(),
					FixedConstant.SPLICER,businessRouteValue.getPhoneNumber(),
					FixedConstant.SPLICER,businessRouteValue.getMessageContent(),
					FixedConstant.SPLICER,businessRouteValue.getChannelID(),
					FixedConstant.SPLICER,(System.currentTimeMillis() - startTime)
					);
		}

	}
	
}


