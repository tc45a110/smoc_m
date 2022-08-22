package com.base.common.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.cache.jedis.JedisService;
import com.base.common.util.CacheNameGeneratorUtil;
import com.base.common.vo.BusinessRouteValue;

/**
 * 对外部提供带有业务含义的服务
 */
class SubmitCacheBaseService {
	private static final Logger logger = LoggerFactory.getLogger(SubmitCacheBaseService.class);
	private static String REDIS_NAME = "jedisClientPool_submit";
	private static CacheServiceInter cacheBaseService = new JedisService("jedisClientPool_submit");
	
	
		
	/**
	 * 保存提交记录到中间件缓存
	 * @param channelID
	 * @param businessRouteValue
	 */
	public static void saveSubmitToMiddlewareCache(String channelID,BusinessRouteValue businessRouteValue){
		try {
			
			cacheBaseService.pushQueue(CacheNameGeneratorUtil.generateSubmitCacheName(channelID), businessRouteValue,REDIS_NAME);
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
			return cacheBaseService.popQueue(CacheNameGeneratorUtil.generateSubmitCacheName(channelID), BusinessRouteValue.class,REDIS_NAME);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 从缓存中获取一个通道下发队列中元素数量
	 * @param channelID
	 * @return
	 */
	public static int getChannelQueueSizeFromMiddlewareCache(String channelID){
		try {
			return (int)cacheBaseService.getQueueSize(CacheNameGeneratorUtil.generateSubmitCacheName(channelID),REDIS_NAME);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return 0;
	}
	
		
}


