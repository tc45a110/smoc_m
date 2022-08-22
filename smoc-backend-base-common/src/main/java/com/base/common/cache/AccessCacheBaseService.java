package com.base.common.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.cache.jedis.JedisService;
import com.base.common.util.CacheNameGeneratorUtil;
import com.base.common.vo.BusinessRouteValue;




/**
 * 对外部提供带有业务含义的服务
 */
class AccessCacheBaseService {
	private static final Logger logger = LoggerFactory.getLogger(AccessCacheBaseService.class);
	private static String REDIS_NAME = "jedisClientPool_access";
	private static CacheServiceInter cacheBaseService = new JedisService("jedisClientPool_access");
	
	/**
	 * 保存状态报告到中间件缓存：保留到接入层 (access)
	 * @param channelID
	 * @param businessRouteValue
	 */
	public static void saveBusinessReportToMiddlewareCache(BusinessRouteValue businessRouteValue){
		try {
			cacheBaseService.pushQueue(CacheNameGeneratorUtil.generateAccessReportCacheName(), businessRouteValue,REDIS_NAME);
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
			return cacheBaseService.popQueue(CacheNameGeneratorUtil.generateAccessReportCacheName(), BusinessRouteValue.class,REDIS_NAME);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}


