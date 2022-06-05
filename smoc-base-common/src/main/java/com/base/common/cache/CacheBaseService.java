package com.base.common.cache;

import org.apache.commons.lang3.StringUtils;

import com.base.common.cache.jedis.JedisService;
import com.base.common.constant.FixedConstant;
import com.base.common.manager.BusinessDataManager;
import com.base.common.util.CacheNameGeneratorUtil;
import com.base.common.vo.BusinessRouteValue;


/**
 * 对外部提供带有业务含义的服务
 */
public class CacheBaseService {
	
	private static CacheServiceInter cacheBaseService = new JedisService();
	
	/**
	 * 判断账号是否超过速率
	 * @param accountID
	 * @param messageNumber 本次信息条数，长短信算多条
	 * @param speed 账号速率/秒
	 * @return
	 */
	public static boolean isOverAccountSpeed(String accountID,int messageNumber,int speed){
		return cacheBaseService.isOverFlow(CacheNameGeneratorUtil.generateSpeedCacheName(accountID), 60, speed, messageNumber);
	}
	
	/**
	 * 从中间件缓存中获取状态报告或上行
	 * @param accountID
	 * @return
	 */
	public static BusinessRouteValue getReportFromMiddlewareCache(String accountID){
		return cacheBaseService.popQueue(CacheNameGeneratorUtil.generateReportCacheName(accountID), BusinessRouteValue.class);
	}
	
	/**
	 * 保存状态报告到中间件缓存
	 * @param accountID
	 * @param businessRouteValue
	 */
	public static void saveReportToMiddlewareCache(String accountID,BusinessRouteValue businessRouteValue){
		cacheBaseService.pushQueue(CacheNameGeneratorUtil.generateReportCacheName(accountID), businessRouteValue);
	}
	
	/**
	 * 保存提交记录到中间件缓存
	 * @param channelID
	 * @param businessRouteValue
	 */
	public static void saveSubmitToMiddlewareCache(String channelID,BusinessRouteValue businessRouteValue){
		cacheBaseService.pushQueue(CacheNameGeneratorUtil.generateSubmitCacheName(channelID), businessRouteValue);
	}
	
	/**
	 * 从中间件缓存中获取提交记录
	 * @param channelID
	 * @return
	 */
	public static BusinessRouteValue getSubmitFromMiddlewareCache(String channelID){
		return cacheBaseService.popQueue(CacheNameGeneratorUtil.generateSubmitCacheName(channelID), BusinessRouteValue.class);
	}
	
	/**
	 * 保存响应记录到中间件缓存
	 * @param businessRouteValue
	 */
	public static void saveResponseToMiddlewareCache(BusinessRouteValue businessRouteValue){
		cacheBaseService.pushQueue(CacheNameGeneratorUtil.generateResponseCacheName(), businessRouteValue);
	}
	
	/**
	 * 从中间件缓存获取状态报告：代理协议层使用
	 * @return
	 */
	public static BusinessRouteValue getReportFromMiddlewareCache(){
		return cacheBaseService.popQueue(CacheNameGeneratorUtil.generateReportCacheName(), BusinessRouteValue.class);
	}
	
	/**
	 * 保存状态报告到中间件缓存：代理协议层使用
	 * @param businessRouteValue
	 */
	public static void saveReportToMiddlewareCache(BusinessRouteValue businessRouteValue){
		cacheBaseService.pushQueue(CacheNameGeneratorUtil.generateReportCacheName(), businessRouteValue);
	}
	
	/**
	 * 从中间件缓存获取响应信息：代理业务层使用
	 * @return
	 */
	public static BusinessRouteValue getResponseToMiddlewareCache(){
		return cacheBaseService.popQueue(CacheNameGeneratorUtil.generateResponseCacheName(), BusinessRouteValue.class);
	}
	
	/**
	 * 获取通道当天的成功条数，长短信算多条
	 * @param channelID
	 * @return
	 */
	public static long getChannelTodaySuccessNumberFromMiddlewareCache(String channelID){
		String result = cacheBaseService.getHashValue(
				CacheNameGeneratorUtil.generateChannelDailyLimitCacheName(), 
				channelID
				);
		if(StringUtils.isNotEmpty(result)){
			return Long.parseLong(result);
		}
		return 0;
	}
	
	/**
	 * 获取通道当月的成功条数，长短信算多条
	 * @param channelID
	 * @return
	 */
	public static long getChannelMonthSuccessNumberFromMiddlewareCache(String channelID){
		String result = cacheBaseService.getHashValue(
				CacheNameGeneratorUtil.generateChannelMonthlyLimitCacheName(), 
				channelID
				);
				
		if(StringUtils.isNotEmpty(result)){
			return Long.parseLong(result);
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
		
		cacheBaseService.increase(CacheNameGeneratorUtil.generateChannelDailyLimitCacheName(),
				channelID
				,60*60*24, successNumber);
		
		cacheBaseService.increase(CacheNameGeneratorUtil.generateChannelMonthlyLimitCacheName(),
				channelID
				,60*60*24*31, successNumber);
	}
	
	/**
	 * 维护账号运营商日提交/成功量
	 * @param accountID
	 * @param carrier
	 * @param number
	 * @return
	 */
	public static void saveAccountCarrierDailyToMiddlewareCache(String accountID,String carrier,int number){
		cacheBaseService.increase(CacheNameGeneratorUtil.generateAccountCarrierDailyLimitCacheName(),
				new StringBuilder().append(accountID).append("_").append(carrier).toString()
				,60*60*24, number);
	}
	
	/**
	 * 从缓存中获取一个通道队列中元素数量
	 * @param channelID
	 * @return
	 */
	public static int getChannelQueueSizeFromMiddlewareCache(String channelID){
		return (int)cacheBaseService.getQueueSize(CacheNameGeneratorUtil.generateSubmitCacheName(channelID));
	}
	
	/**
	 * 保存账号价格到中间件缓存
	 * @param accountID
	 * @param dimension 价格维度:国际账号为国家编码，国内账号为运营商
	 * @param price
	 */
	public static void saveAccountPriceToMiddlewareCache(String accountID,String dimension,String price){	
		cacheBaseService.putHashString(
				CacheNameGeneratorUtil.generateAccountPriceCacheName(), 
				3600*24, 
				new StringBuilder().append(accountID).append("_").append(dimension).toString(), 
				price);
	}
	
	/**
	 * 从中间件缓存获取账号价格
	 * @param accountID
	 * @param dimension 价格维度:国际账号为国家编码，国内账号为运营商
	 * @return
	 */
	public static String getAccountPriceFromMiddlewareCache(String accountID,String dimension){	
		return cacheBaseService.getHashValue(
				CacheNameGeneratorUtil.generateAccountPriceCacheName(), 
				new StringBuilder().append(accountID).append("_").append(dimension).toString()
				);
	}
	
	/**
	 * 保存通道价格到中间件缓存
	 * @param channelID
	 * @param areaCode 业务区域;值为ALL表示全国
	 * @param price
	 */
	public static void saveChannelPriceToMiddlewareCache(String channelID,String areaCode,String price){	
		cacheBaseService.putHashString(
				CacheNameGeneratorUtil.generateChannelPriceCacheName(), 
				3600*24, 
				new StringBuilder().append(channelID).append("_").append(areaCode).toString(), 
				price);
	}
	
	/**
	 * 从中间件缓存获取通道价格
	 * @param channelID
	 * @param areaCode 业务区域;值为ALL表示全国
	 * @return
	 */
	public static String getChannelPriceFromMiddlewareCache(String channelID,String areaCode){	
		return cacheBaseService.getHashValue(
				CacheNameGeneratorUtil.generateChannelPriceCacheName(), 
				new StringBuilder().append(channelID).append("_").append(areaCode).toString()
				);
	}
	
	
	/**
	 *	保存提交消息到代理业务层缓存中 用于匹配状态报告
	 * @param businessRouteValue
	 */
	public static void saveBusinessRouteValueToMiddlewareCache(BusinessRouteValue businessRouteValue) {
		cacheBaseService.putObject(
				CacheNameGeneratorUtil.generateMessageIDCacheName(
				businessRouteValue.getPhoneNumber(), businessRouteValue.getChannelMessageID()),
				BusinessDataManager.getInstance().getResponseStoreToRedisExpirationTime(),
				businessRouteValue);
	}

	/**
	 * 	从代理业务层缓存中获取提交信息 返回状态报告
	 * @param businessRouteValue
	 * @return
	 */
	public static BusinessRouteValue getBusinessRouteValueFromMiddlewareCache(BusinessRouteValue businessRouteValue) {
		return cacheBaseService.getObject(CacheNameGeneratorUtil.generateMessageIDCacheName(businessRouteValue.getPhoneNumber(),
						businessRouteValue.getChannelMessageID()), BusinessRouteValue.class);
	}
	
	/**
	 * 从代理业务层缓存中删除提交信息
	 * @param businessRouteValue
	 * @return
	 */
	public static void deleteBusinessRouteValueFromMiddlewareCache(BusinessRouteValue businessRouteValue) {
		cacheBaseService.delObject(CacheNameGeneratorUtil.generateMessageIDCacheName(businessRouteValue.getPhoneNumber(),
						businessRouteValue.getChannelMessageID()));
	}
	
	/**
	 * 保存状态报告到中间件缓存：代理业务层使用
	 * @param businessRouteValue
	 */
	public static void saveBusinessReportToMiddlewareCache(BusinessRouteValue businessRouteValue){
		cacheBaseService.pushQueue(CacheNameGeneratorUtil.generateReportCacheName(FixedConstant.MiddlewareCacheName.BUSINESS.name()), businessRouteValue);
	}
	
	/**
	 * 接入业务层从中间件缓存中获取状态报告：接入业务层使用
	 * @return
	 */
	public static BusinessRouteValue getBusinessReportFromMiddlewareCache(){
		return cacheBaseService.popQueue(CacheNameGeneratorUtil.generateReportCacheName(FixedConstant.MiddlewareCacheName.BUSINESS.name()), BusinessRouteValue.class);
	}
	
	/**
	 * 加锁
	 * @param key
	 * @param requestId
	 * @param timeout
	 */
	public static boolean lock(String key, String requestId, int timeout) {
		return cacheBaseService.lock(key, requestId, timeout);
	}
	
	/**
	 * 解锁
	 * @param key
	 * @param requestId
	 */
	public static boolean unlock(String key, String requestId) {
		return cacheBaseService.unlock(key, requestId);
	}
}


