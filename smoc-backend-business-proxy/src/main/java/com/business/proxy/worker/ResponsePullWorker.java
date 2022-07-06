 /**
 * @desc
 * 从通道表中按照优先级及时间先后获取数据，每次按照通道的速率进行获取，存入到队列中
 */
package com.business.proxy.worker;

import org.apache.commons.lang3.StringUtils;

import com.base.common.cache.CacheBaseService;
import com.base.common.constant.FixedConstant;
import com.base.common.constant.InsideStatusCodeConstant;
import com.base.common.log.PorxyBusinessLogManager;
import com.base.common.manager.BusinessDataManager;
import com.base.common.manager.MessageSubmitFailManager;
import com.base.common.util.DateUtil;
import com.base.common.util.MessageContentUtil;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperCacheWorker;
import com.business.proxy.manager.ChannelSRCIDManager;

public class ResponsePullWorker extends SuperCacheWorker{

	@Override
	protected void doRun() throws Exception {
		BusinessRouteValue businessRouteValue = CacheBaseService.getResponseToMiddlewareCache();
		if(businessRouteValue != null){
			
			ChannelSRCIDManager.getInstance().add(businessRouteValue.getChannelSRCID());
			doLog(businessRouteValue);
			
			if(InsideStatusCodeConstant.UNKNOWN_CODE.equals(businessRouteValue.getNextNodeCode())) {
				logger.warn("提交未响应{}{}",FixedConstant.SPLICER,businessRouteValue.toString());
				//TODO 未知部分暂不返回状态报告，后续通过 未匹配提交信息的状态报告 和 这部分记录进行匹配，尽量减少未知部分
			}else{
				logger.info("提交响应{}{}",FixedConstant.SPLICER,businessRouteValue.toString());
				
				//判断是否提交成功
				if(InsideStatusCodeConstant.SUCCESS_CODE.equals(businessRouteValue.getNextNodeCode())) {
					//存入缓存  用于匹配状态报告
					CacheBaseService.saveBusinessRouteValueToMiddlewareCache(businessRouteValue);
					//获取日限量方式
					int sendLimitStyleDaily = BusinessDataManager.getInstance().getSendLimitStyleDaily(businessRouteValue.getAccountID());
					//重复发送次数
					int repeatSendTimes = businessRouteValue.getRepeatSendTimes();
					//不考虑失败补发的情况
					if(repeatSendTimes == 0){
						if(FixedConstant.SendLimitStyleDaily.MT_MESSAGE_CONTENT.ordinal() == sendLimitStyleDaily){
							CacheBaseService.saveAccountCarrierDailyToMiddlewareCache(
									businessRouteValue.getAccountID(), 
									businessRouteValue.getBusinessCarrier(), 
									businessRouteValue.getAccountMessageIDs().length());
							
						}else if (FixedConstant.SendLimitStyleDaily.MT_PHONE_NUMBER.ordinal() == sendLimitStyleDaily){
							String [] messageIDsArray = businessRouteValue.getAccountMessageIDs().split(FixedConstant.SPLICER);	
							if(messageIDsArray.length > 1 || businessRouteValue.getMessageIndex() == 1){
								CacheBaseService.saveAccountCarrierDailyToMiddlewareCache(
										businessRouteValue.getAccountID(), 
										businessRouteValue.getBusinessCarrier(), 
										1);
							}
							
						}
					}
		
				}else if(InsideStatusCodeConstant.FAIL_CODE.equals(businessRouteValue.getNextNodeCode())) {
					businessRouteValue.setStatusCodeSource(FixedConstant.StatusReportSource.RESPONSE.name());
					businessRouteValue.setStatusCode(businessRouteValue.getNextNodeErrorCode());
					businessRouteValue.setSubStatusCode("");
					MessageSubmitFailManager.getInstance().process(businessRouteValue);
				}			
			}
			
		}else{
			sleep(BusinessDataManager.getInstance().getReportRedisPopIntervalTime());
		}
	}
	
	private void doLog(BusinessRouteValue businessRouteValue) {
		businessRouteValue.setRouteLabel(FixedConstant.RouteLable.MT.name());
		StringBuilder sb = new StringBuilder()
		.append(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI))
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getAccountID())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getEnterpriseFlag())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getProtocol())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(StringUtils.defaultString(businessRouteValue.getAccountSubmitTime()))
		.append(FixedConstant.LOG_SEPARATOR)
		.append(StringUtils.defaultString(businessRouteValue.getTableAuditTime()))
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getTableSubmitTime())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getQueueSubmitTime())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getChannelSubmitTime())
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
		.append(businessRouteValue.getMessageSignature())//23-签名
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getInfoType())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getIndustryTypes())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getAccountPriority())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getAccountMessageIDs())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getChannelID())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getChannelSRCID())//29-通道基础接入码
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getChannelTotal())//30-通道下发总条数
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getChannelIndex())//31-通道下发序号
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getChannelTotal()==1?businessRouteValue.getMessageNumber():1)//32-通道本条计费条数
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getNextNodeCode())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getNextNodeErrorCode())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getChannelMessageID())//35-提交响应消息id
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getChannelPrice())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getPriceAreaCode())//37-价格区域编码
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getFinanceAccountID())//38-计费账号
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getMessageNumber())
		.append(FixedConstant.LOG_SEPARATOR)
		.append(businessRouteValue.getMessagePrice())//40-账号计费单价
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
		.append(businessRouteValue.getConsumeType())//46-扣费类型
		.append(System.getProperty("line.separator"));
		PorxyBusinessLogManager.getInstance().process(sb.toString(),businessRouteValue.getChannelID(),businessRouteValue.getRouteLabel());
	}
	
}


