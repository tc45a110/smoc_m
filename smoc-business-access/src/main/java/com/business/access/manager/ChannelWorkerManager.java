/**
 * @desc
 * 
 */
package com.business.access.manager;

import com.base.common.constant.FixedConstant;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperMapWorker;
import com.business.access.worker.ChannelWorker;

public class ChannelWorkerManager extends SuperMapWorker<String,ChannelWorker>{
	
	private static ChannelWorkerManager manager = new ChannelWorkerManager();
	
	private ChannelWorkerManager(){
		this.start();
	}
	
	public static ChannelWorkerManager getInstance(){
		return manager;
	}
	
	public void process(BusinessRouteValue businessRouteValue){
		String channelID = businessRouteValue.getChannelID();
		ChannelWorker channelWorker = get(channelID);
		//当通道线程不存在时，需要初始化
		if(channelWorker == null){
			channelWorker = register(channelID);
			if(channelWorker == null){
				logger.error(
						new StringBuilder().append("通道线程不存在")
						.append("{}accountID={}")
						.append("{}phoneNumber={}")
						.append("{}channelID={}")
						.append("{}messageContent={}")
						.toString(),
						FixedConstant.SPLICER,businessRouteValue.getAccountID(),
						FixedConstant.SPLICER,businessRouteValue.getPhoneNumber(),
						FixedConstant.SPLICER,businessRouteValue.getChannelID(),
						FixedConstant.SPLICER,businessRouteValue.getMessageContent()
						);
				return;
			}

		}
		channelWorker.add(businessRouteValue);
	}
	
	private ChannelWorker register(String channelID){
		ChannelWorker channelWorker = new ChannelWorker(channelID);
		channelWorker.start();
		add(channelID, channelWorker);
		logger.info("{}注册线程",channelID);
		return channelWorker;
	}

	@Override
	public void doRun() throws Exception {
		logger.info("通道线程数{}",size());
		Thread.sleep(FixedConstant.COMMON_MONITOR_INTERVAL_TIME);
	}
	
}


