package com.base.common.cache;

import com.base.common.vo.BusinessRouteValue;

/**
 * 对外部提供带有业务含义的服务
 */
public class CacheBaseService {
	
	/**
	 * 判断账号是否超过速率 
	 * @param accountID
	 * @param messageNumber 本次信息条数，长短信算多条
	 * @param speed 账号速率/秒
	 * @return
	 */
	public static boolean isOverAccountSpeed(String accountID,int messageNumber,int speed){
		return MainCacheBaseService.isOverAccountSpeed(accountID, messageNumber, speed);
	}
	
	/**
	 * 从中间件缓存中获取状态报告或上行 (account)
	 * @param accountID
	 * @return
	 */
	public static BusinessRouteValue getReportFromMiddlewareCache(String accountID){
		return AccountCacheBaseService.getReportFromMiddlewareCache(accountID);
	}
	
	/**
	 * 保存状态报告到中间件缓存  (account)
	 * @param accountID
	 * @param businessRouteValue
	 */
	public static void saveReportToMiddlewareCache(String accountID,BusinessRouteValue businessRouteValue){
		AccountCacheBaseService.saveReportToMiddlewareCache(accountID, businessRouteValue);
	}
	
	/**
	 * 保存提交记录到中间件缓存 (submit)
	 * @param channelID
	 * @param businessRouteValue
	 */
	public static void saveSubmitToMiddlewareCache(String channelID,BusinessRouteValue businessRouteValue){
		SubmitCacheBaseService.saveSubmitToMiddlewareCache(channelID,businessRouteValue);
	}
	
	/**
	 * 从中间件缓存中获取提交记录  (submit)
	 * @param accountID
	 * @return
	 */
	public static BusinessRouteValue getSubmitFromMiddlewareCache(String channelID){
		return SubmitCacheBaseService.getSubmitFromMiddlewareCache(channelID);
	}
	
	/**
	 * 保存响应记录到中间件缓存 (response)
	 * @param channelID
	 * @param businessRouteValue
	 */
	public static void saveResponseToMiddlewareCache(BusinessRouteValue businessRouteValue){
		ResponseCacheBaseService.saveResponseToMiddlewareCache(businessRouteValue);
	}
	
	/**
	 * 从中间件缓存获取状态报告：代理层使用  (proxy)
	 * @return
	 */
	public static BusinessRouteValue getReportFromMiddlewareCache(){
		return ProxyCacheBaseService.getReportFromMiddlewareCache();
	}
	
	/**
	 * 保存状态报告到中间件缓存：代理协议层使用  (proxy)
	 * @param channelID
	 * @param businessRouteValue
	 */
	public static void saveReportToMiddlewareCache(BusinessRouteValue businessRouteValue){
		ProxyCacheBaseService.saveReportToMiddlewareCache(businessRouteValue);
	}
	
	/**
	 * 从中间件缓存获取响应信息：代理业务层使用 (response)
	 * @return
	 */
	public static BusinessRouteValue getResponseToMiddlewareCache(){
		return ResponseCacheBaseService.getResponseToMiddlewareCache();			
	}
	
	/**
	 * 获取通道当天的成功条数，长短信算多条
	 * @param channelID
	 * @return
	 */
	public static long getChannelTodaySuccessNumberFromMiddlewareCache(String channelID){
		return MainCacheBaseService.getChannelTodaySuccessNumberFromMiddlewareCache(channelID);
	}
	
	/**
	 * 获取通道当月的成功条数，长短信算多条
	 * @param channelID
	 * @return
	 */
	public static long getChannelMonthSuccessNumberFromMiddlewareCache(String channelID){
		return MainCacheBaseService.getChannelMonthSuccessNumberFromMiddlewareCache(channelID);
	}
	
	/**
	 * 维护通道的成功条数：当天和当月
	 * @param channelID
	 * @param successNumber
	 * @return
	 */
	public static void saveChannelSuccessNumberToMiddlewareCache(String channelID,int successNumber){
		MainCacheBaseService.saveChannelSuccessNumberToMiddlewareCache(channelID, successNumber);
	}
	
	/**
	 * 维护账号运营商日提交/成功量
	 * @param channelID
	 * @param successNumber
	 * @return
	 */
	public static void saveAccountCarrierDailyToMiddlewareCache(String accountID,String carrier,int number){
		MainCacheBaseService.saveAccountCarrierDailyToMiddlewareCache(accountID, carrier, number);
	}
	
	/**
	 * 从缓存中获取一个通道队列中元素数量
	 * @param channelID
	 * @return
	 */
	public static int getChannelQueueSizeFromMiddlewareCache(String channelID){
		return SubmitCacheBaseService.getChannelQueueSizeFromMiddlewareCache(channelID);
	}
	
	/**
	 * 保存账号价格到中间件缓存
	 * @param accountID
	 * @param dimension 价格维度:国际账号为国家编码，国内账号为运营商
	 * @param price
	 */
	public static void saveAccountPriceToMiddlewareCache(String accountID,String dimension,String price){	
		MainCacheBaseService.saveAccountPriceToMiddlewareCache(accountID, dimension, price);
	}
	
	/**
	 * 从中间件缓存获取账号价格
	 * @param accountID
	 * @param dimension 价格维度:国际账号为国家编码，国内账号为运营商
	 * @return
	 */
	public static String getAccountPriceFromMiddlewareCache(String accountID,String dimension){	
		return MainCacheBaseService.getAccountPriceFromMiddlewareCache(accountID, dimension);
	}
	
	/**
	 * 保存通道价格到中间件缓存
	 * @param channelID
	 * @param areaCoden 业务区域;值为ALL表示全国
	 * @param price
	 */
	public static void saveChannelPriceToMiddlewareCache(String channelID,String areaCode,String price){	
		MainCacheBaseService.saveChannelPriceToMiddlewareCache(channelID, areaCode, price);
	}
	
	/**
	 * 从中间件缓存获取通道价格
	 * @param channelID
	 * @param areaCoden 业务区域;值为ALL表示全国
	 * @return
	 */
	public static String getChannelPriceFromMiddlewareCache(String channelID,String areaCode){	
		return MainCacheBaseService.getChannelPriceFromMiddlewareCache(channelID, areaCode);
	}
	
	
	/**
	 *	保存提交消息到代理业务层缓存中 用于匹配状态报告
	 * @param businessRouteValue
	 */
	public static void saveBusinessRouteValueToMiddlewareCache(BusinessRouteValue businessRouteValue) {
		MainCacheBaseService.saveBusinessRouteValueToMiddlewareCache(businessRouteValue);
	}

	/**
	 * 	从代理业务层缓存中获取提交信息 返回状态报告
	 * @param businessRouteValue
	 * @return
	 */
	public static BusinessRouteValue getBusinessRouteValueFromMiddlewareCache(BusinessRouteValue businessRouteValue) {
		return MainCacheBaseService.getBusinessRouteValueFromMiddlewareCache(businessRouteValue);
	}
	
	/**
	 * 从代理业务层缓存中删除提交信息
	 * @param businessRouteValue
	 * @return
	 */
	public static void deleteBusinessRouteValueFromMiddlewareCache(BusinessRouteValue businessRouteValue) {
		MainCacheBaseService.deleteBusinessRouteValueFromMiddlewareCache(businessRouteValue);
	}
	
	/**
	 * 保存状态报告到中间件缓存：保留到接入层 (access)
	 * @param channelID
	 * @param businessRouteValue
	 */
	public static void saveBusinessReportToMiddlewareCache(BusinessRouteValue businessRouteValue){
		AccessCacheBaseService.saveBusinessReportToMiddlewareCache(businessRouteValue);
	}
	
	/**
	 * 接入业务层从中间件缓存中获取状态报告：接入业务层使用 (access)
	 * @param accountID
	 * @return
	 */
	public static BusinessRouteValue getBusinessReportFromMiddlewareCache(){
		return AccessCacheBaseService.getBusinessReportFromMiddlewareCache();
	}
	
	/**
	 * 加锁
	 * @param key
	 * @param requestId
	 * @param timeout
	 */
	public static boolean lock(String key, String requestId, int timeout) {
		return MainCacheBaseService.lock(key, requestId, timeout);
	}
	
	/**
	 * 解锁
	 * @param key
	 * @param requestId
	 */
	public static boolean unlock(String key, String requestId) {
		return MainCacheBaseService.unlock(key, requestId);
	}
}


