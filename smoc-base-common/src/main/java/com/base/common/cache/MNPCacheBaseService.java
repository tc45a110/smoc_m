package com.base.common.cache;

import com.base.common.cache.jedis.JedisService;
import com.base.common.util.CacheNameGeneratorUtil;

/**
 * 对外部提供带有业务含义的服务
 */
public class MNPCacheBaseService {
	private static CacheServiceInter MNPCacheBaseService = new JedisService("jedisClientPool1");

	/**
	 * 从中间件缓存中获取一个号码是否携号转网为其他运营商
	 * @param phoneNumber
	 * @return
	 */
	public static String getMNPFromMiddlewareCache(String phoneNumber){
		return MNPCacheBaseService.getString(CacheNameGeneratorUtil.generateMNPCacheName(phoneNumber));
	}
	
	/**
	 * 保存携号转网数据到中间件缓存
	 * @param phoneNumber
	 * @param carrier
	 */
	public static void saveMNPToMiddlewareCache(String phoneNumber,String carrier){
		MNPCacheBaseService.putString(CacheNameGeneratorUtil.generateMNPCacheName(phoneNumber), carrier);
	}
	
}


