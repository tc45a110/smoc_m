package com.smoc.cloud.scheduler.tools.redis;

import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis简单的K-V操作示例
 */
public class RedisValueOperations {

    private RedisTemplate redisTemplate;

    /**
     * @param key
     * @param value
     * @return void
     * @description string设置 key和 value的值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * @param key
     * @param value
     * @param seconds
     * @param timeUnit
     * @return void
     * @description string设置 key和 value的值并设置过期时间和时间单位
     */
    public void setWithExpire(String key, Object value, Long seconds, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, seconds, timeUnit);
    }

    /**
     * @param * @param key
     * @return java.lang.Object
     * @description string获取 key对应的 value值
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * @param * @param key
     * @return boolean
     * @description 判断在 redis中是不是存在对应的 key值，有的话就返回 true，没有就返回 false
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * @param key
     * @return boolean
     * @description 删除redis中对应的key值
     */
    public Boolean del(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * @param keys
     * @return long
     * @description 批量删除 redis中对应的 key值，其中 keys是数组 keys:Collection<K> keys
     */
    public Long batchDel(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    /**
     * @param key
     * @return byte[]
     * @description 把 key值序列化成 byte[]类型
     */
    public byte[] dump(String key) {
        return redisTemplate.dump(key);
    }

    /**
     * @param *       @param key
     * @param seconds
     * @return void
     * @description 对传入的 key值设置过期时间
     */
    public Boolean expire(String key, long seconds) {
        return redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    /**
     * @param *    @param key
     * @param date
     * @return boolean
     * @description 对传入的 key值设置过期日期
     */
    public Boolean expireAt(String key, Date date) {
        return redisTemplate.expireAt(key, date);
    }

    /**
     * @param * @param key
     * @return java.util.Set<java.lang.String>
     * @description 模糊查询，返回一个没有重复的 Set类型
     */
    public Set<String> getStringKeys(String key) {
        return redisTemplate.keys(key);
    }

    /**
     * @param oldKey
     * @param newKey
     * @return void
     * @description 根据新的 key的名称修改 redis中老的 key的名称
     */
    public void rename(String oldKey, String newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    /**
     * @param oldKey
     * @param newKey
     * @return boolean
     * @description 重命名旧的 key值
     */
    public Boolean renameIfAbsent(String oldKey, String newKey) {
        return redisTemplate.renameIfAbsent(oldKey, newKey);
    }

    /**
     * @param key
     * @return org.springframework.data.redis.connection.DataType
     * @description 获取 key值的类型
     */
    public DataType type(String key) {
        return redisTemplate.type(key);
    }

    /**
     * @param
     * @return java.lang.String
     * @description 随机从 redis中获取一个 key
     */
    public Object randomKey() {
        return redisTemplate.randomKey();
    }

    /**
     * @param key
     * @return void
     * @description 获取当前 key的剩下的过期时间
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * @param key
     * @param timeUnit
     * @return java.lang.Long
     * @description 获取 key剩余的过期时间，同时设置时间单位
     */
    public Long getExpire(String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    /**
     * @param key
     * @return java.lang.Boolean
     * @description 将 key进行持久化
     */
    public Boolean persist(String key) {
        return redisTemplate.persist(key);
    }

    /**
     * @param key
     * @param dbIndex
     * @return java.lang.Boolean
     * @description 将当前数据库的 key移动到指定 redis中数据库当中
     */
    public Boolean move(String key, int dbIndex) {
        return redisTemplate.move(key, dbIndex);
    }

    /**
     * @param key
     * @param start
     * @param end
     * @return java.lang.String
     * @description 截取 key的子字符串
     */
    public String subString(String key, long start, long end) {
        return redisTemplate.opsForValue().get(key, start, end);
    }

    /**
     * @param key
     * @param value
     * @return java.lang.Object
     * @description 设置 key跟 value的值，同时获取 key的值
     */
    public Object getAndSet(String key, Object value) {
        return redisTemplate.opsForValue().getAndSet(key, value);
    }

    /**
     * @param keys
     * @return java.util.List<java.lang.Object>
     * @description 设置多个 key跟 value的值，同时获取 key的值
     */
    public List<Object> multiGetSet(List<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * @param *     @param key
     * @param value
     * @return java.lang.Integer
     * @description 获取原来的 key的值后在后面新增上新的字符串
     */
    public Integer append(String key, String value) {
        return redisTemplate.opsForValue().append(key, value);
    }

    /**
     * @param * @param key
     * @return java.lang.Long
     * @description value 值 +1
     */
    public Long increment(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    /**
     * @param key
     * @param increment
     * @return java.lang.Long
     * @description 增量方式增加或减少 long值
     */
    public Long incrementLong(String key, long increment) {
        return redisTemplate.opsForValue().increment(key, increment);
    }

    /**
     * @param key
     * @param increment
     * @return void
     * @description 增量方式增加double值
     */
    public void incrementDouble(String key, double increment) {
        redisTemplate.opsForValue().increment(key, increment);
    }

    /**
     * @param * @param map
     * @return java.lang.Boolean
     * @description 不存在即新增map的操作
     */
    public Boolean multiSetIfAbsent(Map<? extends String, ?> map) {
        return redisTemplate.opsForValue().multiSetIfAbsent(map);
    }

    /**
     * @param * @param map
     * @return void
     * @description 保存 map集合
     */
    public void multiSet(Map<String, String> map) {
        redisTemplate.opsForValue().multiSet(map);
    }

    /**
     * @param * @param keys
     * @return java.util.List<java.lang.Object>
     * @description 获取 map集合
     */
    public List<Object> multiGet(List<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * @param key
     * @return java.lang.Long
     * @description 获取指定 key的字符串的长度
     */
    public Long sizeString(String key) {
        return redisTemplate.opsForValue().size(key);
    }

    /**
     * @param key
     * @param value
     * @param offset
     * @return void
     * @description 根据偏移量 offset 的值，覆盖重写 value的值
     */
    public void offsetValue(String key, Object value, Long offset) {
        redisTemplate.opsForValue().set(key, value, offset);
    }

    /**
     * @param key
     * @param offset
     * @return java.lang.Boolean
     * @description 对 key所储存的字符串值，获取指定偏移量上的位(bit)
     */
    public Boolean getOffsetValue(String key, Long offset) {
        return redisTemplate.opsForValue().getBit(key, offset);
    }

    /**
     * @param key
     * @param value
     * @return java.lang.Boolean
     * @description 重新设置 key对应的值，如果存在返回 false，否则返回 true
     */
    public Boolean setIfAbsent(String key, Object value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }


}
