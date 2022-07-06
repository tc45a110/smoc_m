package com.base.common.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.cache.jedis.JedisService;
import com.base.common.util.CacheNameGeneratorUtil;
import com.base.common.vo.BusinessRouteValue;




/**
 * 对外部提供带有业务含义的服务
 */
public class AccountCacheBaseService {
	private static final Logger logger = LoggerFactory.getLogger(AccountCacheBaseService.class);
	private static CacheServiceInter cacheBaseService = new JedisService("jedisClientPool_account");
	
	/**
	 * 保存状态报告到中间件缓存  (account)
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
	 * 从中间件缓存中获取状态报告或上行 (account)
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
}


