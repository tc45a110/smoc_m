package com.smoc.cloud.tools.redis;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Redis 针对list类型的数据操作
 */
public class RedisListOperations {

    private RedisTemplate redisTemplate;

    /**
     * @description 把值添加在 list的最前面
     * @param key
     * @param value
     * @return java.lang.Long
     */
    public Long leftPush(String key, Object value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * @description 把值添加在 list
     * @param key
     * @param values
     * @return java.lang.Long
     */
    public Long leftPush(String key, Object... values) {
        return redisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * @description 直接把一个新的 list添加到老的 list上面去
     * @param key
     * @param value
     * @return java.lang.Long
     */
    public Long leftPushAll(String key, List<Object> value) {
        return redisTemplate.opsForList().leftPushAll(key, value);
    }

    /**
     * @description List存在的时候就加入新的值
     * @param key
     * @param value
     * @return long
     */
    public long leftPushIfPresent(String key, Object value) {
        return redisTemplate.opsForList().leftPushIfPresent(key, value);
    }

    /**
     * @description 把值添加在 list的最后面
     * @param key
     * @param value
     * @return java.lang.Long
     */
    public long rightPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * @description 把值添加在 list的最后面
     * @param key
     * @param values
     * @return long
     */
    public long rightPushAll(String key, Object... values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * @description 把值添加在 list的最后面
     * @param key
     * @param values
     * @return long
     */
    public long rightPushAll(String key, List<Object> values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * @description 根据索引获取 list中的值
     * @param key
     * @param index
     * @return java.lang.Object
     */
    public Object index(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * @description 获取 list中开始索引到结束索引的所有值
     * @param key
     * @param start
     * @param end
     * @return java.util.List<java.lang.Object>
     */
    public List<Object> range(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * @description 移除并获取列表中第一个元素
     * @param key
     * @return java.lang.Object
     */
    public Object leftPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * @description 移除并获取列表中第一个元素
     * @param key
     * @param seconds
     * @param timeUnit
     * @return java.lang.Object
     */
    public Object leftPop(String key, Long seconds, TimeUnit timeUnit) {
        return redisTemplate.opsForList().leftPop(key, seconds, timeUnit);
    }

    /**
     * @description 移除并获取列表中最后一个元素
     * @param key
     * @return java.lang.Object
     */
    public Object rightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * @description 移除并获取列表中最后一个元素
     * @param key
     * @param seconds
     * @param timeUnit
     * @return java.lang.Object
     */
    public Object rightPop(String key, Long seconds, TimeUnit timeUnit) {
        return redisTemplate.opsForList().rightPop(key, seconds, timeUnit);
    }

    /**
     * @description 从一个队列的右边弹出一个元素并将这个元素放入另一个指定队列的最左边
     * @param sourceKey
     * @param destinationKey
     * @return java.lang.Object
     */
    public Object rightPopAndLeftPush(String sourceKey, String destinationKey) {
        return redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey);
    }

    /**
     * @description 从一个队列的右边弹出一个元素并将这个元素放入另一个指定队列的最左边
     * @param sourceKey
     * @param destinationKey
     * @param seconds
     * @param timeUnit
     * @return java.lang.Object
     */
    public Object rightPopAndLeftPush(String sourceKey, String destinationKey, long seconds, TimeUnit timeUnit) {
        return redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey, seconds, timeUnit);
    }

    /**
     * @description 获取 list的大小
     * @param key
     * @return java.lang.Long
     */
    public Long sizeList(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * @description 剪裁 List列表
     * @param key
     * @param start
     * @param end
     * @return void
     */
    public void trim(String key, long start, long end) {
        redisTemplate.opsForList().trim(key, start, end);
    }

    /**
     * @description 删除集合中值等于value的元素
     * @param key
     * @param index
     * @param value
     * @return java.lang.Long
     */
    public Long remove(String key, long index, Object value) {
        return redisTemplate.opsForList().remove(key, index, value);
    }


}
