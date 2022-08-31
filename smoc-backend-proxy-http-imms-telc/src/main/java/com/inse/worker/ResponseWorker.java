/**
 * @desc
 * 
 */
package com.inse.worker;
import com.base.common.cache.CacheBaseService;
import com.base.common.constant.DynamicConstant;
import com.base.common.constant.FixedConstant;
import com.base.common.constant.InsideStatusCodeConstant;
import com.base.common.manager.ChannelInfoManager;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperQueueWorker;


public class ResponseWorker extends SuperQueueWorker<BusinessRouteValue>{
	private String channelID;

	ResponseWorker(String channelID, String index) {
		this.channelID = channelID;
		this.setName(new StringBuilder("ResponseWorker-").append(channelID).append("-").append(index).toString());
		this.start();
	}

	@Override
	protected void doRun() throws Exception {
		// 取出响应消息
		BusinessRouteValue businessRouteValue = poll();
		if (businessRouteValue == null) {
			return;
		}
		if (DynamicConstant.RESPONSE_SUCCESS_CODE.equals(businessRouteValue.getNextNodeCode())) {
			businessRouteValue.setNextNodeErrorCode(businessRouteValue.getNextNodeCode());
			businessRouteValue.setNextNodeCode(InsideStatusCodeConstant.SUCCESS_CODE);

			businessRouteValue.setChannelTotal(1);
			businessRouteValue.setChannelIndex(1);
		} else {
			businessRouteValue.setNextNodeErrorCode(businessRouteValue.getNextNodeCode());
			businessRouteValue.setNextNodeCode(InsideStatusCodeConstant.FAIL_CODE);
			businessRouteValue.setChannelTotal(1);
			businessRouteValue.setChannelIndex(1);
		}

		businessRouteValue.setChannelSRCID(ChannelInfoManager.getInstance().getChannelSRCID(channelID));
		CacheBaseService.saveResponseToMiddlewareCache(businessRouteValue);
		logger.info(new StringBuilder().append("响应信息")      
			      .append("{}phoneNumber={}")
			      .append("{}messageContent={}")
			      .append("{}nextNodeCode={}")
			      .append("{}channelID={}")
			      .append("{}channelMessageID={}").toString(),
			      
			      FixedConstant.SPLICER,businessRouteValue.getPhoneNumber(),
			      FixedConstant.SPLICER,businessRouteValue.getMessageContent(),
			      FixedConstant.SPLICER,businessRouteValue.getNextNodeCode(),
			      FixedConstant.SPLICER,businessRouteValue.getChannelID(),
			      FixedConstant.SPLICER,businessRouteValue.getChannelMessageID());                  
	}

}
