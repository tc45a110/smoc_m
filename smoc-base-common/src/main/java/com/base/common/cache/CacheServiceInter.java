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
	 public void putString(String key, int timeout, String str);
	 
	 /**
	  * 存储永久字符串
	  * @param key
	  * @param str
	  */
	 public void putString(String key, String str);
	 
	 /**
	  * 获取字符串
	  * @param key
	  */
	 public String getString(String key);
	 
	 /**
	  * 删除一个字符串
	  * @param key
	  */
	 public void delString(String key);
	 
	 /**
	  * 存序列化对象,带有效期
	  * @param key
	  * @param timeout
	  * @param object
	  */
	 public void putObject(String key, int timeout, Object object);
	 
	 /**
	  * 存序列化对象
	  * @param key
	  * @param object
	  */
	 public void putObject(String key, Object object);
	 
	 /**
	  * 获取反序列化对象
	  * @param key
	  * @param type
	  * @return
	  */
	 public <T> T getObject(String key, Class<T> type);
	 
	 /**
	  * 删除对象
	  * @param key
	  */
	 public void delObject(String key);
	 
	 /**
	  * 队列存序列化对象
	  * @param key
	  * @param object
	  */
	 public void pushQueue(String key, Object object);
	 
	 /**
	  * 移除列表最后一个元素
	  * @param key
	  * @param type
	  * @return
	  */
	 public <T> T popQueue(String key, Class<T> type);
	 
	 /**
	  * 用于控制次数
	  * @param key
	  * @param timeout
	  * @param times
	  * @param by
	  * @return
	  */
	 public boolean isOverFlow(String key, int timeout, int times, int by);
	 
	 /**
	  * 获取分布式锁
	  * @param key
	  * @param requestId
	  * @param timeout 单位秒
	  * @return
	  */
	 public boolean lock(String key, String requestId, int timeout);
	 
	 /**
	  * 释放分布式锁
	  * @param key
	  * @param requestId
	  * @return
	  */
	 public boolean unlock(String key, String requestId);
	 
	 /**
	  * 增加一个值
	  * @param key
	  * @param timeout
	  * @param by
	  */
	 public void increase(String key, int timeout, int by);
	 
	 /**
	  * 获取一个队列元素数量
	  * @param key
	  * @return
	  */
	 public long getQueueSize(String key);
	 
	 /**
	  * 在一个hash中增加一个field的值
	  * @param key hash的key
	  * @param field
	  * @param timeout
	  * @param by
	  */
	 public void increase(String key, String field, int timeout, int by);
	 
	 /**
	  * 在hash中设置一个field的值
	  * @param key
	  * @param timeout
	  * @param field
	  * @param value
	  * @return
	  */
	 public void putHashString(String key, int timeout, String field, String value);
	 
	 /**
	  * 从一个hash中获取属性值
	  * @param key
	  * @param field
	  * @return
	  */
	 public String getHashValue(String key, String field);
	 
}


