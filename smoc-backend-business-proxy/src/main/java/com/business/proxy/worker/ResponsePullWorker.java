 /**
 * @desc
 * 从通道表中按照优先级及时间先后获取数据，每次按照通道的速率进行获取，存入到队列中
 */
package com.business.proxy.worker;

import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.lang3.StringUtils;

import com.base.common.cache.CacheBaseService;
import com.base.common.constant.FixedConstant;
import com.base.common.constant.InsideStatusCodeConstant;
import com.base.common.log.PorxyBusinessLogManager;
import com.base.common.manager.BusinessDataManager;
import com.base.common.util.DateUtil;
import com.base.common.util.MessageContentUtil;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperCacheWorker;
import com.business.proxy.manager.ChannelSRCIDManager;

public class ResponsePullWorker extends SuperCacheWorker{
	
	private ThreadPoolExecutor threadPoolExecutor;

	public ResponsePullWorker(ThreadPoolExecutor threadPoolExecutor) {
		super();
		this.threadPoolExecutor = threadPoolExecutor;
	}

	@Override
	protected void doRun() throws Exception {
		final BusinessRouteValue businessRouteValue = CacheBaseService.getResponseToMiddlewareCache();
		if(businessRouteValue != null){
			
			ChannelSRCIDManager.getInstance().add(businessRouteValue.getChannelSRCID());
		
			if(InsideStatusCodeConstant.UNKNOWN_CODE.equals(businessRouteValue.getNextNodeCode())) {
				
				logger.info(
						new StringBuilder().append("提交无响应")
						.append("{}accountID={}")
						.append("{}phoneNumber={}")
						.append("{}businessMessageID={}")
						.append("{}channelID={}")
						.append("{}channelTotal={}")
						.append("{}channelIndex={}")
						.toString(),
						FixedConstant.SPLICER,businessRouteValue.getAccountID(),
						FixedConstant.SPLICER,businessRouteValue.getPhoneNumber(),
						FixedConstant.SPLICER,businessRouteValue.getBusinessMessageID(),
						FixedConstant.SPLICER,businessRouteValue.getChannelID(),
						FixedConstant.SPLICER,businessRouteValue.getChannelTotal(),
						FixedConstant.SPLICER,businessRouteValue.getChannelIndex()
						);

				//TODO 未知部分暂不返回状态报告，后续通过 未匹配提交信息的状态报告 和 这部分记录进行匹配，尽量减少未知部分
			}else{
				logger.info(
						new StringBuilder().append("提交响应")
						.append("{}accountID={}")
						.append("{}phoneNumber={}")
						.append("{}businessMessageID={}")
						.append("{}channelID={}")
						.append("{}channelTotal={}")
						.append("{}channelIndex={}")
						.append("{}channelMessageID={}")
						.append("{}successCode={}")
						.append("{}responseCode={}")
						.toString(),
						FixedConstant.SPLICER,businessRouteValue.getAccountID(),
						FixedConstant.SPLICER,businessRouteValue.getPhoneNumber(),
						FixedConstant.SPLICER,businessRouteValue.getBusinessMessageID(),
						FixedConstant.SPLICER,businessRouteValue.getChannelID(),
						FixedConstant.SPLICER,businessRouteValue.getChannelTotal(),
						FixedConstant.SPLICER,businessRouteValue.getChannelIndex(),
						FixedConstant.SPLICER,businessRouteValue.getChannelMessageID(),
						FixedConstant.SPLICER,businessRouteValue.getNextNodeCode(),
						FixedConstant.SPLICER,businessRouteValue.getNextNodeErrorCode()
						);
				
				//提交成功
				if(InsideStatusCodeConstant.SUCCESS_CODE.equals(businessRouteValue.getNextNodeCode())) {
					//存入缓存  用于匹配状态报告
					CacheBaseService.saveBusinessRouteValueToMiddlewareCache(businessRouteValue);
					
					threadPoolExecutor.execute(new Runnable() {
						
						@Override
						public void run() {
							doReponseSuccess(businessRouteValue);	
						}
					});
					
		
				}//提交失败
				else if(InsideStatusCodeConstant.FAIL_CODE.equals(businessRouteValue.getNextNodeCode())) {
				
					threadPoolExecutor.execute(new Runnable() {
						
						@Override
						public void run() {
							doReponseFail(businessRouteValue);	
						}
					});
					
				}			
			}
			doLog(businessRouteValue);
		}else{
			sleep(BusinessDataManager.getInstance().getReportRedisPopIntervalTime());
		}
	}
	
	/**
	 * 处理响应成功
	 * @param businessRouteValue
	 */
	private void doReponseSuccess(BusinessRouteValue businessRouteValue){
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
						businessRouteValue.getAccountMessageIDs().split(FixedConstant.SPLICER).length);
				
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
	}
	
	/**
	 * 处理响应失败
	 * @param businessRouteValue
	 */
	private void doReponseFail(BusinessRouteValue businessRouteValue){
		//当提交失败时,也保存key和value，channelMessageID为空时，通过uuid赋值
		String channelMessageID = businessRouteValue.getChannelMessageID();
		if(StringUtils.isEmpty(channelMessageID)){
			channelMessageID = UUID.randomUUID().toString();
		}
		//存入缓存  用于匹配状态报告
		CacheBaseService.saveBusinessRouteValueToMiddlewareCache(businessRouteValue);
		
		//模拟通道返回状态报告
			
		BusinessRouteValue businessRouteValueReport = new BusinessRouteValue();
		
		businessRouteValueReport.setChannelID(businessRouteValue.getChannelID());
		businessRouteValueReport.setStatusCode(businessRouteValue.getNextNodeErrorCode());
		businessRouteValueReport.setSubStatusCode("");
		businessRouteValueReport.setStatusCodeSource(FixedConstant.StatusReportSource.RESPONSE.name());
		businessRouteValueReport.setSuccessCode(InsideStatusCodeConstant.FAIL_CODE);
		businessRouteValueReport.setChannelReportSRCID(businessRouteValue.getChannelSubmitSRCID());
		businessRouteValueReport.setChannelMessageID(channelMessageID);
		businessRouteValueReport.setPhoneNumber(businessRouteValue.getPhoneNumber());
		businessRouteValueReport.setChannelReportTime(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI));
		
		CacheBaseService.saveReportToMiddlewareCache(businessRouteValueReport);
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


