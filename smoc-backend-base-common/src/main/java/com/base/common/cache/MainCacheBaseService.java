package com.base.common.cache;

import com.base.common.constant.FixedConstant;
import com.base.common.constant.RedisHashKeyConstant;
import com.base.common.vo.AccountDelayRateAlarmConfiguration;
import com.base.common.vo.AccountSuccessRateAlarmConfiguration;
import com.base.common.vo.BalanceAlarm;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.cache.jedis.JedisService;
import com.base.common.manager.BusinessDataManager;
import com.base.common.util.CacheNameGeneratorUtil;
import com.base.common.vo.BusinessRouteValue;




/**
 * 对外部提供带有业务含义的服务
 */
class MainCacheBaseService {
	private static final Logger logger = LoggerFactory.getLogger(MainCacheBaseService.class);
	private static String REDIS_NAME = "jedisClientPool_main";
	private static CacheServiceInter cacheBaseService = new JedisService();
	
	/**
	 * 判断账号是否超过速率
	 * @param accountID 账号ID
	 * @param messageNumber 本次信息条数，长短信算多条
	 * @param speed 账号速率/秒
	 * @return boolean
	 */
	public static boolean isOverAccountSpeed(String accountID,int messageNumber,int speed){
		try {
			return cacheBaseService.isOverFlow(CacheNameGeneratorUtil.generateAccountSpeedCacheName(), accountID,60, speed*60, messageNumber,REDIS_NAME);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return false;
	}
	
	/**
	 * 获取通道当天的成功条数，长短信算多条
	 * @param channelID 通道ID
	 * @return 当天成功数
	 */
	public static long getChannelTodaySuccessNumberFromMiddlewareCache(String channelID){
		try {
			String result = cacheBaseService.getHashValue(
					CacheNameGeneratorUtil.generateChannelDailyLimitCacheName(), 
					channelID,REDIS_NAME
					);
			if(StringUtils.isNotEmpty(result)){
				return Long.parseLong(result);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return 0;
	}

	/**
	 * 获取账号当天的成功条数，长短信算多条
	 * @param accountID 账号ID
	 * @return 当天成功数
	 */
	public static long getAccountIDTodaySuccessNumberFromMiddlewareCache(String accountID){
		try {
			String cmccResult = cacheBaseService.getHashValue(
					CacheNameGeneratorUtil.generateAccountCarrierDailyLimitCacheName(),
					new StringBuilder().append(accountID).append("_").append(FixedConstant.Carrier.CMCC.name()).toString(), REDIS_NAME
			);
			String unicResult = cacheBaseService.getHashValue(
					CacheNameGeneratorUtil.generateAccountCarrierDailyLimitCacheName(),
					new StringBuilder().append(accountID).append("_").append(FixedConstant.Carrier.UNIC.name()).toString(), REDIS_NAME
			);
			String telcResult = cacheBaseService.getHashValue(
					CacheNameGeneratorUtil.generateAccountCarrierDailyLimitCacheName(),
					new StringBuilder().append(accountID).append("_").append(FixedConstant.Carrier.TELC.name()).toString(), REDIS_NAME
			);
			String intlResult = cacheBaseService.getHashValue(
					CacheNameGeneratorUtil.generateAccountCarrierDailyLimitCacheName(),
					new StringBuilder().append(accountID).append("_").append(FixedConstant.Carrier.INTL.name()).toString(), REDIS_NAME
			);
			return (StringUtils.isNotEmpty(cmccResult) ? Long.parseLong(cmccResult) : 0) +
					(StringUtils.isNotEmpty(unicResult) ? Long.parseLong(cmccResult) : 0) +
					(StringUtils.isNotEmpty(telcResult) ? Long.parseLong(cmccResult) : 0) +
					(StringUtils.isNotEmpty(intlResult) ? Long.parseLong(cmccResult) : 0);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return 0;
	}

	/**
	 * 获取账号当天的成功条数，长短信算多条
	 * @param accountID 账号ID
	 * @return 当天成功数
	 */
	public static long getAccountIDTodaySuccessNumberFromMiddlewareCache(String accountID,String carrier){
		try {
			String result = cacheBaseService.getHashValue(
					CacheNameGeneratorUtil.generateAccountCarrierDailyLimitCacheName(),
					new StringBuilder().append(accountID).append("_").append(carrier).toString(), REDIS_NAME
			);
			return (StringUtils.isNotEmpty(result) ? Long.parseLong(result) : 0);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return 0;
	}
	
	/**
	 * 获取通道当月的成功条数，长短信算多条
	 * @param channelID 通道ID
	 * @return 当月成功数
	 */
	public static long getChannelMonthSuccessNumberFromMiddlewareCache(String channelID){
		try {
			String result = cacheBaseService.getHashValue(
					CacheNameGeneratorUtil.generateChannelMonthlyLimitCacheName(), 
					channelID,REDIS_NAME
					);
					
			if(StringUtils.isNotEmpty(result)){
				return Long.parseLong(result);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return 0;
	}


	
	/**
	 * 维护通道的成功条数：当天和当月
	 * @param channelID 通道ID
	 * @param successNumber 成功数量
	 */
	public static void saveChannelSuccessNumberToMiddlewareCache(String channelID,int successNumber){
		
		try {
			cacheBaseService.increase(CacheNameGeneratorUtil.generateChannelDailyLimitCacheName(),
					channelID
					,60*60*24, successNumber,REDIS_NAME);
			
			cacheBaseService.increase(CacheNameGeneratorUtil.generateChannelMonthlyLimitCacheName(),
					channelID
					,60*60*24*31, successNumber,REDIS_NAME);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 维护账号运营商日提交/成功量
	 * @param accountID 账号ID
	 * @param carrier 运营商
	 * @param number 数量
	 */
	public static void saveAccountCarrierDailyToMiddlewareCache(String accountID,String carrier,int number){
		try {
			cacheBaseService.increase(CacheNameGeneratorUtil.generateAccountCarrierDailyLimitCacheName(),
					new StringBuilder().append(accountID).append("_").append(carrier).toString()
					,60*60*24, number,1,REDIS_NAME);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 保存账号价格到中间件缓存
	 * @param accountID 账号ID
	 * @param dimension 价格维度:国际账号为国家编码，国内账号为运营商
	 * @param price 账号价格
	 */
	public static void saveAccountPriceToMiddlewareCache(String accountID,String dimension,String price){	
		try {
			cacheBaseService.putHashString(
					CacheNameGeneratorUtil.generateAccountPriceCacheName(), 
					3600*24, 
					new StringBuilder().append(accountID).append("_").append(dimension).toString(), 
					price,REDIS_NAME);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 从中间件缓存获取账号价格
	 * @param accountID
	 * @param dimension 价格维度:国际账号为国家编码，国内账号为运营商
	 * @return 账号价格
	 */
	public static String getAccountPriceFromMiddlewareCache(String accountID,String dimension){	
		try {
			return cacheBaseService.getHashValue(
					CacheNameGeneratorUtil.generateAccountPriceCacheName(), 
					new StringBuilder().append(accountID).append("_").append(dimension).toString()
					,REDIS_NAME);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 保存通道价格到中间件缓存
	 * @param channelID 通道ID
	 * @param areaCode 业务区域;值为ALL表示全国
	 * @param price 价格
	 */
	public static void saveChannelPriceToMiddlewareCache(String channelID,String areaCode,String price){	
		try {
			cacheBaseService.putHashString(
					CacheNameGeneratorUtil.generateChannelPriceCacheName(), 
					3600*24, 
					new StringBuilder().append(channelID).append("_").append(areaCode).toString(), 
					price,REDIS_NAME);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 从中间件缓存获取通道价格
	 * @param channelID 通道ID
	 * @param areaCode 业务区域;值为ALL表示全国
	 * @return 通道价格
	 */
	public static String getChannelPriceFromMiddlewareCache(String channelID,String areaCode){	
		try {
			return cacheBaseService.getHashValue(
					CacheNameGeneratorUtil.generateChannelPriceCacheName(), 
					new StringBuilder().append(channelID).append("_").append(areaCode).toString()
					,REDIS_NAME);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	
	/**
	 *	保存提交消息到代理业务层缓存中 用于匹配状态报告
	 * @param businessRouteValue 业务消息对象
	 */
	public static void saveBusinessRouteValueToMiddlewareCache(BusinessRouteValue businessRouteValue) {
		try {
			cacheBaseService.putObject(
					CacheNameGeneratorUtil.generateMessageIDCacheName(
					businessRouteValue.getPhoneNumber(), businessRouteValue.getChannelMessageID()),
					BusinessDataManager.getInstance().getResponseStoreToRedisExpirationTime(),
					businessRouteValue,REDIS_NAME);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

	/**
	 * 	从代理业务层缓存中获取提交信息 返回状态报告
	 * @param businessRouteValue 业务消息对象
	 * @return 业务消息对象
	 */
	public static BusinessRouteValue getBusinessRouteValueFromMiddlewareCache(BusinessRouteValue businessRouteValue) {
		try {
			return cacheBaseService.getObject(CacheNameGeneratorUtil.generateMessageIDCacheName(businessRouteValue.getPhoneNumber(),
							businessRouteValue.getChannelMessageID()), BusinessRouteValue.class,REDIS_NAME);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 从代理业务层缓存中删除提交信息
	 * @param businessRouteValue 业务消息对象
	 */
	public static void deleteBusinessRouteValueFromMiddlewareCache(BusinessRouteValue businessRouteValue) {
		try {
			cacheBaseService.delObject(CacheNameGeneratorUtil.generateMessageIDCacheName(businessRouteValue.getPhoneNumber(),
							businessRouteValue.getChannelMessageID()),REDIS_NAME);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 加锁
	 * @param key 键
	 * @param requestId 标识
	 * @param timeout 过期时间
	 */
	public static boolean lock(String key, String requestId, int timeout) {
		try {
			return cacheBaseService.lock(key, requestId, timeout,REDIS_NAME);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return false;
	}
	
	/**
	 * 解锁
	 * @param key 键
	 * @param requestId 标识
	 */
	public static boolean unlock(String key, String requestId) {
		try {
			return cacheBaseService.unlock(key, requestId,REDIS_NAME);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return false;
	}

	/**
	 * 保存账号余额告警信息
	 * @param balanceAlarm 账号余额告警对象
	 */
	public static void saveBalanceAlarmToMiddlewareCache(BalanceAlarm balanceAlarm){
		cacheBaseService.putObject(CacheNameGeneratorUtil.generateAlarmAccountBalanceCacheName(balanceAlarm.getAccountID()),balanceAlarm,REDIS_NAME);
	}

	/**
	 * 获取账号余额告警对象
	 * @param accountID 账号ID
	 * @return 账号余额告警对象
	 */
	public static BalanceAlarm getBalanceAlarmToMiddlewareCache(String accountID){
		return cacheBaseService.getObject(CacheNameGeneratorUtil.generateAlarmAccountBalanceCacheName(accountID) , BalanceAlarm.class, REDIS_NAME);
	}

	/**
	 * 删除账号余额告警对象
	 * @param accountID 账号ID
	 */
	public static void deleteBalanceAlarmToMiddlewareCache(String accountID){
		cacheBaseService.delObject(CacheNameGeneratorUtil.generateAlarmAccountBalanceCacheName(accountID) , REDIS_NAME);
	}

	/**
	 * 获取需要余额告警的账号
	 * @param key 键
	 * @return 值
	 */
	public static String getAccountBalanceAlarmToMiddlewareCache(String key){
		return cacheBaseService.getString(CacheNameGeneratorUtil.generateAlarmAccountBalanceCacheName(key), REDIS_NAME);
	}

	/**
	 * 保存需要余额告警的账号
	 * @param key 键
	 */
	public static void saveAccountBalanceAlarmToMiddlewareCache(String key,String value){
		cacheBaseService.putString(CacheNameGeneratorUtil.generateAlarmAccountBalanceCacheName(key),12*60*60, value, REDIS_NAME);
	}

	/**
	 * 保存账号成功率告警信息
	 * @param configuration
	 */
	public static void saveAccountSuccessRateAlarmConfigurationToMiddlewareCache(AccountSuccessRateAlarmConfiguration configuration){
		int timeout = configuration.getEvaluateIntervalTime() * configuration.getEvaluateNumber() * 2 * 60;
		cacheBaseService.putObject(CacheNameGeneratorUtil.generateAlarmAccountSuccessRateCacheName(configuration.getAccountID()), timeout, configuration, REDIS_NAME);
	}

	/**
	 * 获取账号成功率告警信息
	 * @param accountID 账号ID
	 * @return 账号成功率告警信息
	 */
	public static AccountSuccessRateAlarmConfiguration getAccountSuccessRateAlarmConfigurationToMiddlewareCache(String accountID){
		return cacheBaseService.getObject(CacheNameGeneratorUtil.generateAlarmAccountSuccessRateCacheName(accountID) , AccountSuccessRateAlarmConfiguration.class, REDIS_NAME);
	}

	/**
	 * 删除账号成功率告警信息
	 * @param accountID 账号ID
	 */
	public static void deleteAccountSuccessRateAlarmConfigurationToMiddlewareCache(String accountID){
		cacheBaseService.delObject(CacheNameGeneratorUtil.generateAlarmAccountSuccessRateCacheName(accountID) , REDIS_NAME);
	}

	/**
	 * 保存账号延迟率告警信息
	 * @param configuration
	 */
	public static void saveAccountDelayRateAlarmConfigurationToMiddlewareCache(AccountDelayRateAlarmConfiguration configuration){
		int timeout = configuration.getEvaluateIntervalTime() * configuration.getEvaluateNumber() * 2 * 60;
		cacheBaseService.putObject(CacheNameGeneratorUtil.generateAlarmAccountDelayRateCacheName(configuration.getAccountID()), timeout, configuration, REDIS_NAME);
	}

	/**
	 * 获取账号延迟率告警信息
	 * @param accountID 账号ID
	 * @return 账号延迟率告警信息
	 */
	public static AccountDelayRateAlarmConfiguration getAccountDelayRateAlarmConfigurationToMiddlewareCache(String accountID){
		return cacheBaseService.getObject(CacheNameGeneratorUtil.generateAlarmAccountDelayRateCacheName(accountID) , AccountDelayRateAlarmConfiguration.class, REDIS_NAME);
	}

	/**
	 * 删除账号延迟率告警信息
	 * @param accountID 账号ID
	 */
	public static void deleteAccountDelayRateAlarmConfigurationToMiddlewareCache(String accountID){
		cacheBaseService.delObject(CacheNameGeneratorUtil.generateAlarmAccountDelayRateCacheName(accountID) , REDIS_NAME);
	}

	/**
	 * 获取账号业务告警已需要恢复正常的次数
	 * @param accountID 账号ID
	 * @return 当天成功数
	 */
	public static String getAccountNormalAlarmNumberFromMiddlewareCache(String accountID,String alarmType){
		try {
			return cacheBaseService.getString(
					CacheNameGeneratorUtil.generateAccountNormalAlarmNumberRateCacheName(accountID, alarmType), REDIS_NAME
			);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}

	/**
	 * 保存账号业务告警已需要恢复正常的次数
	 * @param accountID 账号ID
	 * @return 当天成功数
	 */
	public static void saveAccountNormalAlarmNumberToMiddlewareCache(String accountID,String alarmType,String normalNumber){
		try {
			cacheBaseService.putString(
					CacheNameGeneratorUtil.generateAccountNormalAlarmNumberRateCacheName(accountID, alarmType),
					60 * 5,
					normalNumber,REDIS_NAME);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

	public static void deleteAccountNormalAlarmNumberFromMiddlewareCache(String accountID, String alarmType) {
		cacheBaseService.delString(CacheNameGeneratorUtil.generateAccountNormalAlarmNumberRateCacheName(accountID, alarmType),REDIS_NAME);
	}
}


