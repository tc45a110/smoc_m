package com.base.common.util;

import com.base.common.constant.RedisHashKeyConstant;
import com.base.common.constant.FixedConstant;
import com.base.common.vo.AlarmMessage;

public class CacheNameGeneratorUtil {
	
	/**
	 * 生成携号转网的缓存名称
	 * @param key
	 * @return
	 */
	public static final String generateMNPCacheName(String key) {
		return new StringBuilder()
		.append(FixedConstant.MiddlewareCacheName.MNP.name())
		.append(FixedConstant.REIDS_SPLICER)
		.append(key)
		.toString();
	}
	
	/**
	 * 获取账号价格hash的key
	 * @return
	 */
	public static final String generateAccountPriceCacheName(){
		return new StringBuilder()
		.append(RedisHashKeyConstant.ACCOUNT_PRICE_PREFIX)
		.append(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_DAY))
		.toString();
	}
	
	/**
	 * 获取通道价格hash的key
	 * @return
	 */
	public static final String generateChannelPriceCacheName(){
		return new StringBuilder()
		.append(RedisHashKeyConstant.CHANNEL_PRICE_PREFIX)
		.append(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_DAY))
		.toString();
	}
	
	/**
	 * 账号级接入层状态报告队列
	 * @param key
	 * @return
	 */
	public static final String generateAccessReportCacheName(String key) {
		return new StringBuilder()
		.append(FixedConstant.MiddlewareCacheName.ACCESS_REPORT.name())
		.append(FixedConstant.REIDS_SPLICER)
		.append(key)
		.toString();
	}
	
	/**
	 * 接入层状态报告队列
	 * @return
	 */
	public static final String generateAccessReportCacheName() {
		return new StringBuilder()
		.append(FixedConstant.MiddlewareCacheName.ACCESS_REPORT.name())
		.toString();
	}
	
	/**
	 * 代理层状态报告队列
	 * @return
	 */
	public static final String generateProxyReportCacheName() {
		return new StringBuilder()
		.append(FixedConstant.MiddlewareCacheName.PROXY_REPORT.name())
		.toString();
	}
	
	/**
	 * 生成响应队列名称
	 * @return
	 */
	public static final String generateResponseCacheName() {
		return new StringBuilder()
		.append(FixedConstant.MiddlewareCacheName.RESPONSE.name())
		.toString();
	}
	
	/**
	 * 生成提交队列名称
	 * @param key
	 * @return
	 */
	public static final String generateSubmitCacheName(String key){
		return new StringBuilder()
		.append(FixedConstant.MiddlewareCacheName.SUBMIT.name())
		.append(FixedConstant.REIDS_SPLICER)
		.append(key)
		.toString();
	}
	
	/**
	 * 生成消息唯一的key
	 * @param mobile
	 * @param channelMessageID
	 * @return
	 */
	public static final String generateMessageIDCacheName(String mobile,String channelMessageID){
		return new StringBuilder()
		.append(FixedConstant.MiddlewareCacheName.MESSAGE_ID.name())
		.append(FixedConstant.REIDS_SPLICER)
		.append(mobile)
		.append(FixedConstant.REIDS_SPLICER)
		.append(channelMessageID)
		.toString();
	}
	
	/**
	 * 获取账号运营商日限量key
	 * @return
	 */
	public static final String generateAccountCarrierDailyLimitCacheName(){
		return new StringBuilder()
		.append(RedisHashKeyConstant.ACCOUNT_CARRIER_DAILY_LIMIT_PREFIX)
		.append(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_DAY))
		.toString();
	}
	
	/**
	 * 获取账号流控key
	 * @return
	 */
	public static final String generateAccountSpeedCacheName(){
		return new StringBuilder()
		.append(RedisHashKeyConstant.ACCOUNT_SPEED_PREFIX)
		.append(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_MINUTE))
		.toString();
	}
	
	/**
	 * 获取通道日限量key
	 * @return
	 */
	public static final String generateChannelDailyLimitCacheName(){
		return new StringBuilder()
		.append(RedisHashKeyConstant.CHANNEL_LIMIT_PREFIX)
		.append(DateUtil.getCurDateTime(DateUtil.DATE_FORMAT_COMPACT_DAY))
		.toString();
	}
	
	/**
	 * 获取通道月限量key
	 * @return
	 */
	public static final String generateChannelMonthlyLimitCacheName(){
		return new StringBuilder()
		.append(RedisHashKeyConstant.CHANNEL_LIMIT_PREFIX)
		.append(DateUtil.getCurDateTime(DateUtil.DATE_YEAR_MONTH))
		.toString();
	}
	
	/**
	 * 	状态报告redis分布锁名
	 * @param accountID
	 * @return
	 */
	public static final String generateReportRedisLockCacheName(String accountID){
		return new StringBuilder()
		.append(FixedConstant.MiddlewareCacheName.LOCK_REPORT.name())
		.append(FixedConstant.REIDS_SPLICER)
		.append(accountID)
		.toString();
	}
	
	
	/**
	 * 	上行redis分布锁名
	 * @param accountID
	 * @return
	 */
	public static final String generateMORedisLockCacheName(String accountID){
		return new StringBuilder()
		.append(FixedConstant.MiddlewareCacheName.LOCK_MO.name())
		.append(FixedConstant.REIDS_SPLICER)
		.append(accountID)
		.toString();
	}

	public static final String generateAlarmAccountBalanceCacheName(String accountID){
		return new StringBuilder()
				.append(FixedConstant.MiddlewareCacheName.ALARM.name())
				.append(FixedConstant.REIDS_SPLICER)
				.append(AlarmMessage.AlarmKey.AccountBalance.name())
				.append(FixedConstant.REIDS_SPLICER)
				.append(accountID)
				.toString();
	}

	/**
	 *  账号成功率key名
	 * @param accountID
	 * @return
	 */
	public static String generateAlarmAccountSuccessRateCacheName(String accountID){
		return new StringBuilder()
				.append(FixedConstant.MiddlewareCacheName.ALARM.name())
				.append(FixedConstant.REIDS_SPLICER)
				.append(AlarmMessage.AlarmKey.AccountSuccessRate.name())
				.append(FixedConstant.REIDS_SPLICER)
				.append(accountID)
				.toString();
	}

	/**
	 *  账号延迟率key名
	 * @param accountID
	 * @return
	 */
	public static String generateAlarmAccountDelayRateCacheName(String accountID){
		return new StringBuilder()
				.append(FixedConstant.MiddlewareCacheName.ALARM.name())
				.append(FixedConstant.REIDS_SPLICER)
				.append(AlarmMessage.AlarmKey.AccountDelayRate.name())
				.append(FixedConstant.REIDS_SPLICER)
				.append(accountID)
				.toString();
	}

	/**
	 *  账号业务告警回复正常次数key名
	 * @param accountID
	 * @param alarmType
	 * @return
	 */
	public static String generateAccountNormalAlarmNumberRateCacheName(String accountID,String alarmType){
		return new StringBuilder()
				.append(FixedConstant.MiddlewareCacheName.ALARM.name())
				.append(FixedConstant.REIDS_SPLICER)
				.append(AlarmMessage.NORMAL)
				.append(FixedConstant.REIDS_SPLICER)
				.append(alarmType).append("_").append(accountID)
				.toString();
	}
}
