/**
 * @desc
 * 
 */
package com.base.common.cache.jedis;

import java.util.Collections;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.SerializationUtils;

import com.base.common.cache.CacheServiceInter;
import com.base.common.util.Commons;

import redis.clients.jedis.params.SetParams;

public class JedisService implements CacheServiceInter{

	private static Logger logger = Logger.getLogger(JedisService.class);
	
	private JedisClient jedisClient;
	
	@SuppressWarnings("resource")
	public JedisService() {
		ApplicationContext ac = new ClassPathXmlApplicationContext(
				"jedis-config.xml");
		jedisClient = (JedisClient) ac.getBean("jedisClientPool");
		logger.info("redis 客户端初始化完成!");
	}
	
	@SuppressWarnings("resource")
	public JedisService(String jedisClientName) {
		ApplicationContext ac = new ClassPathXmlApplicationContext(
				"jedis-config.xml");
		jedisClient = (JedisClient) ac.getBean(jedisClientName);
		logger.info("redis 客户端初始化完成!");
	}
	
	@Override
	public void putString(String key, int timeout, String str) {
		jedisClient.setex(key,timeout, str);
		
	}

	@Override
	public void putString(String key, String str) {
		jedisClient.set(key, str);
		
	}

	@Override
	public String getString(String key) {
		return jedisClient.get(key);
	}

	@Override
	public void delString(String key) {
		jedisClient.del(key);
		
	}

	@Override
	public void putObject(String key, int timeout, Object object) {
		jedisClient.setex(toByteArray(key), timeout, serialize(object));
		
	}

	@Override
	public void putObject(String key, Object object) {
		jedisClient.set(toByteArray(key), serialize(object));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getObject(String key, Class<T> type) {
		return (T) SerializationUtils.deserialize(jedisClient.get(key.getBytes()));
	}

	@Override
	public void delObject(String key) {
		jedisClient.del(toByteArray(key));
		
	}

	@Override
	public void pushQueue(String key, Object object) {
		jedisClient.lpush(toByteArray(key), serialize(object));
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T popQueue(String key, Class<T> type) {
		return (T) SerializationUtils.deserialize(jedisClient.rpop(key.getBytes()));
	}

	@Override
	public boolean isOverFlow(String key, int timeout, int times, int by) {
		try {
			String value = jedisClient.get(key);
			if (value == null) {
				jedisClient.incrBy(key.getBytes(), by);
				jedisClient.expire(key, timeout);
				return false;
			} else {
				int intvalue = Integer.parseInt(value);
				if (intvalue + by <= times) {
					jedisClient.incrBy(key.getBytes(), by);
					jedisClient.expire(key, timeout);
					return false;
				}
				return true;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return false;
	}

	@Override
	public boolean lock(String key, String requestId, int timeout) {
		SetParams setParams = SetParams.setParams();
		setParams.nx();
		setParams.ex(timeout);
		String result = jedisClient.set(key, requestId, setParams);
		return Commons.LOCK_SUCCESS.equalsIgnoreCase(result);
	}

	@Override
	public boolean unlock(String key, String requestId) {
		String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
		Object result = jedisClient.eval(script, Collections.singletonList(key), Collections.singletonList(requestId));
		return Commons.RELEASE_SUCCESS.equals(result);
	}

	@Override
	public void increase(String key, int timeout, int by) {
		try {
			jedisClient.incrBy(key.getBytes(), by);
			jedisClient.expire(key, timeout);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
	}
	
	private byte[] serialize(Object object) {
		return SerializationUtils.serialize(object);
	}

	private byte[] toByteArray(String str) {
		return str.getBytes();
	}

	@Override
	public long getQueueSize(String key) {
		return jedisClient.llen(key);
	}

	@Override
	public void increase(String key, String field, int timeout, int by) {
		try {
			jedisClient.hincrBy(key,field, by);
			jedisClient.expire(key, timeout);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

	public String getHashValue(String key, String field) {
		return jedisClient.hget(key, field);
	}

	@Override
	public void putHashString(String key, int timeout, String field,
			String value) {
		jedisClient.hset(key, field, value);
		jedisClient.expire(key, timeout);
	}
	
}


