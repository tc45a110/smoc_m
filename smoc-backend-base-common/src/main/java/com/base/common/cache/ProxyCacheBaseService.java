package com.base.common.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.cache.jedis.JedisService;
import com.base.common.util.CacheNameGeneratorUtil;
import com.base.common.vo.BusinessRouteValue;




/**
 * 对外部提供带有业务含义的服务
 */
class ProxyCacheBaseService {
	private static final Logger logger = LoggerFactory.getLogger(ProxyCacheBaseService.class);
	private static String REDIS_NAME = "jedisClientPool_proxy";
	private static CacheServiceInter cacheBaseService = new JedisService("jedisClientPool_proxy");
		
	/**
	 * 从中间件缓存获取状态报告：代理层使用
	 * @return
	 */
	public static BusinessRouteValue getReportFromMiddlewareCache(){
		try {
			return cacheBaseService.popQueue(CacheNameGeneratorUtil.generateProxyReportCacheName(), BusinessRouteValue.class,REDIS_NAME);
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
			cacheBaseService.pushQueue(CacheNameGeneratorUtil.generateProxyReportCacheName(), businessRouteValue,REDIS_NAME);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
}


