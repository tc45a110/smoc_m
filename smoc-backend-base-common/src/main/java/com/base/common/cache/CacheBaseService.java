package com.base.common.cache;

import com.base.common.util.CacheNameGeneratorUtil;
import com.base.common.vo.*;
import org.apache.commons.lang3.StringUtils;

/**
 * 对外部提供带有业务含义的服务
 */
public class CacheBaseService {
	
	/**
	 * 判断账号是否超过速率 
	 * @param accountID 账号ID
	 * @param messageNumber 本次信息条数，长短信算多条
	 * @param speed 账号速率/秒
	 * @return boolean
	 */
	public static boolean isOverAccountSpeed(String accountID,int messageNumber,int speed){
		return MainCacheBaseService.isOverAccountSpeed(accountID, messageNumber, speed);
	}

	/**
	 * 从中间件缓存中获取状态报告或上行 (account)
	 * @param accountID 账号
	 * @return 协议层状态报告对象
	 */
	public static ProtocolRouteValue getReportFromMiddlewareCache(String accountID){
		return AccountCacheBaseService.getReportFromMiddlewareCache(accountID);
	}

	/**
	 * 保存状态报告到中间件缓存  (account)
	 * @param accountID 账号
	 * @param protocolRouteValue 协议层状态报告对象
	 */
	public static void saveReportToMiddlewareCache(String accountID,ProtocolRouteValue protocolRouteValue){
		AccountCacheBaseService.saveReportToMiddlewareCache(accountID, protocolRouteValue);
	}

	/**
	 * 保存提交记录到中间件缓存
	 * @param channelID 通道ID
	 * @param businessRouteValue 消息对象
	 */
	public static void saveSubmitToMiddlewareCache(String channelID,BusinessRouteValue businessRouteValue){
		SubmitCacheBaseService.saveSubmitToMiddlewareCache(channelID,businessRouteValue);
	}
	
	/**
	 * 从中间件缓存中获取提交记录  (submit)
	 * @param channelID 通道ID
	 * @return 消息对象
	 */
	public static BusinessRouteValue getSubmitFromMiddlewareCache(String channelID){
		return SubmitCacheBaseService.getSubmitFromMiddlewareCache(channelID);
	}
	
	/**
	 * 保存响应记录到中间件缓存 (response)
	 * @param businessRouteValue 消息对象
	 */
	public static void saveResponseToMiddlewareCache(BusinessRouteValue businessRouteValue){
		ResponseCacheBaseService.saveResponseToMiddlewareCache(businessRouteValue);
	}
	
	/**
	 * 从中间件缓存获取状态报告：代理层使用  (proxy)
	 * @return 消息对象
	 */
	public static BusinessRouteValue getReportFromMiddlewareCache(){
		return ProxyCacheBaseService.getReportFromMiddlewareCache();
	}
	
	/**
	 * 保存状态报告到中间件缓存：代理协议层使用  (proxy)
	 * @param businessRouteValue 消息对象
	 */
	public static void saveReportToMiddlewareCache(BusinessRouteValue businessRouteValue){
		ProxyCacheBaseService.saveReportToMiddlewareCache(businessRouteValue);
	}
	
	/**
	 * 从中间件缓存获取响应信息：代理业务层使用 (response)
	 * @return  消息对象
	 */
	public static BusinessRouteValue getResponseToMiddlewareCache(){
		return ResponseCacheBaseService.getResponseToMiddlewareCache();			
	}
	
	/**
	 * 获取通道当天的成功条数，长短信算多条
	 * @param channelID 通道ID
	 * @return 当天成功数
	 */
	public static long getChannelTodaySuccessNumberFromMiddlewareCache(String channelID){
		return MainCacheBaseService.getChannelTodaySuccessNumberFromMiddlewareCache(channelID);
	}
	
	/**
	 * 获取通道当月的成功条数，长短信算多条
	 * @param channelID 通道ID
	 * @return 当月成功数
	 */
	public static long getChannelMonthSuccessNumberFromMiddlewareCache(String channelID){
		return MainCacheBaseService.getChannelMonthSuccessNumberFromMiddlewareCache(channelID);
	}

	/**
	 * 获取账号当天的成功条数，长短信算多条
	 * @param accountID 账号ID
	 * @return 当天成功数
	 */
	public static long getAccountIDTodaySuccessNumberFromMiddlewareCache(String accountID,String carrier){
		return MainCacheBaseService.getAccountIDTodaySuccessNumberFromMiddlewareCache(accountID,carrier);
	}

	/**
	 * 维护通道的成功条数：当天和当月
	 * @param channelID 通道ID
	 * @param successNumber 成功数量
	 */
	public static void saveChannelSuccessNumberToMiddlewareCache(String channelID,int successNumber){
		MainCacheBaseService.saveChannelSuccessNumberToMiddlewareCache(channelID, successNumber);
	}

	/**
	 * 维护账号运营商日提交/成功量
	 * @param accountID 账号ID
	 * @param carrier 运营商
	 * @param number 数量
	 */
	public static void saveAccountCarrierDailyToMiddlewareCache(String accountID,String carrier,int number){
		MainCacheBaseService.saveAccountCarrierDailyToMiddlewareCache(accountID, carrier, number);
	}
	
	/**
	 * 从缓存中获取一个通道队列中元素数量
	 * @param channelID 通道ID
	 * @return 通道队列数量
	 */
	public static int getChannelQueueSizeFromMiddlewareCache(String channelID){
		return SubmitCacheBaseService.getChannelQueueSizeFromMiddlewareCache(channelID);
	}
	
	/**
	 * 保存账号价格到中间件缓存
	 * @param accountID 账号ID
	 * @param dimension 价格维度:国际账号为国家编码，国内账号为运营商
	 * @param price 账号价格
	 */
	public static void saveAccountPriceToMiddlewareCache(String accountID,String dimension,String price){	
		MainCacheBaseService.saveAccountPriceToMiddlewareCache(accountID, dimension, price);
	}
	
	/**
	 * 从中间件缓存获取账号价格
	 * @param accountID 账号ID
	 * @param dimension 价格维度:国际账号为国家编码，国内账号为运营商
	 * @return 账号价格
	 */
	public static String getAccountPriceFromMiddlewareCache(String accountID,String dimension){	
		return MainCacheBaseService.getAccountPriceFromMiddlewareCache(accountID, dimension);
	}
	
	/**
	 * 保存通道价格到中间件缓存
	 * @param channelID 通道ID
	 * @param areaCode 业务区域;值为ALL表示全国
	 * @param price 通道价格
	 */
	public static void saveChannelPriceToMiddlewareCache(String channelID,String areaCode,String price){	
		MainCacheBaseService.saveChannelPriceToMiddlewareCache(channelID, areaCode, price);
	}
	
	/**
	 * 从中间件缓存获取通道价格
	 * @param channelID 通道ID
	 * @param areaCode 业务区域;值为ALL表示全国
	 * @return 通道价格
	 */
	public static String getChannelPriceFromMiddlewareCache(String channelID,String areaCode){	
		return MainCacheBaseService.getChannelPriceFromMiddlewareCache(channelID, areaCode);
	}
	
	
	/**
	 *	保存提交消息到代理业务层缓存中 用于匹配状态报告
	 * @param businessRouteValue 业务消息对象
	 */
	public static void saveBusinessRouteValueToMiddlewareCache(BusinessRouteValue businessRouteValue) {
		MainCacheBaseService.saveBusinessRouteValueToMiddlewareCache(businessRouteValue);
	}

	/**
	 * 	从代理业务层缓存中获取提交信息 返回状态报告
	 * @param businessRouteValue 业务消息对象
	 * @return  业务消息对象
	 */
	public static BusinessRouteValue getBusinessRouteValueFromMiddlewareCache(BusinessRouteValue businessRouteValue) {
		return MainCacheBaseService.getBusinessRouteValueFromMiddlewareCache(businessRouteValue);
	}
	
	/**
	 * 从代理业务层缓存中删除提交信息
	 * @param businessRouteValue 业务消息对象
	 */
	public static void deleteBusinessRouteValueFromMiddlewareCache(BusinessRouteValue businessRouteValue) {
		MainCacheBaseService.deleteBusinessRouteValueFromMiddlewareCache(businessRouteValue);
	}
	
	/**
	 * 保存状态报告到中间件缓存：保留到接入层 (access)
	 * @param businessRouteValue 业务消息对象
	 */
	public static void saveBusinessReportToMiddlewareCache(BusinessRouteValue businessRouteValue){
		AccessCacheBaseService.saveBusinessReportToMiddlewareCache(businessRouteValue);
	}
	
	/**
	 * 接入业务层从中间件缓存中获取状态报告：接入业务层使用 (access)
	 * @return 业务消息对象
	 */
	public static BusinessRouteValue getBusinessReportFromMiddlewareCache(){
		return AccessCacheBaseService.getBusinessReportFromMiddlewareCache();
	}
	
	/**
	 * 加锁
	 * @param key 键
	 * @param requestId 标识
	 * @param timeout 过期时间
	 */
	public static boolean lock(String key, String requestId, int timeout) {
		return MainCacheBaseService.lock(key, requestId, timeout);
	}

	/**
	 * 解锁
	 * @param key 键
	 * @param requestId 标识
	 */
	public static boolean unlock(String key, String requestId) {
		return MainCacheBaseService.unlock(key, requestId);
	}
	
	/**
	 * 从缓存中获取一个账号队列中元素数量
	 * @param accountID 账号ID
	 * @return 账号队列数量
	 */
	public static int getAccountReportQueueSizeFromMiddlewareCache(String accountID){
		return AccountCacheBaseService.getAccountReportQueueSizeFromMiddlewareCache(accountID);
	}

	/**
	 * 保存账号余额告警对象
	 * @param balanceAlarm 账号余额告警对象
	 */
	public static void saveBalanceAlarmToMiddlewareCache(BalanceAlarm balanceAlarm){
		MainCacheBaseService.saveBalanceAlarmToMiddlewareCache(balanceAlarm);
	}

	/**
	 * 获取账号余额告警对象
	 * @return 账号余额告警对象
	 */
	public static BalanceAlarm getBalanceAlarmToMiddlewareCache(String accountID){
		return MainCacheBaseService.getBalanceAlarmToMiddlewareCache(accountID);
	}

	/**
	 * 删除账号余额告警对象
	 * @param accountID 账号ID
	 */
	public static void deleteBalanceAlarmToMiddlewareCache(String accountID){
		MainCacheBaseService.deleteBalanceAlarmToMiddlewareCache(accountID);
	}

	/**
	 * 保存需要余额告警的账号
	 * @param key 键
	 */
	public static void saveAccountBalanceAlarmToMiddlewareCache(String key,String value){
		MainCacheBaseService.saveAccountBalanceAlarmToMiddlewareCache(key, value);
	}

	/**
	 * 获取上次需要余额告警的账号
	 * @param key 键
	 * @return 值
	 */
	public static String getAccountBalanceAlarmToMiddlewareCache(String key){
		return MainCacheBaseService.getAccountBalanceAlarmToMiddlewareCache(key);
	}

	/**
	 * 保存账号成功率告警信息
	 * @param configuration
	 */
	public static void saveAccountSuccessRateAlarmConfigurationToMiddlewareCache(AccountSuccessRateAlarmConfiguration configuration){
		MainCacheBaseService.saveAccountSuccessRateAlarmConfigurationToMiddlewareCache(configuration);
	}

	/**
	 * 获取账号成功率告警信息
	 * @param accountID 账号ID
	 * @return 账号成功率告警信息
	 */
	public static AccountSuccessRateAlarmConfiguration getAccountSuccessRateAlarmConfigurationToMiddlewareCache(String accountID){
		return MainCacheBaseService.getAccountSuccessRateAlarmConfigurationToMiddlewareCache(accountID);
	}

	/**
	 * 删除账号成功率告警信息
	 * @param accountID 账号ID
	 */
	public static void deleteAccountSuccessRateAlarmConfigurationToMiddlewareCache(String accountID){
		MainCacheBaseService.deleteAccountSuccessRateAlarmConfigurationToMiddlewareCache(accountID);
	}

	/**
	 * 保存账号延迟率告警信息
	 * @param configuration
	 */
	public static void saveAccountDelayRateAlarmConfigurationToMiddlewareCache(AccountDelayRateAlarmConfiguration configuration){
		MainCacheBaseService.saveAccountDelayRateAlarmConfigurationToMiddlewareCache(configuration);
	}

	/**
	 * 获取账号延迟率告警信息
	 * @param accountID 账号ID
	 * @return 账号延迟率告警信息
	 */
	public static AccountDelayRateAlarmConfiguration getAccountDelayRateAlarmConfigurationToMiddlewareCache(String accountID){
		return MainCacheBaseService.getAccountDelayRateAlarmConfigurationToMiddlewareCache(accountID);
	}

	/**
	 * 删除账号延迟率告警信息
	 * @param accountID 账号ID
	 */
	public static void deleteAccountDelayRateAlarmConfigurationToMiddlewareCache(String accountID){
		MainCacheBaseService.deleteAccountDelayRateAlarmConfigurationToMiddlewareCache(accountID);
	}

	/**
	 * 获取账号业务告警已需要恢复正常的次数
	 * @param accountID 账号ID
	 * @return 当天成功数
	 */
	public static String getAccountNormalAlarmNumberFromMiddlewareCache(String accountID,String alarmType){
		return MainCacheBaseService.getAccountNormalAlarmNumberFromMiddlewareCache(accountID,alarmType);
	}

	/**
	 * 保存账号业务告警已需要恢复正常的次数
	 * @param accountID 账号ID
	 * @return 当天成功数
	 */
	public static void saveAccountNormalAlarmNumberToMiddlewareCache(String accountID,String alarmType,String normalNumber){
		MainCacheBaseService.saveAccountNormalAlarmNumberToMiddlewareCache(accountID,alarmType,normalNumber);
	}

	public static void deleteAccountNormalAlarmNumberFromMiddlewareCache(String accountID, String alarmType) {
		MainCacheBaseService.deleteAccountNormalAlarmNumberFromMiddlewareCache(accountID,alarmType);
	}
}


