package com.base.common.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.cache.jedis.JedisService;
import com.base.common.util.CacheNameGeneratorUtil;
import com.base.common.vo.ProtocolRouteValue;

/**
 * 对外部提供带有业务含义的服务
 */
class AccountCacheBaseService {
	private static final Logger logger = LoggerFactory.getLogger(AccountCacheBaseService.class);
	private static String REDIS_NAME = "jedisClientPool_account";
	private static CacheServiceInter cacheBaseService = new JedisService(REDIS_NAME);
	
	/**
	 * 保存状态报告到中间件缓存  (account)
	 * @param accountID 账号
	 * @param protocolRouteValue 协议层状态报告对象
	 */
	public static void saveReportToMiddlewareCache(String accountID,ProtocolRouteValue protocolRouteValue){
		try {
			cacheBaseService.pushQueue(CacheNameGeneratorUtil.generateAccessReportCacheName(accountID), protocolRouteValue,REDIS_NAME);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 从中间件缓存中获取状态报告或上行 (account)
	 * @param accountID 账号
	 * @return 协议层状态报告对象
	 */
	public static ProtocolRouteValue getReportFromMiddlewareCache(String accountID){
		try {
			return cacheBaseService.popQueue(CacheNameGeneratorUtil.generateAccessReportCacheName(accountID), ProtocolRouteValue.class,REDIS_NAME);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 从缓存中获取一个账号队列中元素数量
	 * @param accountID 账号
	 * @return 账号队列数量
	 */
	public static int getAccountReportQueueSizeFromMiddlewareCache(String accountID){
		try {
			return (int)cacheBaseService.getQueueSize(CacheNameGeneratorUtil.generateAccessReportCacheName(accountID),REDIS_NAME);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return 0;
	}
	
}


