package com.smoc.cloud.redis;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Redis 针对map类型的数据操作
 */
public class RedisHashOperations {

    private RedisTemplate redisTemplate;

    /**
     * @description 新增map值
     * @param key
     * @param field
     * @param value
     * @return void
     */
    public void put(String key, Object field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    /**
     * @description 以map集合的形式添加键值对
     * @param key
     * @param map
     * @return void
     */
    public void putAll(String key, Map<Object, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * @description 获取 map中指定的 key值，如果存在则返回值，没有就返回null
     * @param key
     * @param field
     * @return java.lang.Object
     */
    public Object getMapValue(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    /**
     * @description 根据 key获取 Map对象
     * @param  * @param key
     * @return java.util.Map<java.lang.Object,java.lang.Object>
     */
    public Map<Object, Object> getMap(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * @description 当 hashKey不存在的时候，进行设置 map的值
     * @param key
     * @param hashKey
     * @param value
     * @return java.lang.Boolean
     */
    public Boolean putIfAbsent(String key, Object hashKey, Object value) {
        return redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
    }

    /**
     * @description 删除多个map的字段
     * @param key
     * @param fields
     * @return java.lang.Long
     */
    public Long del(String key, List<Object> fields) {
        return redisTemplate.opsForHash().delete(key, fields);
    }

    /**
     * @description 查看 hash表中指定字段是否存在
     * @param  * @param key
     * @param field
     * @return java.lang.Boolean
     */
    public Boolean hasKey(String key, Object field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    /**
     * @description 给 map中指定字段的整数值加上 long型增量 increment
     * @param key
     * @param field
     * @param increment
     * @return java.lang.Long
     */
    public Long incrementLong(String key, Object field, long increment) {
        return redisTemplate.opsForHash().increment(key, field, increment);
    }

    /**
     * @description 给 map中指定字段的整数值加上 double型增量 increment
     * @param key
     * @param field
     * @param increment
     * @return java.lang.Double
     */
    public Double incrementDouble(String key, Object field, double increment) {
        return redisTemplate.opsForHash().increment(key, field, increment);
    }

    /**
     * @description 获取 map中的所有字段
     * @param key
     * @return java.util.Set<java.lang.Object>
     */
    public Set<Object> keys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * @description 获取 map中所有字段的数量
     * @param key
     * @return java.util.Set<java.lang.Object>
     */
    public Long sizeHash(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    /**
     * @description 获取hash表中存在的所有的值
     * @param key
     * @return java.util.List<java.lang.Object>
     */
    public List<Object> values(String key) {
        return redisTemplate.opsForHash().values(key);
    }

    /**
     * @description 查看匹配的键值对
     * @param key
     * @param scanOptions
     * @return org.springframework.data.redis.core.Cursor<java.util.Map.Entry<java.lang.Object,java.lang.Object>>
     */
    public Cursor<Map.Entry<Object, Object>> scan(String key, ScanOptions scanOptions) {
        return redisTemplate.opsForHash().scan(key, scanOptions);
    }


}
