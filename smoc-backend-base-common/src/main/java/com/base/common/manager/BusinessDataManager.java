/**
 * @desc
 * 
 */
package com.base.common.manager;

import java.net.InetAddress;
import org.apache.commons.lang3.StringUtils;

import com.base.common.constant.FixedConstant;
import com.base.common.log.CategoryLog;



public class BusinessDataManager {
	
	private static BusinessDataManager manager = new BusinessDataManager();
	
	
	private BusinessDataManager(){
		
	}
	
	public static BusinessDataManager getInstance(){
		return manager;
	}

	/**
	 * 获取业务参数值的通用方法,当没有获取指定账号或通道时则默认为SYSTEM
	 * @param businessDataCategory
	 * @param businessDataItem
	 * @param businessID
	 * @return
	 */
	private String getBusinessParamValue(BusinessDataCategory businessDataCategory,BusinessDataItem businessDataItem,String businessID){
		if(businessID == null){
			businessID = "SYSTEM";
		}
		return ExtendParameterManager.getInstance().getParameterValue(businessDataCategory.getBusinessDataCategoryName(), businessID, businessDataItem.getBusinessDataItemName());
	}
	
	public int getMaxSmsTextLength(){
		String result = getBusinessParamValue(BusinessDataCategory.SYSTEM_PARAM,BusinessDataItem.MAX_SMS_TEXT_LENGTH,null);
		try {
			return Integer.parseInt(result);
		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(),e);
		}	
		
		return 4000;
	}
	
	/**
	 *client session监控间隔时间
	 * @return
	 */
	public long getSessionMonitorIntervalTime(){
		String result = getBusinessParamValue(BusinessDataCategory.SYSTEM_PARAM,BusinessDataItem.SESSION_MONITOR_INTERVAL_TIME,null);
		try {
			return Long.parseLong(result);
		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(),e);
		}
		
		return FixedConstant.COMMON_MONITOR_INTERVAL_TIME;
	}
	
	/**
	 * 数据保存间隔时间
	 * @return
	 */
	public long getMessageSaveIntervalTime(){
		String result = getBusinessParamValue(BusinessDataCategory.SYSTEM_PARAM,BusinessDataItem.MESSAGE_SAVE_INTERVAL_TIME,null);
		try {
			return Long.parseLong(result);
		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(),e);
		}
		
		return FixedConstant.COMMON_INTERVAL_TIME;
	}
	
	/**
	 * 数据加载间隔时间
	 * @return
	 */
	public long getMessageLoadIntervalTime(){
		String result = getBusinessParamValue(BusinessDataCategory.SYSTEM_PARAM,BusinessDataItem.MESSAGE_LOAD_INTERVAL_TIME,null);
		try {
			return Long.parseLong(result);
		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(),e);
		}
		
		return FixedConstant.COMMON_INTERVAL_TIME;
	}
	
	/**
	 * 加载通道临时上行数据的间隔时间
	 * @return
	 */
	public long getChannelMOLoadIntervalTime(){
		String result = getBusinessParamValue(BusinessDataCategory.SYSTEM_PARAM,BusinessDataItem.CHANNEL_MO_LOAD_INTERVAL_TIME,null);
		try {
			return Long.parseLong(result);
		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(),e);
		}
		
		return FixedConstant.COMMON_INTERVAL_TIME;
	}
	
	/**
	 * 加载账号财务信息的间隔时间
	 * @return
	 */
	public long getAccountFinanceLoadIntervalTime(){
		String result = getBusinessParamValue(BusinessDataCategory.SYSTEM_PARAM,BusinessDataItem.ACCOUNT_FINANCE_LOAD_INTERVAL_TIME,null);
		try {
			return Long.parseLong(result);
		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(),e);
		}
		
		return FixedConstant.COMMON_EFFECTIVE_TIME;
	}
	
	/**
	 * 单次数据加载最大条数
	 * @return
	 */
	public int getMessageLoadMaxNumber(){
		String result = getBusinessParamValue(BusinessDataCategory.SYSTEM_PARAM,BusinessDataItem.MESSAGE_LOAD_MAX_NUMBER,null);
		try {
			return Integer.parseInt(result);
		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(),e);
		}
		
		return 1000;
	}
	
	/**
	 * 接入平台状态报告拉取为空时的间隔时间
	 */
	public long getReportRedisPopIntervalTime(){
		String result = getBusinessParamValue(BusinessDataCategory.SYSTEM_PARAM,BusinessDataItem.REPORT_REDIS_POP_INTERVAL_TIME,null);
		try {
			return Long.parseLong(result);
		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(),e);
		}	
		return FixedConstant.COMMON_INTERVAL_TIME;
	}
	
	/**
	 * redis拉取数据为空时的间隔时间
	 */
	public long getRedisPopIntervalTime(){
		String result = getBusinessParamValue(BusinessDataCategory.SYSTEM_PARAM,BusinessDataItem.REDIS_POP_INTERVAL_TIME,null);
		try {
			return Long.parseLong(result);
		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(),e);
		}	
		return FixedConstant.COMMON_INTERVAL_TIME;
	}
	
	/**
	 * redis分布锁过期时间 单位:秒
	 */
	public int getRedisLockExpirationTime(){
		String result = getBusinessParamValue(BusinessDataCategory.SYSTEM_PARAM,BusinessDataItem.REDIS_LOCK_EXPIRATION_TIME,null);
		try {
			return Integer.parseInt(result);
		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(),e);
		}	
		return 60;
	}
	
	/**
	 * 指定账号状态报告推送间隔时间
	 */
	private long getReportPushIntervalTime(){
		String result = getBusinessParamValue(BusinessDataCategory.SYSTEM_PARAM,BusinessDataItem.REPORT_PUSH_INTERVAL_TIME,null);
		try {
			return Long.parseLong(result);
		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(),e);
		}	
		return FixedConstant.COMMON_INTERVAL_TIME;
	}
	
	/**
	 * 获取日志的根路径
	 * @return
	 */
	public String getLogBasePath(){
		String result = getBusinessParamValue(BusinessDataCategory.SYSTEM_PARAM,BusinessDataItem.LOG_BASE_PATH,null);
		if(result == null){
			result = "/tmp/";
		}
		return result;
	}
	
	/**
	 * 获取日志的根路径
	 * @return
	 */
	private String getHostIDType(){
		String result = getBusinessParamValue(BusinessDataCategory.SYSTEM_PARAM,BusinessDataItem.HOST_ID_TYPE,null);
		if(result == null){
			result = "IP";
		}
		return result;
	}
	
	/**
	 * 获取主机标识
	 * @return
	 */
	public String getHostID(){
		if("NAME".equals(getHostIDType())){
			try {
				return InetAddress.getLocalHost().getHostName();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			try {
				return InetAddress.getLocalHost().getHostAddress();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// 模拟一个IP地址
		return new StringBuffer().append(1 + (int) (Math.random() * 250))
				.append(".").append(1 + (int) (Math.random() * 250))
				.append(".").append(1 + (int) (Math.random() * 250))
				.append(".").append(1 + (int) (Math.random() * 250)).toString();
	}
	
	/**
	 * 获取状态报告存储到REDIS的接入协议
	 * @return
	 */
	public String getReportStoreToRedisProtocol(){
		String result = getBusinessParamValue(BusinessDataCategory.SYSTEM_PARAM,BusinessDataItem.REPORT_STORE_TO_REDIS_PROTOCOL,null);
		if(result == null){
			result = "";
		}
		return result;
	}
	
	/**
	 * 获取状态报告存储到DATABASE的接入协议
	 * @return
	 */
	public String getReportStoreToDatabaseProtocol(){
		String result = getBusinessParamValue(BusinessDataCategory.SYSTEM_PARAM,BusinessDataItem.REPORT_STORE_TO_DATABASE_PROTOCOL,null);
		if(result == null){
			result = "";
		}
		return result;
	}
	
	/**
	 * REDIS保存响应信息的过期时间
	 * @return
	 */
	public int getResponseStoreToRedisExpirationTime(){
		String result = getBusinessParamValue(BusinessDataCategory.SYSTEM_PARAM,BusinessDataItem.RESPONSE_STORE_TO_REDIS_EXPIRATION_TIME,null);
		try {
			return Integer.parseInt(result);
		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(),e);
		}
		return 3600*24*72;
	}
	
	/**
	 * 上行信息匹配有效期
	 * @return
	 */
	public int getChannelMOMatchExpirationTime(){
		String result = getBusinessParamValue(BusinessDataCategory.SYSTEM_PARAM,BusinessDataItem.CHANNEL_MO_MATCH_EXPIRATION_TIME,null);
		try {
			return Integer.parseInt(result);
		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(),e);
		}
		return 60*2;
	}
	
	/**
	 * 黑词过滤
	 */
	public boolean isBlackWordFiltering(String accountID){
		String result = getBusinessParamValue(BusinessDataCategory.BUSINESS_ACCOUNT_FILTER,BusinessDataItem.BLACK_WORD_FILTERING,accountID);
		return "1".equals(result);
	}

	/**
	 * 审核词过滤
	 */
	public boolean isAuditWordFiltering(String accountID){
		String result = getBusinessParamValue(BusinessDataCategory.BUSINESS_ACCOUNT_FILTER,BusinessDataItem.AUDIT_WORD_FILTERING,accountID);
		return "1".equals(result);
	}
	/**
	 * 相同手机号频次限制
	 */
	public boolean isSendFrequencyLimit(String accountID){
		String result = getBusinessParamValue(BusinessDataCategory.BUSINESS_ACCOUNT_FILTER,BusinessDataItem.SEND_FREQUENCY_LIMIT,accountID);
		return "1".equals(result);
	}
	/**
	 * 黑名单层级过滤
	 */
	public String getBlackListLevelFiltering(String accountID){
		String result = getBusinessParamValue(BusinessDataCategory.BUSINESS_ACCOUNT_FILTER,BusinessDataItem.BLACK_LIST_LEVEL_FILTERING,accountID);
		return result;
	}
	/**
	 * 日限量方式
	 */
	public int getSendLimitStyleDaily(String accountID){
		String result = getBusinessParamValue(BusinessDataCategory.BUSINESS_ACCOUNT_FILTER,BusinessDataItem.SEND_LIMIT_STYLE_DAILY,accountID);
		try {
			return Integer.parseInt(result);
		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(),e);
		}	
		return 0;
	}
	
	/**
	 * 移动日限量
	 */
	public int getSendLimitNumberDailyCMCC(String accountID){
		String result = getBusinessParamValue(BusinessDataCategory.BUSINESS_ACCOUNT_FILTER,BusinessDataItem.SEND_LIMIT_NUMBER_DAILY_CMCC,accountID);
		try {
			return Integer.parseInt(result);
		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(),e);
		}	
		return 0;
	}
	/**
	 * 联通日限量
	 */
	public int getSendLimitNumberDailyUNIC(String accountID){
		String result = getBusinessParamValue(BusinessDataCategory.BUSINESS_ACCOUNT_FILTER,BusinessDataItem.SEND_LIMIT_NUMBER_DAILY_UNIC,accountID);
		try {
			return Integer.parseInt(result);
		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(),e);
		}	
		return 0;
	}
	/**
	 * 电信日限量
	 */
	public int getSendLimitNumberDailyTELC(String accountID){
		String result = getBusinessParamValue(BusinessDataCategory.BUSINESS_ACCOUNT_FILTER,BusinessDataItem.SEND_LIMIT_NUMBER_DAILY_TELC,accountID);
		try {
			return Integer.parseInt(result);
		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(),e);
		}	
		return 0;
	}
	/**
	 * 发送时间限制
	 */
	public String getSendTimeLimit(String accountID){
		String result = getBusinessParamValue(BusinessDataCategory.BUSINESS_ACCOUNT_FILTER,BusinessDataItem.SEND_TIME_LIMIT,accountID);
		return result;
	}
	/**
	 * 移动屏蔽省份
	 */
	public String getCMCCMaskProvince(String accountID){
		String result = getBusinessParamValue(BusinessDataCategory.BUSINESS_ACCOUNT_FILTER,BusinessDataItem.CMCC_MASK_PROVINCE,accountID);
		return result;
	}
	/**
	 * 联通屏蔽省份
	 */
	public String getUNICMaskProvince(String accountID){
		String result = getBusinessParamValue(BusinessDataCategory.BUSINESS_ACCOUNT_FILTER,BusinessDataItem.UNIC_MASK_PROVINCE,accountID);
		return result;
	}
	/**
	 * 电信屏蔽省份
	 */
	public String getTELCMaskProvince(String accountID){
		String result = getBusinessParamValue(BusinessDataCategory.BUSINESS_ACCOUNT_FILTER,BusinessDataItem.TELC_MASK_PROVINCE,accountID);
		return result;
	}
	
	/**
	 * 获取通道扩展参数
	 * @param channelID
	 * @return
	 */
	public String getExtendInterfaceParam(String channelID){
		String result = getBusinessParamValue(BusinessDataCategory.CHANNEL,BusinessDataItem.EXTEND_INTERFACE_PARAM,channelID);
		return result;
	}
	
	/**
	 * 获取通道每日成功量(仅用于通道组中通道量满时权重为0)
	 * 返回为0时，则代表不限量
	 * @param channelID
	 * @return
	 */
	public int getDaySuccessNumber(String channelID){
		String result = getBusinessParamValue(BusinessDataCategory.CHANNEL,BusinessDataItem.DAY_SUCCESS_NUMBER,channelID);
		if(StringUtils.isEmpty(result)){
			return 0;
		}
		try {
			return Integer.parseInt(result);
		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(),e);
		}	
		return 0;
	}
	
	/**
	 * 获取通道每月成功量(仅用于通道组中通道量满时权重为0)
	 * 返回为0时，则代表不限量
	 * @param channelID
	 * @return
	 */
	public int getMonthSuccessNumber(String channelID){
		String result = getBusinessParamValue(BusinessDataCategory.CHANNEL,BusinessDataItem.MONTH_SUCCESS_NUMBER,channelID);
		if(StringUtils.isEmpty(result)){
			return 0;
		}
		try {
			return Integer.parseInt(result);
		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(),e);
		}	
		return 0;
	}
	
	/**
	 * 指定账号状态报告推送间隔时间
	 */
	public long getAccountReportPushIntervalTime(String accountID){
		String result = getBusinessParamValue(BusinessDataCategory.BUSINESS_ACCOUNT,BusinessDataItem.ACCOUNT_REPORT_PUSH_INTERVAL_TIME,accountID);
		try {
			return Long.parseLong(result);
		} catch (Exception e) {
			CategoryLog.commonLogger.error(e.getMessage(),e);
		}	
		return getReportPushIntervalTime();
	}
	
	/**
	 * 	指定账号的扣费方式
	 * @param accountID
	 * @return
	 */
	public String getAccountConsumeType(String accountID) {
		String consumeType = getBusinessParamValue(BusinessDataCategory.BUSINESS_ACCOUNT,BusinessDataItem.ACCOUNT_CONSUME_TYPE,accountID);
		if (String.valueOf(FixedConstant.AccountConsumeType.REPORT.ordinal()).equals(consumeType)) {
			return consumeType;
		}
		return String.valueOf(FixedConstant.AccountConsumeType.SUBMIT.ordinal());
	}
	
	/**
	 * 根据账号获取状态码转换信息
	 * @param accountID
	 * @return
	 */
	public String getAccountStatusCodeConversion(String accountID,String statusCode) {
		String statusCodeConversions = getBusinessParamValue(BusinessDataCategory.BUSINESS_ACCOUNT,BusinessDataItem.STATUS_CODE_CONVERSION,accountID);
		if(StringUtils.isNotEmpty(statusCodeConversions) && statusCodeConversions.contains(statusCode)){
			String[] statusCodeConversionArray = statusCodeConversions.split(FixedConstant.DATABASE_SEPARATOR);
			for(String statusCodeConversion : statusCodeConversionArray){
				String[] statusCodeArray = statusCodeConversion.split("=");
				if(statusCodeArray.length == 2){
					if(statusCode.equalsIgnoreCase(statusCodeArray[0])){
						return statusCodeArray[1];
					}
				}
			}
		}
		return null;
	}
	
	private static class BusinessDataCategory {
		
		private String businessDataCategoryName;
		
		public BusinessDataCategory(String businessDataCategoryName) {
			super();
			this.businessDataCategoryName = businessDataCategoryName;
		}
		public String getBusinessDataCategoryName() {
			return businessDataCategoryName;
		}
		public static BusinessDataCategory CHANNEL = new BusinessDataCategory(FixedConstant.BusinessDataCategory.CHANNEL.toString());
		//public static BusinessDataCategory CHANNEL_FILTER = new BusinessDataCategory(FixedConstant.BusinessDataCategory.CHANNEL_FILTER.toString());
		public static BusinessDataCategory BUSINESS_ACCOUNT = new BusinessDataCategory(FixedConstant.BusinessDataCategory.BUSINESS_ACCOUNT.toString());
		public static BusinessDataCategory BUSINESS_ACCOUNT_FILTER = new BusinessDataCategory(FixedConstant.BusinessDataCategory.BUSINESS_ACCOUNT_FILTER.toString());
		public static BusinessDataCategory SYSTEM_PARAM = new BusinessDataCategory(FixedConstant.BusinessDataCategory.SYSTEM_PARAM.toString());
	
	}
	
	private static class BusinessDataItem {
		
		private String businessDataItemName;
		
		public BusinessDataItem(String businessDataItemName) {
			this.businessDataItemName = businessDataItemName;
		}
		public String getBusinessDataItemName() {
			return businessDataItemName;
		}
		
		//系统级参数数据项
//		public static BusinessDataItem WHITELIST_EFFECTIVE_TIME = new BusinessDataItem(FixedConstant.SystemItem.WHITELIST_EFFECTIVE_TIME.toString());
//		public static BusinessDataItem BLACKLIST_EFFECTIVE_TIME = new BusinessDataItem(FixedConstant.SystemItem.BLACKLIST_EFFECTIVE_TIME.toString());
//		public static BusinessDataItem COMMON_EFFECTIVE_TIME = new BusinessDataItem(FixedConstant.SystemItem.COMMON_EFFECTIVE_TIME.toString());

		public static BusinessDataItem MESSAGE_SAVE_INTERVAL_TIME = new BusinessDataItem(FixedConstant.SystemItem.MESSAGE_SAVE_INTERVAL_TIME.toString());
		public static BusinessDataItem MESSAGE_LOAD_INTERVAL_TIME = new BusinessDataItem(FixedConstant.SystemItem.MESSAGE_LOAD_INTERVAL_TIME.toString());
		public static BusinessDataItem MESSAGE_LOAD_MAX_NUMBER = new BusinessDataItem(FixedConstant.SystemItem.MESSAGE_LOAD_MAX_NUMBER.toString());
		public static BusinessDataItem CHANNEL_MO_LOAD_INTERVAL_TIME = new BusinessDataItem(FixedConstant.SystemItem.CHANNEL_MO_LOAD_INTERVAL_TIME.toString());
		public static BusinessDataItem ACCOUNT_FINANCE_LOAD_INTERVAL_TIME = new BusinessDataItem(FixedConstant.SystemItem.ACCOUNT_FINANCE_LOAD_INTERVAL_TIME.toString());
		
		public static BusinessDataItem SESSION_MONITOR_INTERVAL_TIME = new BusinessDataItem(FixedConstant.SystemItem.SESSION_MONITOR_INTERVAL_TIME.toString());
		public static BusinessDataItem REPORT_REDIS_POP_INTERVAL_TIME = new BusinessDataItem(FixedConstant.SystemItem.REPORT_REDIS_POP_INTERVAL_TIME.toString());
		public static BusinessDataItem REDIS_POP_INTERVAL_TIME = new BusinessDataItem(FixedConstant.SystemItem.REDIS_POP_INTERVAL_TIME.toString());
		public static BusinessDataItem REPORT_PUSH_INTERVAL_TIME = new BusinessDataItem(FixedConstant.SystemItem.REPORT_PUSH_INTERVAL_TIME.toString());

		public static BusinessDataItem MAX_SMS_TEXT_LENGTH = new BusinessDataItem(FixedConstant.SystemItem.MAX_SMS_TEXT_LENGTH.toString());
		public static BusinessDataItem LOG_BASE_PATH = new BusinessDataItem(FixedConstant.SystemItem.LOG_BASE_PATH.toString());
		public static BusinessDataItem HOST_ID_TYPE = new BusinessDataItem(FixedConstant.SystemItem.HOST_ID_TYPE.toString());

		public static BusinessDataItem REPORT_STORE_TO_REDIS_PROTOCOL = new BusinessDataItem(FixedConstant.SystemItem.REPORT_STORE_TO_REDIS_PROTOCOL.toString());
		public static BusinessDataItem REPORT_STORE_TO_DATABASE_PROTOCOL = new BusinessDataItem(FixedConstant.SystemItem.REPORT_STORE_TO_DATABASE_PROTOCOL.toString());
		public static BusinessDataItem RESPONSE_STORE_TO_REDIS_EXPIRATION_TIME = new BusinessDataItem(FixedConstant.SystemItem.RESPONSE_STORE_TO_REDIS_EXPIRATION_TIME.toString());
		public static BusinessDataItem CHANNEL_MO_MATCH_EXPIRATION_TIME = new BusinessDataItem(FixedConstant.SystemItem.CHANNEL_MO_MATCH_EXPIRATION_TIME.toString());
		public static BusinessDataItem REDIS_LOCK_EXPIRATION_TIME = new BusinessDataItem(FixedConstant.SystemItem.REDIS_LOCK_EXPIRATION_TIME.toString());
		
		
		//账号过滤数据项
		public static BusinessDataItem BLACK_WORD_FILTERING = new BusinessDataItem(FixedConstant.AccountFilterItem.COMMON_BLACK_WORD_FILTERING.toString());
		public static BusinessDataItem AUDIT_WORD_FILTERING = new BusinessDataItem(FixedConstant.AccountFilterItem.COMMON_AUDIT_WORD_FILTERING.toString());
		public static BusinessDataItem SEND_FREQUENCY_LIMIT = new BusinessDataItem(FixedConstant.AccountFilterItem.COMMON_SEND_FREQUENCY_LIMIT.toString());
		public static BusinessDataItem BLACK_LIST_LEVEL_FILTERING = new BusinessDataItem(FixedConstant.AccountFilterItem.COMMON_BLACK_LIST_LEVEL_FILTERING.toString());
		public static BusinessDataItem SEND_LIMIT_STYLE_DAILY = new BusinessDataItem(FixedConstant.AccountFilterItem.COMMON_SEND_LIMIT_STYLE_DAILY.toString());
		public static BusinessDataItem SEND_LIMIT_NUMBER_DAILY_CMCC = new BusinessDataItem(FixedConstant.AccountFilterItem.COMMON_SEND_LIMIT_NUMBER_DAILY_CMCC.toString());
		public static BusinessDataItem SEND_LIMIT_NUMBER_DAILY_UNIC = new BusinessDataItem(FixedConstant.AccountFilterItem.COMMON_SEND_LIMIT_NUMBER_DAILY_UNIC.toString());
		public static BusinessDataItem SEND_LIMIT_NUMBER_DAILY_TELC = new BusinessDataItem(FixedConstant.AccountFilterItem.COMMON_SEND_LIMIT_NUMBER_DAILY_TELC.toString());
		public static BusinessDataItem SEND_TIME_LIMIT = new BusinessDataItem(FixedConstant.AccountFilterItem.COMMON_SEND_TIME_LIMIT.toString());
		public static BusinessDataItem CMCC_MASK_PROVINCE = new BusinessDataItem(FixedConstant.AccountFilterItem.COMMON_CMCC_MASK_PROVINCE.toString());
		public static BusinessDataItem UNIC_MASK_PROVINCE = new BusinessDataItem(FixedConstant.AccountFilterItem.COMMON_UNIC_MASK_PROVINCE.toString());
		public static BusinessDataItem TELC_MASK_PROVINCE = new BusinessDataItem(FixedConstant.AccountFilterItem.COMMON_TELC_MASK_PROVINCE.toString());
		
		//业务账号的扩展项
		public static BusinessDataItem ACCOUNT_REPORT_PUSH_INTERVAL_TIME = new BusinessDataItem(FixedConstant.AccountExtendItem.ACCOUNT_REPORT_PUSH_INTERVAL_TIME.toString());
		public static BusinessDataItem ACCOUNT_CONSUME_TYPE = new BusinessDataItem(FixedConstant.AccountExtendItem.ACCOUNT_CONSUME_TYPE.toString());
		public static BusinessDataItem STATUS_CODE_CONVERSION = new BusinessDataItem(FixedConstant.AccountExtendItem.STATUS_CODE_CONVERSION.toString());

		
		//通道扩展参数
		public static BusinessDataItem DAY_SUCCESS_NUMBER = new BusinessDataItem(FixedConstant.ChannelExtendItem.DAY_SUCCESS_NUMBER.toString());
		public static BusinessDataItem MONTH_SUCCESS_NUMBER = new BusinessDataItem(FixedConstant.ChannelExtendItem.MONTH_SUCCESS_NUMBER.toString());
		public static BusinessDataItem EXTEND_INTERFACE_PARAM = new BusinessDataItem(FixedConstant.ChannelExtendItem.EXTEND_INTERFACE_PARAM.toString());

	}
	
}


