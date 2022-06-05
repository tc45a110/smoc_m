package com.base.common.util;

import com.base.common.constant.RedisHashKeyConstant;
import com.base.common.constant.FixedConstant;

public class CacheNameGeneratorUtil {
	
	/**
	 * 生成流速的缓存名称
	 * @param key
	 * @return
	 */
	public static final String generateSpeedCacheName(String key) {
		return new StringBuilder()
		.append(FixedConstant.MiddlewareCacheName.SPEED)
		.append(FixedConstant.SPLICER)
		.append(key)
		.toString();
	}
	
	/**
	 * 生成携号转网的缓存名称
	 * @param key
	 * @return
	 */
	public static final String generateMNPCacheName(String key) {
		return new StringBuilder()
		.append(FixedConstant.MiddlewareCacheName.MNP)
		.append(FixedConstant.SPLICER)
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
	 * 生成状态报告队列名称
	 * @param key
	 * @return
	 */
	public static final String generateReportCacheName(String key) {
		return new StringBuilder()
		.append(FixedConstant.MiddlewareCacheName.REPORT)
		.append(FixedConstant.SPLICER)
		.append(key)
		.toString();
	}
	
	/**
	 * 状态报告队列
	 * @return
	 */
	public static final String generateReportCacheName() {
		return new StringBuilder()
		.append(FixedConstant.MiddlewareCacheName.REPORT)
		.append(FixedConstant.SPLICER)
		.toString();
	}
	
	/**
	 * 生成响应队列名称
	 * @return
	 */
	public static final String generateResponseCacheName() {
		return new StringBuilder()
		.append(FixedConstant.MiddlewareCacheName.RESPONSE)
		.append(FixedConstant.SPLICER)
		.toString();
	}
	
	/**
	 * 生成提交队列名称
	 * @param key
	 * @return
	 */
	public static final String generateSubmitCacheName(String key){
		return new StringBuilder()
		.append(FixedConstant.MiddlewareCacheName.SUBMIT)
		.append(FixedConstant.SPLICER)
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
		.append(FixedConstant.MiddlewareCacheName.MESSAGE_ID)
		.append(FixedConstant.SPLICER)
		.append(mobile)
		.append(FixedConstant.SPLICER)
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
		.append(FixedConstant.MiddlewareCacheName.LOCK_REPORT)
		.append(FixedConstant.SPLICER)
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
		.append(FixedConstant.MiddlewareCacheName.LOCK_MO)
		.append(FixedConstant.SPLICER)
		.append(accountID)
		.toString();
	}

}
