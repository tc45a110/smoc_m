package com.base.common.cache;

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
	 * @param accountID
	 * @param messageNumber 本次信息条数，长短信算多条
	 * @param speed 账号速率/秒
	 * @return
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
	 * @param channelID
	 * @return
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
	 * 获取通道当月的成功条数，长短信算多条
	 * @param channelID
	 * @return
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
	 * @param channelID
	 * @param successNumber
	 * @return
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
	 * @param channelID
	 * @param successNumber
	 * @return
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
	 * @param accountID
	 * @param dimension 价格维度:国际账号为国家编码，国内账号为运营商
	 * @param price
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
	 * @return
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
	 * @param channelID
	 * @param areaCoden 业务区域;值为ALL表示全国
	 * @param price
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
	 * @param channelID
	 * @param areaCoden 业务区域;值为ALL表示全国
	 * @return
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
	 * @param businessRouteValue
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
	 * @param businessRouteValue
	 * @return
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
	 * @param businessRouteValue
	 * @return
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
	 * @param key
	 * @param requestId
	 * @param timeout
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
	 * @param key
	 * @param requestId
	 */
	public static boolean unlock(String key, String requestId) {
		try {
			return cacheBaseService.unlock(key, requestId,REDIS_NAME);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return false;
	}
}


