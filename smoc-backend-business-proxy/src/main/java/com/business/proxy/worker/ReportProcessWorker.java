 /**
 * @desc
 * 从通道表中按照优先级及时间先后获取数据，每次按照通道的速率进行获取，存入到队列中
 */
package com.business.proxy.worker;


import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import com.base.common.cache.CacheBaseService;
import com.base.common.constant.FixedConstant;
import com.base.common.constant.InsideStatusCodeConstant;
import com.base.common.log.PorxyBusinessLogManager;
import com.base.common.manager.BusinessDataManager;
import com.base.common.manager.ChannelInfoManager;
import com.base.common.manager.ChannelPriceManager;
import com.base.common.manager.FailRepairManager;
import com.base.common.util.Commons;
import com.base.common.util.DateUtil;
import com.base.common.util.MessageContentUtil;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperQueueWorker;
import com.business.proxy.manager.ChannelRepairWorkerManager;
import com.business.proxy.manager.LongChannelRepairWorkerManager;


public class ReportProcessWorker extends SuperQueueWorker<BusinessRouteValue>{

	@Override
	protected void doRun() throws Exception {
		BusinessRouteValue businessRouteValueReport = take();
		logger.info(
				new StringBuilder().append("状态报告")
				.append("{}{}")
				.toString(),
				FixedConstant.SPLICER,businessRouteValueReport.toString()
				);
		//获取中间件缓存中的提交信息
		BusinessRouteValue businessRouteValue = CacheBaseService.getBusinessRouteValueFromMiddlewareCache(businessRouteValueReport);
		//匹配到提交信息
		if(businessRouteValue != null){
			
			logger.info(
					new StringBuilder().append("状态报告匹配到提交信息")
					.append("{}{}")
					.toString(),
					FixedConstant.SPLICER,businessRouteValue.toString()
					);
			
			CacheBaseService.deleteBusinessRouteValueFromMiddlewareCache(businessRouteValueReport);
			businessRouteValue.setBusinessRouteValueReport(businessRouteValueReport);
			businessRouteValue.setRouteLabel(FixedConstant.RouteLable.MR.name());
			String [] messageIDsArray = businessRouteValue.getAccountMessageIDs().split(FixedConstant.SPLICER);
			
			for(int i = 0; i < messageIDsArray.length; i++) {
				businessRouteValue.setAccountMessageIDs(messageIDsArray[i]);
				//如果解析的msgID数组大于1    则为提交到平台为多条数据  下发合成为1条  状态报告返回为1条  如：cmpp -- http
				if(messageIDsArray.length > 1) {
					businessRouteValue.setMessageIndex(i+1);
				}
				doLog(businessRouteValue);
			}
			
			//维护通道日，月成功量
			if(InsideStatusCodeConstant.SUCCESS_CODE.equals(businessRouteValue.getSuccessCode()) ){
				//向接入业务层状态报告队列中存入状态报告    
				for(int i = 0; i < messageIDsArray.length; i++) {
					businessRouteValue.setAccountMessageIDs(messageIDsArray[i]);
					//如果解析的msgID数组大于1    则为提交到平台为多条数据  下发合成为1条  状态报告返回为1条  如：cmpp -- http
					if(messageIDsArray.length > 1) {
						businessRouteValue.setMessageIndex(i+1);
					}
					CacheBaseService.saveBusinessReportToMiddlewareCache(businessRouteValue);
				}
				
				CacheBaseService.saveChannelSuccessNumberToMiddlewareCache(businessRouteValue.getChannelID(), messageIDsArray.length);
				
				//获取日限量方式：为
				int sendLimitStyleDaily = BusinessDataManager.getInstance().getSendLimitStyleDaily(businessRouteValue.getAccountID());
				if(FixedConstant.SendLimitStyleDaily.RP_MESSAGE_CONTENT.ordinal() == sendLimitStyleDaily){
					CacheBaseService.saveAccountCarrierDailyToMiddlewareCache(
							businessRouteValue.getAccountID(), 
							businessRouteValue.getBusinessCarrier(), 
							messageIDsArray.length);
				}else if (FixedConstant.SendLimitStyleDaily.RP_PHONE_NUMBER.ordinal() == sendLimitStyleDaily){
					if(messageIDsArray.length > 1 || businessRouteValue.getMessageIndex() == 1){
						CacheBaseService.saveAccountCarrierDailyToMiddlewareCache(
								businessRouteValue.getAccountID(), 
								businessRouteValue.getBusinessCarrier(), 
								1);
					}

				}
			}else {
				//失败补发
				String accountID = businessRouteValue.getAccountID();
				String businessCarrier = businessRouteValue.getBusinessCarrier();
				String statusCode = businessRouteValue.getStatusCode();
				String channelID = businessRouteValue.getChannelID();
				int repeatSendTimes = businessRouteValue.getRepeatSendTimes();
				int intervalTime = DateUtil.getIntervalTime(businessRouteValue.getAccountSubmitTime(), DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI));
				
				boolean reportFlag = true;
				ArrayList<String> channelRepairList;
				int repairTime;
				
				if(repeatSendTimes > 0) {
					//已补发过
					channelRepairList = businessRouteValue.getChannelRepairIDs();
					repairTime = businessRouteValue.getRepairTime();
					if(repairTime > intervalTime && channelRepairList.size() > repeatSendTimes) {
						//进行补发
						repair(businessRouteValue,channelRepairList.get(repeatSendTimes));
						reportFlag = false;
					}
				}else {
					//初补发
					channelRepairList = FailRepairManager.getInstance().getChannelRepairID(accountID, businessCarrier, statusCode, channelID);
					if(channelRepairList != null) {
						repairTime = FailRepairManager.getInstance().getRepairTime(accountID, businessCarrier, channelID) * 60;
						if(repairTime > intervalTime) {
							//进行补发
							businessRouteValue.setChannelRepairIDs(channelRepairList);
							businessRouteValue.setRepairTime(repairTime);
							repair(businessRouteValue,channelRepairList.get(0));
							reportFlag = false;
						}
					}
				}
				
				if(reportFlag) {
					//向接入业务层状态报告队列中存入状态报告    
					for(int i = 0; i < messageIDsArray.length; i++) {
						businessRouteValue.setAccountMessageIDs(messageIDsArray[i]);
						//如果解析的msgID数组大于1    则为提交到平台为多条数据  下发合成为1条  状态报告返回为1条  如：cmpp -- http
						if(messageIDsArray.length > 1) {
							businessRouteValue.setMessageIndex(i+1);
						}
						CacheBaseService.saveBusinessReportToMiddlewareCache(businessRouteValue);
					}
				}
			}
			
		}else{
			logger.warn(
					new StringBuilder().append("状态报告未匹配到提交信息")
					.append("{}channelID={}")
					.append("{}phoneNumber={}")
					.append("{}channelMessageID={}")
					.append("{}channelReportSRCID={}")
					.append("{}statusCode={}")
					.append("{}subStatusCode={}")
					.append("{}successCode={}")
					.toString(),
					FixedConstant.SPLICER,businessRouteValueReport.getChannelID(),
					FixedConstant.SPLICER,businessRouteValueReport.getPhoneNumber(),
					FixedConstant.SPLICER,businessRouteValueReport.getChannelMessageID(),
					FixedConstant.SPLICER,businessRouteValueReport.getChannelReportSRCID(),
					FixedConstant.SPLICER,businessRouteValueReport.getStatusCode(),
					FixedConstant.SPLICER,businessRouteValueReport.getSubStatusCode(),
					FixedConstant.SPLICER,businessRouteValueReport.getSuccessCode()
					);
		}
	}

	private void repair(BusinessRouteValue businessRouteValue, String channelID) {
		businessRouteValue.setChannelID(channelID);
		businessRouteValue.setChannelSRCID(ChannelInfoManager.getInstance().getChannelSRCID(channelID));
		
		//获取通道价格
		if(FixedConstant.PriceStyle.AREA_PRICE.name().equals(ChannelInfoManager.getInstance().getPriceStyle(channelID))) {
			businessRouteValue.setPriceAreaCode(businessRouteValue.getAreaCode());
			businessRouteValue.setChannelPrice(ChannelPriceManager.getInstance().getPrice(channelID, businessRouteValue.getAreaCode()));
		}else {
			//省份编码/国家编码,当通道不区分省份计价时，该值为ALL
			businessRouteValue.setPriceAreaCode(Commons.UNIFIED_PRICING_CODE);
			businessRouteValue.setChannelPrice(ChannelPriceManager.getInstance().getPrice(channelID, Commons.UNIFIED_PRICING_CODE));
		}
		businessRouteValue.setRepeatSendTimes(businessRouteValue.getRepeatSendTimes() + 1);
		businessRouteValue.setQueueSubmitTime(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI));
		
		logger.info(
				new StringBuilder().append("失败补发")
				.append("{}accountID={}")
				.append("{}phoneNumber={}")
				.append("{}messageContent={}")
				.append("{}businessCarrier={}")
				.append("{}channelID={}")
				.append("{}repeatSendTimes={}")
				.toString(),
				FixedConstant.SPLICER,businessRouteValue.getAccountID(),
				FixedConstant.SPLICER,businessRouteValue.getPhoneNumber(),
				FixedConstant.SPLICER,businessRouteValue.getMessageContent(),
				FixedConstant.SPLICER,businessRouteValue.getBusinessCarrier(),
				FixedConstant.SPLICER,businessRouteValue.getChannelID(),
				FixedConstant.SPLICER,businessRouteValue.getRepeatSendTimes()
				);
		
		//长短信区分
		if(businessRouteValue.getChannelTotal() == 1) {
			ChannelRepairWorkerManager.getInstance().saveBusinessRouteValue(channelID, businessRouteValue);
		}else {
			LongChannelRepairWorkerManager.getInstance().process(businessRouteValue);
		}
	}
	
	private void doLog(BusinessRouteValue businessRouteValue) {
		businessRouteValue.setRouteLabel(FixedConstant.RouteLable.MR.name());
		StringBuilder sb = new StringBuilder()
		.append(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI))
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getAccountID())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getEnterpriseFlag())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getProtocol())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getAccountSubmitTime())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getTableSubmitTime())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getQueueSubmitTime())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getChannelSubmitTime())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getChannelReportTime())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getBusinessMessageID())//10-业务消息标识
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getPhoneNumber())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getSegmentCarrier())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getBusinessCarrier())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getAreaCode())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getAreaName())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getCityName())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getChannelSubmitSRCID())//17-通道下发代码
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getAccountExtendCode())//18-平台下发扩展码
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getAccountBusinessCode())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getAccountSubmitSRCID())//20-客户完整接入代码
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getMessageFormat())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(MessageContentUtil.handlingLineBreakCommas(businessRouteValue.getMessageContent()))
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getMessageSignature())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getInfoType())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getIndustryTypes())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getAccountMessageIDs())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getChannelID())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getChannelSRCID())//28-通道基础接入码
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getSuccessCode())//29-成功标识状态码
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getStatusCode())//30-状态码
		.append(FixedConstant.LOG_SEPARATOR)
		.append(StringUtils.defaultString(businessRouteValue.getSubStatusCode()))//31-状态子码
		.append(FixedConstant.LOG_SEPARATOR)
		.append(DateUtil.getIntervalTime(businessRouteValue.getChannelSubmitTime(), businessRouteValue.getChannelReportTime()))//32-间隔时间
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getChannelTotal())//33-通道下发总条数
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getChannelIndex())//34-通道下发序号
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getChannelTotal()==1?businessRouteValue.getMessageNumber():1)//35-通道本条计费条数
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getChannelMessageID())//36-状态报告消息id
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getChannelPrice())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getPriceAreaCode())//38-价格区域编码
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getFinanceAccountID())//39-计费账号
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getMessageNumber())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getMessagePrice())//41-账号计费单价
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getMessageAmount())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getBusinessType())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getAccountTemplateID())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getRepeatSendTimes())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getOptionParam())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getConsumeType())//47-扣费类型
		.append(System.getProperty("line.separator"));
		PorxyBusinessLogManager.getInstance().process(sb.toString(),businessRouteValue.getChannelID(),businessRouteValue.getRouteLabel());
	}
}


