package com.smoc.cloud.filters.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class FiltersRedisDataService {

    @Resource
    private RedisTemplate<String, Object> limitTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 是否包含 set 存储
     *
     * @param key
     * @param value
     * @return
     */
    public Boolean contains(String key, String value) {
        return redisTemplate.opsForSet().isMember(key, new Long(value));
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

        List<Long> rst = limitTemplate.execute(script, Arrays.asList(key), maxBurst, tokens, seconds, apply);
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
