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
public class CacheBaseService {
	private static final Logger logger = LoggerFactory.getLogger(CacheBaseService.class);
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
			return cacheBaseService.isOverFlow(CacheNameGeneratorUtil.generateAccountSpeedCacheName(), accountID,60, speed*60, messageNumber);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return false;
	}
	
	/**
	 * 从中间件缓存中获取状态报告或上行
	 * @param accountID
	 * @return
	 */
	public static BusinessRouteValue getReportFromMiddlewareCache(String accountID){
		try {
			return cacheBaseService.popQueue(CacheNameGeneratorUtil.generateAccessReportCacheName(accountID), BusinessRouteValue.class);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 保存状态报告到中间件缓存
	 * @param accountID
	 * @param businessRouteValue
	 */
	public static void saveReportToMiddlewareCache(String accountID,BusinessRouteValue businessRouteValue){
		try {
			cacheBaseService.pushQueue(CacheNameGeneratorUtil.generateAccessReportCacheName(accountID), businessRouteValue);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 保存提交记录到中间件缓存
	 * @param channelID
	 * @param businessRouteValue
	 */
	public static void saveSubmitToMiddlewareCache(String channelID,BusinessRouteValue businessRouteValue){
		try {
			cacheBaseService.pushQueue(CacheNameGeneratorUtil.generateSubmitCacheName(channelID), businessRouteValue);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 从中间件缓存中获取提交记录
	 * @param accountID
	 * @return
	 */
	public static BusinessRouteValue getSubmitFromMiddlewareCache(String channelID){
		try {
			return cacheBaseService.popQueue(CacheNameGeneratorUtil.generateSubmitCacheName(channelID), BusinessRouteValue.class);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 保存响应记录到中间件缓存
	 * @param channelID
	 * @param businessRouteValue
	 */
	public static void saveResponseToMiddlewareCache(BusinessRouteValue businessRouteValue){
		try {
			cacheBaseService.pushQueue(CacheNameGeneratorUtil.generateResponseCacheName(), businessRouteValue);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 从中间件缓存获取状态报告：代理层使用
	 * @return
	 */
	public static BusinessRouteValue getReportFromMiddlewareCache(){
		try {
			return cacheBaseService.popQueue(CacheNameGeneratorUtil.generateProxyReportCacheName(), BusinessRouteValue.class);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 保存状态报告到中间件缓存：代理协议层使用
	 * @param channelID
	 * @param businessRouteValue
	 */
	public static void saveReportToMiddlewareCache(BusinessRouteValue businessRouteValue){
		try {
			cacheBaseService.pushQueue(CacheNameGeneratorUtil.generateProxyReportCacheName(), businessRouteValue);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 从中间件缓存获取响应信息：代理业务层使用
	 * @return
	 */
	public static BusinessRouteValue getResponseToMiddlewareCache(){
		try {
			return cacheBaseService.popQueue(CacheNameGeneratorUtil.generateResponseCacheName(), BusinessRouteValue.class);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
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
					channelID
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
					channelID
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
					,60*60*24, successNumber);
			
			cacheBaseService.increase(CacheNameGeneratorUtil.generateChannelMonthlyLimitCacheName(),
					channelID
					,60*60*24*31, successNumber);
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
					,60*60*24, number,1);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 从缓存中获取一个通道队列中元素数量
	 * @param channelID
	 * @return
	 */
	public static int getChannelQueueSizeFromMiddlewareCache(String channelID){
		try {
			return (int)cacheBaseService.getQueueSize(CacheNameGeneratorUtil.generateSubmitCacheName(channelID));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return 0;
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
					price);
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
					);
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
					price);
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
					);
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
					businessRouteValue);
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
							businessRouteValue.getChannelMessageID()), BusinessRouteValue.class);
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
							businessRouteValue.getChannelMessageID()));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 保存状态报告到中间件缓存：保留到接入层
	 * @param channelID
	 * @param businessRouteValue
	 */
	public static void saveBusinessReportToMiddlewareCache(BusinessRouteValue businessRouteValue){
		try {
			cacheBaseService.pushQueue(CacheNameGeneratorUtil.generateAccessReportCacheName(), businessRouteValue);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 接入业务层从中间件缓存中获取状态报告：接入业务层使用
	 * @param accountID
	 * @return
	 */
	public static BusinessRouteValue getBusinessReportFromMiddlewareCache(){
		try {
			return cacheBaseService.popQueue(CacheNameGeneratorUtil.generateAccessReportCacheName(), BusinessRouteValue.class);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 加锁
	 * @param key
	 * @param requestId
	 * @param timeout
	 */
	public static boolean lock(String key, String requestId, int timeout) {
		try {
			return cacheBaseService.lock(key, requestId, timeout);
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
			return cacheBaseService.unlock(key, requestId);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return false;
	}
}


