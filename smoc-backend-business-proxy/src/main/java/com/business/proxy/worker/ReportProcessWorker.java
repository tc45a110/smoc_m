 /**
 * @desc
 * 从通道表中按照优先级及时间先后获取数据，每次按照通道的速率进行获取，存入到队列中
 */
package com.business.proxy.worker;


import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.base.common.cache.CacheBaseService;
import com.base.common.constant.FixedConstant;
import com.base.common.constant.InsideStatusCodeConstant;
import com.base.common.log.PorxyBusinessLogManager;
import com.base.common.manager.BusinessDataManager;
import com.base.common.manager.ChannelInfoManager;
import com.base.common.manager.ChannelPriceManager;
import com.base.common.manager.FailRepairManager;
import com.base.common.manager.ResourceManager;
import com.base.common.util.Commons;
import com.base.common.util.DateUtil;
import com.base.common.util.MessageContentUtil;
import com.base.common.vo.BusinessRouteValue;
import com.base.common.worker.SuperQueueWorker;
import com.business.proxy.manager.ChannelRepairWorkerManager;
import com.business.proxy.manager.LongChannelRepairWorkerManager;


public class ReportProcessWorker extends SuperQueueWorker<BusinessRouteValue>{

	private ThreadPoolExecutor threadPoolExecutor;
	
	public ReportProcessWorker(ThreadPoolExecutor threadPoolExecutor) {
		super();
		this.threadPoolExecutor = threadPoolExecutor;
	}
	
	@Override
	protected void doRun() throws Exception {
		BusinessRouteValue businessRouteValueReport = take();
		logger.info(
				new StringBuilder().append("处理状态报告")
				.append("{}channelID={}")
				.append("{}phoneNumber={}")
				.append("{}channelMessageID={}")
				.append("{}channelReportSRCID={}")
				.append("{}statusCode={}")
				.append("{}subStatusCode={}")
				.append("{}successCode={}")
				.append("{}queueSize={}")
				.toString(),
				FixedConstant.SPLICER,businessRouteValueReport.getChannelID(),
				FixedConstant.SPLICER,businessRouteValueReport.getPhoneNumber(),
				FixedConstant.SPLICER,businessRouteValueReport.getChannelMessageID(),
				FixedConstant.SPLICER,businessRouteValueReport.getChannelReportSRCID(),
				FixedConstant.SPLICER,businessRouteValueReport.getStatusCode(),
				FixedConstant.SPLICER,businessRouteValueReport.getSubStatusCode(),
				FixedConstant.SPLICER,businessRouteValueReport.getSuccessCode(),
				FixedConstant.SPLICER,size()
				);
		//获取中间件缓存中的提交信息
		BusinessRouteValue businessRouteValue = CacheBaseService.getBusinessRouteValueFromMiddlewareCache(businessRouteValueReport);
		//匹配到提交信息
		if(businessRouteValue != null){	
			// 处理状态报告
			processReport(businessRouteValue,businessRouteValueReport);
		}else{
			threadPoolExecutor.execute(new Runnable() {
				long createTime = System.currentTimeMillis();
				
				@Override
				public void run() {
					try {
						long waitTime = ResourceManager.getInstance().getLongValue("rematch.report.sleep.time");
						if (waitTime == 0) {
							waitTime = 500;
						}
						long intervalTime = waitTime - (System.currentTimeMillis() - createTime);
						if (intervalTime > 0) {
							Thread.sleep(intervalTime);
						}
						BusinessRouteValue businessRouteValue = CacheBaseService
								.getBusinessRouteValueFromMiddlewareCache(businessRouteValueReport);
						if (businessRouteValue != null) {
							// 处理状态报告
							processReport(businessRouteValue, businessRouteValueReport);
						} else {
							logger.warn(
									new StringBuilder().append("状态报告未匹配到提交信息").append("{}channelID={}")
											.append("{}phoneNumber={}").append("{}channelMessageID={}")
											.append("{}channelReportSRCID={}").append("{}statusCode={}")
											.append("{}subStatusCode={}").append("{}successCode={}").toString(),
									FixedConstant.SPLICER, businessRouteValueReport.getChannelID(),
									FixedConstant.SPLICER, businessRouteValueReport.getPhoneNumber(),
									FixedConstant.SPLICER, businessRouteValueReport.getChannelMessageID(),
									FixedConstant.SPLICER, businessRouteValueReport.getChannelReportSRCID(),
									FixedConstant.SPLICER, businessRouteValueReport.getStatusCode(),
									FixedConstant.SPLICER, businessRouteValueReport.getSubStatusCode(),
									FixedConstant.SPLICER, businessRouteValueReport.getSuccessCode());
						}
					} catch (InterruptedException e) {
						logger.error(e.getMessage(), e);
					}
				}
			});
		}
	}
	
	private void processReport(BusinessRouteValue businessRouteValue,BusinessRouteValue businessRouteValueReport) {
		CacheBaseService.deleteBusinessRouteValueFromMiddlewareCache(businessRouteValueReport);
		businessRouteValue.setBusinessRouteValueReport(businessRouteValueReport);
		businessRouteValue.setRouteLabel(FixedConstant.RouteLable.MR.name());
		doLogs(businessRouteValue);
		
		//维护通道日，月成功量
		if(InsideStatusCodeConstant.SUCCESS_CODE.equals(businessRouteValue.getSuccessCode()) ){
			//推送状态报告到接入业务层
			doReport(businessRouteValue);
			//状态报告成功维护通道限量和账号限量
			doReportSuccess(businessRouteValue);
		}else {
			//判断是否进失败补发 补发完毕则返回true
			if(!isRepair(businessRouteValue)) {
				//推送状态报告到接入业务层
				doReport(businessRouteValue);
			}
		}
		logger.info(
				new StringBuilder().append("维护状态报告业务逻辑").append("{}accountID={}").append("{}phoneNumber={}")
						.append("{}businessMessageID={}").append("{}channelMessageID={}")
						.append("{}statusCode={}").append("{}statusCodeSource={}")
						.toString(),
				FixedConstant.SPLICER, businessRouteValue.getAccountID(), 
				FixedConstant.SPLICER, businessRouteValue.getPhoneNumber(),
				FixedConstant.SPLICER,businessRouteValue.getBusinessMessageID(),
				FixedConstant.SPLICER,businessRouteValue.getChannelMessageID(),
				FixedConstant.SPLICER, businessRouteValue.getStatusCode(),
				FixedConstant.SPLICER, businessRouteValue.getStatusCodeSource());
	}
	
	/**
	 *   失败补发判断 
	 * @param businessRouteValue
	 * @return 获取补发通道并补发返回 true 获取补发通道失败 false
	 */
	private boolean isRepair(BusinessRouteValue businessRouteValue) {
		try {
			//失败补发
			String accountID = businessRouteValue.getAccountID();
			String businessCarrier = businessRouteValue.getBusinessCarrier();
			String statusCode = businessRouteValue.getStatusCode();
			String channelID = businessRouteValue.getChannelID();
			
			boolean isRepairFlag = false;
			boolean isFirstRepair = false;
			
			int intervalTime = DateUtil.getIntervalTime(businessRouteValue.getAccountSubmitTime(), DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_STANDARD_MILLI));
			ArrayList<String> channelRepairList;
			int repairTime;
			
			channelRepairList = businessRouteValue.getChannelRepairIDs();
			repairTime = businessRouteValue.getRepairTime();
			if(channelRepairList == null) {
				//获取补发通道组
				channelRepairList = FailRepairManager.getInstance().getChannelRepairID(accountID, businessCarrier, statusCode, channelID);
				//有效补发时间
				repairTime = FailRepairManager.getInstance().getRepairTime(accountID, businessCarrier, channelID) * 60;
				businessRouteValue.setChannelRepairIDs(channelRepairList);
				businessRouteValue.setRepairTime(repairTime);
				isFirstRepair = true;
			}
			
			if(CollectionUtils.isNotEmpty(channelRepairList) && repairTime > intervalTime) {
				//获取补发通道
				String channelRepairID = getChannelRepairID(channelRepairList, channelID, isFirstRepair);
				if(channelRepairID != null) {
					//开始补发
					repair(businessRouteValue, channelRepairID);
					isRepairFlag = true;
				}
			}
			logger.info("channelRepairList={},repairTime={},repairStatus={},isRepairFlag={}",channelRepairList,repairTime,FailRepairManager.getInstance().getAccountRepairStatus(accountID, channelID, businessCarrier),isRepairFlag);
			return isRepairFlag;
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return false;
	}
	
	/**
	 * 获取失败补发通道
	 * @param channelRepairList 补发通道组
	 * @param channelID	本次失败通道
	 * @param isFirstRepair 是否为第一次补发
	 * @return 失败补发通道
	 */
	private String getChannelRepairID(ArrayList<String> channelRepairList, String channelID, boolean isFirstRepair) {
		//logger.info("channelRepairList={},channelID={},isFirstRepair={}",channelRepairList,channelID,isFirstRepair);
		int index = 0;
		//初次补发 直接获取通道进行判断
		if(!isFirstRepair) {
			index = channelRepairList.indexOf(channelID);
			if(index == -1 || index == (channelRepairList.size() - 1)) {
				return null;
			}
			index++;
		}
		//根据当前通道索引 获取通道 判断通道状态
		String channeRepairID = channelRepairList.get(index);
		if(FixedConstant.ChannelStatus.NORMAL.name().equals(ChannelInfoManager.getInstance().getChannelStatus(channeRepairID))) {
			return channeRepairID;
		}
		logger.info("通道状态异常:{}",channeRepairID);
		return getChannelRepairID(channelRepairList,channeRepairID,false);
	}
	
	/**
	 * 开始补发
	 * @param businessRouteValue 
	 * @param channelID	补发通道
	 */
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
	
	/**
	 * 回传状态报告
	 * @param businessRouteValue
	 */
	private void doReport(BusinessRouteValue businessRouteValue){
		String [] messageIDsArray = businessRouteValue.getAccountMessageIDs().split(FixedConstant.SPLICER);
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
	
	/**
	 * 处理成功的状态报告，维护成功量
	 * @param businessRouteValue
	 */
	private void doReportSuccess(BusinessRouteValue businessRouteValue){
		String [] messageIDsArray = businessRouteValue.getAccountMessageIDs().split(FixedConstant.SPLICER);
		
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
	}
	
	/**
	 * 记录状态报告日志
	 * @param businessRouteValue
	 */
	private void doLogs(BusinessRouteValue businessRouteValue){
		String [] messageIDsArray = businessRouteValue.getAccountMessageIDs().split(FixedConstant.SPLICER);
		
		for(int i = 0; i < messageIDsArray.length; i++) {
			businessRouteValue.setAccountMessageIDs(messageIDsArray[i]);
			//如果解析的msgID数组大于1    则为提交到平台为多条数据  下发合成为1条  状态报告返回为1条  如：cmpp -- http
			if(messageIDsArray.length > 1) {
				businessRouteValue.setMessageIndex(i+1);
			}
			doLog(businessRouteValue);
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


