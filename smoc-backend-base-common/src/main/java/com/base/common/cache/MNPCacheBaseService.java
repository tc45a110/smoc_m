package com.base.common.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.cache.jedis.JedisService;
import com.base.common.util.CacheNameGeneratorUtil;




/**
 * 对外部提供带有业务含义的服务
 */
public class MNPCacheBaseService {
	private static final Logger logger = LoggerFactory.getLogger(MNPCacheBaseService.class);
	
	private static String REDIS_NAME = "jedisClientPool_mnp";
	private static CacheServiceInter MNPCacheBaseService = new JedisService(REDIS_NAME);

	/**
	 * 从中间件缓存中获取一个号码是否携号转网为其他运营商
	 * @param phoneNumber
	 * @return
	 */
	public static String getMNPFromMiddlewareCache(String phoneNumber){
		try {
			return MNPCacheBaseService.getMnpString(CacheNameGeneratorUtil.generateMNPCacheName(phoneNumber), REDIS_NAME);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 保存携号转网数据到中间件缓存
	 * @param phoneNumber
	 * @param carrier
	 */
	public static void saveMNPToMiddlewareCache(String phoneNumber,String carrier){
		try {
			MNPCacheBaseService.putMnpString(CacheNameGeneratorUtil.generateMNPCacheName(phoneNumber), carrier, REDIS_NAME);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
	}
	
	public static String getPoolString(){
		return MNPCacheBaseService.getPoolString(REDIS_NAME);
	}
	
}


