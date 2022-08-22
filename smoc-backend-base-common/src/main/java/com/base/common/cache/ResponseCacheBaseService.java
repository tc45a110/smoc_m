package com.base.common.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.cache.jedis.JedisService;
import com.base.common.util.CacheNameGeneratorUtil;
import com.base.common.vo.BusinessRouteValue;




/**
 * 对外部提供带有业务含义的服务
 */
class ResponseCacheBaseService {
	private static final Logger logger = LoggerFactory.getLogger(ResponseCacheBaseService.class);
	private static String REDIS_NAME = "jedisClientPool_response";
	private static CacheServiceInter cacheBaseService = new JedisService("jedisClientPool_response");
	
	
	
	/**
	 * 保存响应记录到中间件缓存
	 * @param channelID
	 * @param businessRouteValue
	 */
	public static void saveResponseToMiddlewareCache(BusinessRouteValue businessRouteValue){
		try {
			cacheBaseService.pushQueue(CacheNameGeneratorUtil.generateResponseCacheName(), businessRouteValue,REDIS_NAME);
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
			return cacheBaseService.popQueue(CacheNameGeneratorUtil.generateResponseCacheName(), BusinessRouteValue.class,REDIS_NAME);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	
}


