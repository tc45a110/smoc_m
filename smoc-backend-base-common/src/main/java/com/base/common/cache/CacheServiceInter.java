/**
 * @desc
 * 对象存储的key
 */
package com.base.common.cache;


public interface CacheServiceInter {
	 /**
	  * 存放带有效期字符串
	  * @param key
	  * @param timeout
	  * @param str
	  */
	 public void putString(String key, int timeout,String str,String redisName);
	 
	 /**
	  * 存储永久字符串
	  * @param key
	  * @param str
	  */
	 public void putString(String key,String str,String redisName);
	 
	 /**
	  * 存储携号转网运营商
	  * @param key
	  * @param str
	  */
	 public void putMnpString(String key,String str,String redisName);
	 
	 /**
	  * 获取字符串
	  * @param key
	  */
	 public String getString(String key,String redisName);
	 
	 /**
	  * 获取携号转网运营商
	  * @param key
	  */
	 public String getMnpString(String key,String redisName);
	 
	 /**
	  * 删除一个字符串
	  * @param key
	  */
	 public void delString(String key,String redisName);
	 
	 /**
	  * 存序列化对象,带有效期
	  * @param key
	  * @param timeout
	  * @param object
	  */
	 public void putObject(String key, int timeout, Object object,String redisName);
	 
	 /**
	  * 存序列化对象
	  * @param key
	  * @param object
	  */
	 public void putObject(String key, Object object,String redisName);
	 
	 /**
	  * 获取反序列化对象
	  * @param key
	  * @param type
	  * @return
	  */
	 public <T> T getObject(String key, Class<T> type,String redisName);
	 
	 /**
	  * 删除对象
	  * @param key
	  */
	 public void delObject(String key,String redisName);
	 
	 /**
	  * 队列存序列化对象
	  * @param key
	  * @param object
	  */
	 public void pushQueue(String key,Object object,String redisName);
	 
	 /**
	  * 移除列表最后一个元素
	  * @param key
	  * @param type
	  * @return
	  */
	 public <T> T popQueue(String key, Class<T> type,String redisName);
	 
	 /**
	  * 用于控制次数
	  * @param key
	  * @param field
	  * @param timeout
	  * @param times
	  * @param by
	  * @return
	  */
	 public boolean isOverFlow(String key, String field,int timeout, int times, int by,String redisName);
	 
	 /**
	  * 获取分布式锁
	  * @param key
	  * @param requestId
	  * @param timeout 单位秒
	  * @return
	  */
	 public boolean lock(String key,String requestId,int timeout,String redisName);
	 
	 /**
	  * 释放分布式锁
	  * @param key
	  * @param requestId
	  * @return
	  */
	 public boolean unlock(String key,String requestId,String redisName);
	 
	 /**
	  * 获取一个队列元素数量
	  * @param key
	  * @return
	  */
	 public long getQueueSize(String key,String redisName);
	 
	 /**
	  * 在一个hash中增加一个field的值
	  * @param key hash的key
	  * @param field
	  * @param timeout
	  * @param by
	  */
	 public void increase(String key, String field,int timeout,int by,String redisName);
	 
	 /**
	  * 在一个hash中增加一个field的值,指定database
	  * @param key
	  * @param field
	  * @param timeout
	  * @param by
	  * @param database
	  */
	 public void increase(String key, String field,int timeout,int by,int database,String redisName);
	 
	 /**
	  * 在hash中设置一个field的值
	  * @param key
	  * @param timeout
	  * @param field
	  * @param value
	  * @return
	  */
	 public void putHashString(String key, int timeout, String field,String value,String redisName);
	 
	 /**
	  * 从一个hash中获取属性值
	  * @param key
	  * @param field
	  * @return
	  */
	 public String getHashValue(String key, String field,String redisName);

	/**
	 * 从一个hash删除属性值
	 * @param key
	 * @param field
	 * @return
	 */
	 public void delHashString(String key, String field,String redisName);
	 
	 /**
	  * 获取连接池重要信息
	  * @return
	  */
	 public String getPoolString(String redisName);
	 
}


