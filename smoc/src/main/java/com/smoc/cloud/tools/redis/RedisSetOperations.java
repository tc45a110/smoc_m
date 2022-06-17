package com.smoc.cloud.tools.redis;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Redis set类型数据操作
 */
public class RedisSetOperations {

    @Resource(name = "defaultRedisTemplate")
    private RedisTemplate redisTemplate;

    /**
     * @description 添加元素到 set中
     * @param key
     * @param values
     * @return java.lang.Long
     */
    public Long add(String key, Collection<Object> values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * @description 从 set中删除一个随机元素，并返回该元素
     * @param key
     * @return java.lang.Object
     */
    public Object pop(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    /**
     * @description 获取 set集合的大小
     * @param key
     * @return long
     */
    public Long sizeSet(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * @description 判断 set集合中是否存在value值
     * @param key
     * @param value
     * @return java.lang.Boolean
     */
    public Boolean isMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * @description 获取两个集合的交集并返回一个集合
     * @param key
     * @param otherKey
     * @return java.util.Set<java.lang.Object>
     */
    public Set<Object> intersect(String key, String otherKey) {
        return redisTemplate.opsForSet().intersect(key, otherKey);
    }

    /**
     * @description 获取两个集合的交集并返回一个集合
     * @param key
     * @param collection
     * @return java.util.Set<java.lang.Object>
     */
    public Set<Object> intersect(String key, Collection<String> collection) {
        return redisTemplate.opsForSet().intersect(key, collection);
    }

    /**
     * @description 获取两个集合交集，并存储到 destKey
     * @param key1
     * @param key2
     * @param destKey
     * @return java.lang.Long
     */
    public Long intersectAndStore(String key1, String key2, String destKey) {
        return redisTemplate.opsForSet().intersectAndStore(key1, key2, destKey);
    }

    /**
     * @description 获取两个集合的并集
     * @param key
     * @param key1
     * @return java.util.Set<java.lang.Object>
     */
    public Set<Object> union(String key, String key1) {
        return redisTemplate.opsForSet().union(key, key1);
    }

    /**
     * @description 获取两个集合的并集
     * @param key
     * @param collection
     * @return java.util.Set<java.lang.Object>
     */
    public Set<Object> union(String key, Collection<String> collection) {
        return redisTemplate.opsForSet().union(key, collection);
    }

    /**
     * @description 获取两个集合的并集，并存储到 destKey
     * @param key
     * @param key1
     * @param destKey
     * @return java.lang.Long
     */
    public Long unionAndStore(String key, String key1, String destKey) {
        return redisTemplate.opsForSet().unionAndStore(key, key1, destKey);
    }

    /**
     * @description 获取两个集合的差集
     * @param key
     * @param key1
     * @return java.util.Set<java.lang.Object>
     */
    public Set<Object> difference(String key, String key1) {
        return redisTemplate.opsForSet().difference(key, key1);
    }

    /**
     * @description 获取两个集合的差集
     * @param key
     * @param collection
     * @return java.util.Set<java.lang.Object>
     */
    public Set<Object> difference(String key, Collection<String> collection) {
        return redisTemplate.opsForSet().difference(key, collection);
    }

    /**
     * @description 获取两个集合的差集，并存储到 destKey
     * @param key
     * @param key1
     * @param destKey
     * @return java.lang.Long
     */
    public Long differenceAndStore(String key, String key1, String destKey) {
        return redisTemplate.opsForSet().differenceAndStore(key, key1, destKey);
    }

    /**
     * @description 获取集合中的所有元素
     * @param key
     * @return java.util.Set<java.lang.Object>
     */
    public Set<Object> members(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * @description 随机获取集合中一个元素
     * @param key
     * @return java.lang.Object
     */
    public Object randomMember(String key) {
        return redisTemplate.opsForSet().randomMember(key);
    }

    /**
     * @description 随机获取集合中 count个元素，返回一个 List集合
     * @param key
     * @param count
     * @return java.util.List<java.lang.Object>
     */
    public List<Object> randomMembers(String key, long count) {
        return redisTemplate.opsForSet().randomMembers(key, count);
    }

    /**
     * @description 随机获取集合中 count个元素，去重后返回一个 Set集合
     * @param key
     * @param count
     * @return java.util.Set<java.lang.Object>
     */
    public Set<Object> distinctRandomMembers(String key, long count) {
        return redisTemplate.opsForSet().distinctRandomMembers(key, count);
    }

    /**
     * @description 遍历 set
     * @param key
     * @param scanOptions
     * @return org.springframework.data.redis.core.Cursor<java.lang.Object>
     */
    public Cursor<Object> scanSet(String key, ScanOptions scanOptions) {
        return redisTemplate.opsForSet().scan(key, scanOptions);
    }

    /**
     * @description 移除元素
     * @param key
     * @param objects
     * @return java.lang.Long
     */
    public Long remove(String key, Collection<Object> objects) {
        return redisTemplate.opsForSet().remove(key, objects);
    }


}
