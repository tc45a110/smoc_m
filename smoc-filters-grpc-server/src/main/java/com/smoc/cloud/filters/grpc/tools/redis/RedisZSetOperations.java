package com.smoc.cloud.filters.grpc.tools.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Collection;
import java.util.Set;

/**
 * Redis zset类型数据操作
 */
public class RedisZSetOperations {

    private RedisTemplate redisTemplate;

    /**
     * @param key
     * @param value
     * @param score
     * @return java.lang.Boolean
     * @description 添加元素到 zset，从小到大排序
     * @author chengjunyu
     * @date 2022/2/12
     */
    public Boolean add(String key, Object value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * @param key
     * @param value
     * @param score
     * @return java.lang.Double
     * @description 增加元素的 score值同时返回增加后的 score值
     * @author chengjunyu
     * @date 2022/2/12
     */
    public Double incrementScore(String key, Object value, double score) {
        return redisTemplate.opsForZSet().incrementScore(key, value, score);
    }

    /**
     * @param key
     * @param object
     * @return java.lang.Long
     * @description 返回 zset元素在集合的从小到大排名
     * @author chengjunyu
     * @date 2022/2/12
     */
    public Long rank(String key, Object object) {
        return redisTemplate.opsForZSet().rank(key, object);
    }

    /**
     * @param key
     * @param object
     * @return java.lang.Long
     * @description 返回 zset元素在集合的由大到小排名
     * @author chengjunyu
     * @date 2022/2/12
     */
    public Long reverseRank(String key, Object object) {
        return redisTemplate.opsForZSet().reverseRank(key, object);
    }

    /**
     * @param key
     * @param start
     * @param end
     * @return java.util.Set<org.springframework.data.redis.core.ZSetOperations.TypedTuple < java.lang.Object>>
     * @description 获取 zset集合中指定区间的元素
     * @author chengjunyu
     * @date 2022/2/12
     */
    public Set<ZSetOperations.TypedTuple<Object>> reverseRangeWithScores(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
    }

    /**
     * @param key
     * @param min
     * @param max
     * @return java.util.Set<java.lang.Object>
     * @description 查询 zset集合中的元素并从小到大排序
     * @author chengjunyu
     * @date 2022/2/12
     */
    public Set<Object> reverseRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
    }

    /**
     * @param key
     * @param min
     * @param max
     * @param start
     * @param end
     * @return java.util.Set<java.lang.Object>
     * @description 从高到低进行排序，然后获取最小与最大值之间的值
     * @author chengjunyu
     * @date 2022/2/12
     */
    public Set<Object> reverseRangeByScore(String key, double min, double max, long start, long end) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max, start, end);
    }

    /**
     * @param key
     * @param min
     * @param max
     * @return java.util.Set<org.springframework.data.redis.core.ZSetOperations.TypedTuple < java.lang.Object>>
     * @description 查询 zset集合中的元素并从小到大排序
     * @author chengjunyu
     * @date 2022/2/12
     */
    public Set<ZSetOperations.TypedTuple<Object>> reverseRangeByScoreWithScores(String key, double min, double max) {
        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max);
    }

    /**
     * @param key
     * @param min
     * @param max
     * @return java.lang.Long
     * @description 根据score值获取元素数量
     * @author chengjunyu
     * @date 2022/2/12
     */
    public Long count(String key, double min, double max) {
        return redisTemplate.opsForZSet().count(key, min, max);
    }

    /**
     * @param key
     * @return java.lang.Long
     * @description 获取 zset集合的大小
     * @author chengjunyu
     * @date 2022/2/12
     */
    public Long sizeZset(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    /**
     * @param key
     * @return java.lang.Long
     * @description 获取 zset集合的大小
     * @author chengjunyu
     * @date 2022/2/12
     */
    public Long zCard(String key) {
        return redisTemplate.opsForZSet().zCard(key);
    }

    /**
     * @param key
     * @param value
     * @return java.lang.Double
     * @description 获取集合中 key、value元素的 score值
     * @author chengjunyu
     * @date 2022/2/12
     */
    public Double score(String key, Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * @param key
     * @param start
     * @param end
     * @return java.lang.Long
     * @description 移除 zset中指定索引元素
     * @author chengjunyu
     * @date 2022/2/12
     */
    public Long removeRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    /**
     * @param key
     * @param min
     * @param max
     * @return java.lang.Long
     * @description 移除 zset中指定 score范围的集合成员
     * @author chengjunyu
     * @date 2022/2/12
     */
    public Long removeRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }

    /**
     * @param key
     * @param key1
     * @param destKey
     * @return java.lang.Long
     * @description 获取 key和 key1的并集并存储在 destKey中
     * @author chengjunyu
     * @date 2022/2/12
     */
    public Long zSetUnionAndStore(String key, String key1, String destKey) {
        return redisTemplate.opsForZSet().unionAndStore(key, key1, destKey);
    }

    /**
     * @param key
     * @param collection
     * @param destKey
     * @return java.lang.Long
     * @description 获取 key和 collection集合的并集并存储在 destKey中
     * @author chengjunyu
     * @date 2022/2/12
     */
    public Long zSetUnionAndStore(String key, Collection<String> collection, String destKey) {
        return redisTemplate.opsForZSet().unionAndStore(key, collection, destKey);
    }

    /**
     * @param key
     * @param key1
     * @param destKey
     * @return java.lang.Long
     * @description 获取 key和 key1的交集并存储在 destKey中
     * @author chengjunyu
     * @date 2022/2/12
     */
    public Long zSetIntersectAndStore(String key, String key1, String destKey) {
        return redisTemplate.opsForZSet().intersectAndStore(key, key1, destKey);
    }

    /**
     * @param key
     * @param collection
     * @param destKey
     * @return java.lang.Long
     * @description 获取 key和 collection集合的交集并存储在 destKey中
     * @author chengjunyu
     * @date 2022/2/12
     */
    public Long zSetIntersectAndStore(String key, Collection<String> collection, String destKey) {
        return redisTemplate.opsForZSet().intersectAndStore(key, collection, destKey);
    }

    /**
     * @param key
     * @param values
     * @return java.lang.Long
     * @description 删除多个values的值
     * @author chengjunyu
     * @date 2022/2/12
     */
    public Long remove(String key, Object... values) {
        return redisTemplate.opsForZSet().remove(key, values);
    }


}
