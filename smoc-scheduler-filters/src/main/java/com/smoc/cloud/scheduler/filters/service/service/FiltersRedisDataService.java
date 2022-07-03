package com.smoc.cloud.scheduler.filters.service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class FiltersRedisDataService {

    @Resource(name = "defaultRedisTemplate")
    private RedisTemplate redisTemplate;

    @Resource(name = "redisTemplate2")
    private RedisTemplate<String ,Object> redisTemplate2;

    /**
     * 是否包含 set 存储
     *
     * @param key
     * @param value
     * @return
     */
    public Boolean isMember(String key, String value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 获取set
     *
     * @param key
     * @return
     */
    public Set<String> sget(String key) {
        Set<String> sets = redisTemplate.opsForSet().members(key);
        return sets;
    }


    /**
     * HashGet
     *
     * @param redisKey
     * @param hashKey
     * @return 值
     */
    public Object hget(String redisKey, String hashKey) {
        return redisTemplate.opsForHash().get(redisKey, hashKey);
    }

    /**
     * @param key       (filters:temporary:limit:flow:carrier:20220523)
     * @param field     (YQT124_CMCC)
     * @param increment 增加数量
     * @return java.lang.Long
     * @description 给 map中指定字段的整数值加上 long型增量 increment
     */
    public Long incrementLong(String key, Object field, long increment) {
        Long num = redisTemplate.opsForHash().increment(key, field, increment);
        redisTemplate.expire(key, 60 * 60 * 24, TimeUnit.SECONDS);
        return num;
    }

    /**
     * @param key  (filters:temporary:limit:flow:carrier:20220523)
     * @param field (YQT124_CMCC)
     * @return java.lang.Object
     * @description 获取 map中指定的 key值，如果存在则返回值，没有就返回null
     */
    public Object getMapValue(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }


    /**
     * 批量取hash数据
     *
     * @param key
     * @param keys hash key列表
     * @return
     */
    public List<Object> hashGetBatch(String key, Set<String> keys) {
        List list = redisTemplate.executePipelined(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                for (String keyValue : keys) {
                    connection.hGet(key.getBytes(), keyValue.getBytes());
                }
                return null;
            }
        });
        return list;
    }

    /**
     * HashEntries
     *
     * @param redisKey
     * @return
     */
    public Map<Object, Object> hEntries(String redisKey) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(redisKey);
        return entries;
    }

    /**
     * 根据key 获取字符串
     *
     * @param redisKey
     * @return
     */
    public Object get(String redisKey) {
        Object result = redisTemplate.opsForValue().get(redisKey);
        return result;
    }

    /**
     * redis 计数器
     *
     * @param redisKey
     * @param incrementBy 增加次数
     * @return
     */
    public long increment(String redisKey, Integer incrementBy) {
        long count = redisTemplate.opsForValue().increment(redisKey, incrementBy);
        return count;
    }

    /**
     * 按账号 检测手机号发送频率限制
     *
     * @param account  限流的账号
     * @param mobile   限流的手机号
     * @param maxBurst 初始化容量
     * @param tokens   每seconds 添加的容量
     * @param seconds  限流的时间间隔
     * @param times    本次要发送的条数
     * @return 返回true 表示，可以继续发送，返回false表示已触发限流
     */
    public Boolean limiter(String account, String mobile, int maxBurst, int tokens, int seconds, int times) {

        Boolean status = isActionAllowed(account + ":" + mobile, maxBurst, tokens, seconds, times);
        return status;
    }

    /**
     * 按系统手机号发送限流
     *
     * @param mobile   限流的手机号
     * @param maxBurst 初始化容量
     * @param tokens   每seconds 添加的容量
     * @param seconds  限流的时间间隔
     * @param times    本次要发送的条数
     * @return 返回true 表示，可以继续发送，返回false表示已触发限流
     */
    public Boolean limiter(String mobile, int maxBurst, int tokens, int seconds, int times) {
        Boolean status = isActionAllowed(mobile, maxBurst, tokens, seconds, times);
        return status;
    }

    /**
     * 按系统手机号发送限流
     *
     * @param key   限流的key
     * @param maxBurst 初始化容量
     * @param tokens   每seconds 添加的容量
     * @param seconds  限流的时间间隔
     * @param times    本次要发送的条数
     * @return 返回true 表示，可以继续发送，返回false表示已触发限流
     */
    public Boolean limiterMessageFilter(String key, int maxBurst, int tokens, int seconds, int times) {
        Boolean status = isActionAllowed(key, maxBurst, tokens, seconds, times);
        return status;
    }

    /**
     * 请求是否被允许
     *
     * @param key      限流的key
     * @param maxBurst 最大请求
     * @param tokens   令牌数量   每过seconds时间就往桶里放入tokens的令牌数量
     * @param seconds  时间      每过seconds时间就往桶里放入tokens的令牌数量
     * @param apply    本次获取几个令牌
     * @return
     */
    public boolean isActionAllowed(String key, int maxBurst, int tokens, int seconds, int apply) {

        DefaultRedisScript<List> script = new DefaultRedisScript<>("return redis.call('cl.throttle',KEYS[1],ARGV[1],ARGV[2],ARGV[3],ARGV[4])", List.class);

        List<Long> rst = redisTemplate2.execute(script, Arrays.asList(key), maxBurst, tokens, seconds, apply);
        //响应结果
        /**
         * 1) (integer) 0       # 当前请求是否被允许，0表示允许，1表示不允许
         * 2) (integer) 11      # 令牌桶的最大容量，令牌桶中令牌数的最大值
         * 3) (integer) 10      # 令牌桶中当前的令牌数
         * 4) (integer) -1      # 如果被拒绝，需要多长时间后在重试，如果当前被允许则为-1
         * 5) (integer) 12      # 多长时间后令牌桶中的令牌会满
         */
        return rst.get(0) == 0;
    }

}
