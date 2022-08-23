package com.base.common.cache.jedis;

import java.util.List;

import com.base.common.manager.ResourceManager;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

public class JedisClientPool implements JedisClient {
	private static int DATABASE = ResourceManager.getInstance().getIntValue("redis.database");
	
	private JedisPool jedisPool;

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
	
	@Override
	public String set(String key, String value) {
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			jedis.select(DATABASE);
			String result = jedis.set(key, value);
			return result;
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	
	@Override
	public String setMnp(String key, String value) {
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			String result = jedis.set(key, value);
			return result;
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}

	@Override
	public String setex(String key,long seconds, String value) {
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			jedis.select(DATABASE);
			String result = jedis.setex(key,seconds, value);
			return result;
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}

	@Override
	public String get(String key) {
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			jedis.select(DATABASE);
			String result = jedis.get(key);
			return result;
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	@Override
	public String getMnp(String key) {
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			String result = jedis.get(key);
			return result;
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}

	@Override
	public Boolean exists(String key) {
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			jedis.select(DATABASE);
			Boolean result = jedis.exists(key);
			return result;
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}

	@Override
	public Long expire(String key, long seconds) {
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			jedis.select(DATABASE);
			Long result = jedis.expire(key, seconds);
			return result;
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}

	@Override
	public Long ttl(String key) {
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			jedis.select(DATABASE);
			Long result = jedis.ttl(key);
			return result;
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}

	@Override
	public Long incr(String key) {
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			jedis.select(DATABASE);
			Long result = jedis.incr(key);
			return result;
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}

	@Override
	public Long hset(String key, String field, String value) {
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			jedis.select(DATABASE);
			Long result = jedis.hset(key, field, value);
			return result;
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}

	@Override
	public String hget(String key, String field) {
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			jedis.select(DATABASE);
			String result = jedis.hget(key, field);
			return result;
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}

	@Override
	public Long hdel(String key, String... field) {
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			jedis.select(DATABASE);
			Long result = jedis.hdel(key, field);
			return result;
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	@Override
	public Long del(String key) {
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			jedis.select(DATABASE);
			Long result = jedis.del(key);
			return result;
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	@Override
	public String setex(byte[] key, long seconds, byte[] value) {
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			jedis.select(DATABASE);
			String result = jedis.setex(key,seconds, value);
			return result;
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	@Override
	public String set(byte[] key, byte[] value) {
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			jedis.select(DATABASE);
			String result = jedis.set(key, value);
			return result;
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	@Override
	public byte[] get(byte[] key) {
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			jedis.select(DATABASE);
			byte[] result = jedis.get(key);
			return result;
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	@Override
	public Long del(byte[] key) {
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			jedis.select(DATABASE);
			Long result = jedis.del(key);
			return result;
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}

	@Override
	public Long lpush(byte[] key, byte[] value) {
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			jedis.select(DATABASE);
			Long result = jedis.lpush(key, value);
			return result;
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	@Override
	public byte[] rpop(byte[] key) {
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			jedis.select(DATABASE);
			byte[] result = jedis.rpop(key);
			return result;
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	@Override
	public Long incrBy(byte[] key, long by) {
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			jedis.select(DATABASE);
			Long result = jedis.incrBy(key,by);
			return result;
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}

	@Override
	public String set(String key, String value, SetParams setParams) {
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			jedis.select(DATABASE);
			String result = jedis.set(key, value, setParams);
			return result;
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}

	@Override
	public Object eval(String script, List<String> keys, List<String> args) {
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			jedis.select(DATABASE);
			Object result = jedis.eval(script, keys, args);
			return result;
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}

	@Override
	public Long llen(String key) {
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			jedis.select(DATABASE);
			Long result = jedis.llen(key);
			return result;
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}

	@Override
	public Long hincrBy(String key, String field, long by) {
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			jedis.select(DATABASE);
			Long result = jedis.hincrBy(key,field,by);
			return result;
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}

	@Override
	public Long expire(String key, long seconds, int database) {
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			jedis.select(database);
			Long result = jedis.expire(key, seconds);
			return result;
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}

	@Override
	public Long hincrBy(String key, String field, long by, int database) {
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			jedis.select(database);
			Long result = jedis.hincrBy(key,field,by);
			return result;
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	
}
