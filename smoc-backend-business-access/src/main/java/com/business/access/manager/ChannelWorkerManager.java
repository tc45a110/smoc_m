/**
 * @desc
 * 
 */
package com.business.access.manager;

import com.base.common.constant.FixedConstant;
import com.base.common.manager.AlarmManager;
import com.base.common.manager.BusinessDataManager;
import com.base.common.manager.ResourceManager;
import com.base.common.vo.AlarmMessage;
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
		channelWorker.process(businessRouteValue.getBusinessMessageID(), businessRouteValue);
		logger.debug(
				new StringBuilder().append("添加到通道队列")
				.append("{}accountID={}")
				.append("{}phoneNumber={}")
				.append("{}channelID={}")
				.append("{}businessMessageID={}")
				.toString(),
				FixedConstant.SPLICER,businessRouteValue.getAccountID(),
				FixedConstant.SPLICER,businessRouteValue.getPhoneNumber(),
				FixedConstant.SPLICER,businessRouteValue.getChannelID(),
				FixedConstant.SPLICER,businessRouteValue.getBusinessMessageID()
				);
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
		isOverCacheThreshold();
		Thread.sleep(FixedConstant.COMMON_MONITOR_INTERVAL_TIME);
	}
	
	/**
	 * 超过阈值
	 * @return
	 */
	private void isOverCacheThreshold(){
		//单个通道缓存数与单次加载条数的倍数
		int channelWorkerCacheSizeMultiple = ResourceManager.getInstance().getIntValue("channel.worker.cache.size.multiple");
		if(channelWorkerCacheSizeMultiple == 0){
			channelWorkerCacheSizeMultiple = 5;
		}
		//所有通道缓总数与单次加载条数的倍数
		int channelCacheSizeMultiple = ResourceManager.getInstance().getIntValue("channel.cache.size.multiple");
		if(channelCacheSizeMultiple == 0){
			channelCacheSizeMultiple = 100;
		}
		//单个channel worker的缓存阈值
		int channelWorkerThreshold = BusinessDataManager.getInstance().getMessageLoadMaxNumber()*channelWorkerCacheSizeMultiple;
		//总的channel worker的缓存阈值
		int channelThreshold = BusinessDataManager.getInstance().getMessageLoadMaxNumber()*channelCacheSizeMultiple;
		
		int total = 0;
		//判断单个channel worker的缓存数量是否超过阈值
		for(ChannelWorker  channelWorker  : superMap.values()){
			int size = channelWorker.getSize();
			if(size > channelWorkerThreshold  ){
				logger.warn("通道{}缓存队列数量{},超过缓存阈值{}",channelWorker.getChannelID(),size,channelWorkerThreshold);			
				AlarmManager.getInstance().process(new AlarmMessage(AlarmMessage.AlarmKey.ChannelWorker, 
						new StringBuilder().append(channelWorker.getChannelID()).append(",").append(size).toString())
				);
			}
			total+=size;
		}
		logger.info("通道总的缓存队列数量{}",total);
		//判断所有channel worker的缓存数量是否超过阈值
		if(total > channelThreshold ){
			logger.warn("通道总的缓存队列数量{},超过缓存阈值{}",total,channelThreshold);
			AlarmManager.getInstance().process(new AlarmMessage(AlarmMessage.AlarmKey.ChannelWorker,new StringBuilder().append("ALL").append(",").append(total).toString()));
		}
	}
	
}


