package com.smoc.cloud.tools.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Arrays;
import java.util.List;

/**
 * Redis RedisCell module
 * 基于漏桶理论的限流组件
 */
public class RedisModuleRedisCell {

    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 基于漏桶理论的redis限流
     *
     * @param key      限流的key
     * @param maxBurst 初始化容量 可以理解为初始化桶的大小，可访问的最大爆发力
     * @param tokens   每seconds 添加的容量
     * @param seconds  限流的时间间隔
     * @param times    本次要发送的条数
     * @return 返回true 表示，可以继续发送，返回false表示已触发限流
     */
    public Boolean limiter(String key, int maxBurst, int tokens, int seconds, int times) {

        Boolean status = isActionAllowed(key, maxBurst, tokens, seconds, times);
        return status;
    }

    /**
     * 请求是否被允许
     *
     * @param key      限流的key
     * @param maxBurst 最大请求   可以理解为初始化桶的大小，可访问的最大爆发力
     * @param tokens   令牌数量   每过seconds时间就往桶里放入tokens的令牌数量
     * @param seconds  时间      每过seconds时间就往桶里放入tokens的令牌数量
     * @param apply    本次获取几个令牌
     * @return
     */
    public boolean isActionAllowed(String key, int maxBurst, int tokens, int seconds, int apply) {

        DefaultRedisScript<List> script = new DefaultRedisScript<>("return redis.call('cl.throttle',KEYS[1],ARGV[1],ARGV[2],ARGV[3],ARGV[4])", List.class);

        List<Long> rst = redisTemplate.execute(script, Arrays.asList(key), maxBurst, tokens, seconds, apply);
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
