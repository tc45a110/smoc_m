package com.base.common.cache.jedis;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

public class JedisClientPool implements JedisClient {
	private JedisPool jedisPool;

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
	
	@Override
	public String set(String key, String value) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.set(key, value);
		jedis.close();
		return result;
	}
	
	@Override
	public String setex(String key,long seconds, String value) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.setex(key,seconds, value);
		jedis.close();
		return result;
	}

	@Override
	public String get(String key) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.get(key);
		jedis.close();
		return result;
	}

	@Override
	public Boolean exists(String key) {
		Jedis jedis = jedisPool.getResource();
		Boolean result = jedis.exists(key);
		jedis.close();
		return result;
	}

	@Override
	public Long expire(String key, long seconds) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.expire(key, seconds);
		jedis.close();
		return result;
	}

	@Override
	public Long ttl(String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.ttl(key);
		jedis.close();
		return result;
	}

	@Override
	public Long incr(String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.incr(key);
		jedis.close();
		return result;
	}

	@Override
	public Long hset(String key, String field, String value) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.hset(key, field, value);
		jedis.close();
		return result;
	}

	@Override
	public String hget(String key, String field) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.hget(key, field);
		jedis.close();
		return result;
	}

	@Override
	public Long hdel(String key, String... field) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.hdel(key, field);
		jedis.close();
		return result;
	}
	
	@Override
	public Long del(String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.del(key);
		jedis.close();
		return result;
	}
	
	@Override
	public String setex(byte[] key, long seconds, byte[] value) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.setex(key,seconds, value);
		jedis.close();
		return result;
	}
	
	@Override
	public String set(byte[] key, byte[] value) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.set(key, value);
		jedis.close();
		return result;
	}
	
	@Override
	public byte[] get(byte[] key) {
		Jedis jedis = jedisPool.getResource();
		byte[] result = jedis.get(key);
		jedis.close();
		return result;
	}
	
	@Override
	public Long del(byte[] key) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.del(key);
		jedis.close();
		return result;
	}

	@Override
	public Long lpush(byte[] key, byte[] value) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.lpush(key, value);
		jedis.close();
		return result;
	}
	
	@Override
	public byte[] rpop(byte[] key) {
		Jedis jedis = jedisPool.getResource();
		byte[] result = jedis.rpop(key);
		jedis.close();
		return result;
	}
	
	@Override
	public Long incrBy(byte[] key, long by) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.incrBy(key,by);
		jedis.close();
		return result;
	}

	@Override
	public String set(String key, String value, SetParams setParams) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.set(key, value, setParams);
		jedis.close();
		return result;
	}

	@Override
	public Object eval(String script, List<String> keys, List<String> args) {
		Jedis jedis = jedisPool.getResource();
		Object result = jedis.eval(script, keys, args);
		jedis.close();
		return result;
	}

	@Override
	public Long llen(String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.llen(key);
		jedis.close();
		return result;
	}

	@Override
	public Long hincrBy(String key, String field, long by) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.hincrBy(key,field,by);
		jedis.close();
		return result;
	}

}
