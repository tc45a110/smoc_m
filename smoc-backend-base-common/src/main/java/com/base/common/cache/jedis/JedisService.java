/**
 * @desc
 * 
 */
package com.base.common.cache.jedis;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.SerializationUtils;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import com.base.common.cache.CacheServiceInter;
import com.base.common.manager.AlarmManager;
import com.base.common.util.Commons;
import com.base.common.vo.AlarmMessage;
import com.base.common.vo.AlarmMessage.AlarmKey;

public class JedisService implements CacheServiceInter{

	private static final Logger logger = LoggerFactory.getLogger(JedisService.class);
	
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
		
		logger.info("{} redis客户端初始化完成!",jedisClientName);
	}
	
	@Override
	public void putString(String key, int timeout, String str,String redisName) {
		try {
			jedisClient.setex(key,timeout, str);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			AlarmManager.getInstance().process(new AlarmMessage(AlarmKey.Redis, redisName+":"+e.getMessage()));
		}	
	}

	@Override
	public void putString(String key, String str,String redisName) {
		try {
			jedisClient.set(key, str);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			AlarmManager.getInstance().process(new AlarmMessage(AlarmKey.Redis, redisName+":"+e.getMessage()));
		}
	}
	

	@Override
	public void putMnpString(String key, String str, String redisName) {
		try {
			jedisClient.setMnp(key, str);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			AlarmManager.getInstance().process(new AlarmMessage(AlarmKey.Redis, redisName+":"+e.getMessage()));
		}
	}

	@Override
	public String getMnpString(String key, String redisName) {
		try {
			return jedisClient.getMnp(key);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			AlarmManager.getInstance().process(new AlarmMessage(AlarmKey.Redis, redisName+":"+e.getMessage()));
		}
		return null;
	}

	@Override
	public String getString(String key,String redisName) {
		try {
			return jedisClient.get(key);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			AlarmManager.getInstance().process(new AlarmMessage(AlarmKey.Redis, redisName+":"+e.getMessage()));
		}
		return null;
	}

	@Override
	public void delString(String key,String redisName) {
		try {
			jedisClient.del(key);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			AlarmManager.getInstance().process(new AlarmMessage(AlarmKey.Redis, redisName+":"+e.getMessage()));
		}
	}

	@Override
	public void putObject(String key, int timeout, Object object,String redisName) {
		try {
			jedisClient.setex(toByteArray(key), timeout, serialize(object));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			AlarmManager.getInstance().process(new AlarmMessage(AlarmKey.Redis, redisName+":"+e.getMessage()));
		}
	}

	@Override
	public void putObject(String key, Object object,String redisName) {
		try {
			jedisClient.set(toByteArray(key), serialize(object));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			AlarmManager.getInstance().process(new AlarmMessage(AlarmKey.Redis, redisName+":"+e.getMessage()));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getObject(String key, Class<T> type,String redisName) {
		try {
			return (T) SerializationUtils.deserialize(jedisClient.get(key.getBytes()));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			AlarmManager.getInstance().process(new AlarmMessage(AlarmKey.Redis, redisName+":"+e.getMessage()));
		}
		return null;
	}

	@Override
	public void delObject(String key,String redisName) {
		try {
			jedisClient.del(toByteArray(key));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			AlarmManager.getInstance().process(new AlarmMessage(AlarmKey.Redis, redisName+":"+e.getMessage()));
		}
	}

	@Override
	public void pushQueue(String key, Object object,String redisName) {
		try {
			jedisClient.lpush(toByteArray(key), serialize(object));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			AlarmManager.getInstance().process(new AlarmMessage(AlarmKey.Redis, redisName+":"+e.getMessage()));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T popQueue(String key, Class<T> type,String redisName) {
		try {
			return (T) SerializationUtils.deserialize(jedisClient.rpop(key.getBytes()));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			AlarmManager.getInstance().process(new AlarmMessage(AlarmKey.Redis, redisName+":"+e.getMessage()));
		}
		return null;
	}

	@Override
	public boolean isOverFlow(String key, String field, int timeout, int times, int by,String redisName) {
		try {
			String value = jedisClient.hget(key, field);
			if (value == null) {
				jedisClient.hincrBy(key,field, by);
				jedisClient.expire(key, timeout);
				return false;
			} else {
				int intvalue = Integer.parseInt(value);
				if (intvalue + by <= times) {
					jedisClient.hincrBy(key,field, by);
					jedisClient.expire(key, timeout);
					return false;
				}
				return true;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			AlarmManager.getInstance().process(new AlarmMessage(AlarmKey.Redis, redisName+":"+e.getMessage()));
		}
		return false;
	}

	@Override
	public boolean lock(String key, String requestId, int timeout,String redisName) {
		try {
			SetParams setParams = SetParams.setParams();
			setParams.nx();
			setParams.ex(timeout);
			String result = jedisClient.set(key, requestId, setParams);
			return Commons.LOCK_SUCCESS.equalsIgnoreCase(result);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			AlarmManager.getInstance().process(new AlarmMessage(AlarmKey.Redis, redisName+":"+e.getMessage()));
		}
		return false;
	}

	@Override
	public boolean unlock(String key, String requestId,String redisName) {
		try {
			String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
			Object result = jedisClient.eval(script, Collections.singletonList(key), Collections.singletonList(requestId));
			return Commons.RELEASE_SUCCESS.equals(result);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			AlarmManager.getInstance().process(new AlarmMessage(AlarmKey.Redis, redisName+":"+e.getMessage()));
		}
		return false;
	}
	
	private byte[] serialize(Object object) {
		try {
			return SerializationUtils.serialize(object);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			AlarmManager.getInstance().process(new AlarmMessage(AlarmKey.Redis, e.getMessage()));
		}
		return null;
	}

	private byte[] toByteArray(String str) {
		try {
			return str.getBytes();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			AlarmManager.getInstance().process(new AlarmMessage(AlarmKey.Redis, e.getMessage()));
		}
		return null;
	}

	@Override
	public long getQueueSize(String key,String redisName) {
		try {
			return jedisClient.llen(key);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			AlarmManager.getInstance().process(new AlarmMessage(AlarmKey.Redis, redisName+":"+e.getMessage()));
		}
		return 0;
	}

	@Override
	public void increase(String key, String field, int timeout, int by,String redisName) {
		try {
			jedisClient.hincrBy(key,field, by);
			jedisClient.expire(key, timeout);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			AlarmManager.getInstance().process(new AlarmMessage(AlarmKey.Redis, redisName+":"+e.getMessage()));
		}
	}

	@Override
	public void increase(String key, String field, int timeout, int by,
			int database,String redisName) {
		try {
			jedisClient.hincrBy(key,field, by,database);
			jedisClient.expire(key, timeout,database);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			AlarmManager.getInstance().process(new AlarmMessage(AlarmKey.Redis, redisName+":"+e.getMessage()));
		}
	}

	public String getHashValue(String key, String field,String redisName) {
		try {
			return jedisClient.hget(key, field);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			AlarmManager.getInstance().process(new AlarmMessage(AlarmKey.Redis, redisName+":"+e.getMessage()));
		}
		return null;
	}

	@Override
	public void putHashString(String key, int timeout, String field,
			String value,String redisName) {
		try {
			jedisClient.hset(key, field, value);
			jedisClient.expire(key, timeout);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			AlarmManager.getInstance().process(new AlarmMessage(AlarmKey.Redis, redisName+":"+e.getMessage()));
		}
	}

	@Override
	public void delHashString(String key, String field,String redisName) {
		try {
			jedisClient.hdel(key, field);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			AlarmManager.getInstance().process(new AlarmMessage(AlarmKey.Redis, redisName+":"+e.getMessage()));
		}
	}
	
	public String getPoolString(String redisName){
		try {
			JedisPool jedisPool = ((JedisClientPool)jedisClient).getJedisPool();
			
			StringBuilder sb = new StringBuilder();
			sb.append(redisName);
			sb.append(":");
			sb.append("NumActive:");
			sb.append(jedisPool.getNumActive());
			sb.append(",");
			sb.append("NumIdle:");
			sb.append(jedisPool.getNumIdle());
			sb.append(",");
			sb.append("NumWaiters:");
			sb.append(jedisPool.getNumWaiters());
			sb.append(",");
			sb.append("CreatedCount:");
			sb.append(jedisPool.getCreatedCount());
			sb.append(",");
			sb.append("DestroyedCount:");
			sb.append(jedisPool.getDestroyedCount());
			sb.append(",");
			sb.append("ReturnedCount:");
			sb.append(jedisPool.getReturnedCount());

			return sb.toString();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			AlarmManager.getInstance().process(new AlarmMessage(AlarmKey.Redis, redisName+":"+e.getMessage()));
		}
		return null;
	}
}


