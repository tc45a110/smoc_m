package com.base.common.cache.jedis;

import java.util.List;

import redis.clients.jedis.params.SetParams;

/**
 * 
 */
public interface JedisClient {
	
	String set(String key, String value);
	String setMnp(String key, String value);
	String setex(String key,long seconds, String value);
	String get(String key);
	String getMnp(String key);
	Boolean exists(String key);
	Long expire(String key, long seconds);
	Long expire(String key, long seconds,int database);
	Long ttl(String key);
	Long incr(String key);
	Long hset(String key, String field, String value);
	String hget(String key, String field);
	Long hdel(String key, String... field);
	Long del(String key);
	String setex(byte[] key,long seconds, byte[] value);
	String set(byte[] key,byte[] value);
	byte[] get(byte[] key);
	Long del(byte[] key);
	Long lpush(byte[] key,byte[] value);
	byte[] rpop(byte[] key);
	Long incrBy(byte[] key, long by);
	/**
	 * SetParams:
	 * EX：设置超时时间，单位是秒。
	 * PX：设置超时时间，单位是毫秒。
	 * NX：IF NOT EXIST的缩写，只有KEY不存在的前提下才会设置值。
	 * XX：IF EXIST的缩写，只有在KEY存在的前提下才会设置值。
	 * @param key
	 * @param value
	 * @param setParams
	 * @return
	 */
	String set(String key, String value,SetParams setParams);
	Object eval(String script, List<String> keys, List<String> args);
	Long llen(String key);
	Long hincrBy(String key,String field, long by);
	Long hincrBy(String key,String field, long by,int database);

}