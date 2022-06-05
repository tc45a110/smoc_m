package com.base.common.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.cache.CacheBaseService;
import com.base.common.constant.FixedConstant;
import com.base.common.util.DateUtil;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperQueueWorker;

/**
 * 提交信息失败管理
 */
public class MessageSubmitFailManager extends SuperQueueWorker<BusinessRouteValue>{
	private static final Logger logger = LoggerFactory
			.getLogger(MessageSubmitFailManager.class);
	private static MessageSubmitFailManager manager = new MessageSubmitFailManager();
	public static MessageSubmitFailManager getInstance(){
		return manager;
	}
	
	private MessageSubmitFailManager(){
		this.start();
	}
	
	/**
	 * 处理没有成功下发到通道的信息
	 * @param businessRouteValue
	 */
	public void process(BusinessRouteValue businessRouteValue){
		businessRouteValue.setRouteLabel(FixedConstant.RouteLable.MR.name());
		businessRouteValue.setChannelReportTime(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI));
		add(businessRouteValue);
	}
	
	@Override
	protected void doRun() throws Exception {
		BusinessRouteValue businessRouteValue = take();
		String accountMessageIDs =businessRouteValue.getAccountMessageIDs();
		int messageIndex = 0;
		for(String accountMessageID : accountMessageIDs.split(FixedConstant.SPLICER)){
			messageIndex++;
			businessRouteValue.setAccountMessageIDs(accountMessageID);
			businessRouteValue.setMessageIndex(messageIndex);
			logger.info("模拟回执{}{}",FixedConstant.SPLICER,businessRouteValue.toString());
			CacheBaseService.saveBusinessReportToMiddlewareCache(businessRouteValue);
		}
		
	}
	
}


