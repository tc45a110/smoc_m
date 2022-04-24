package com.smoc.cloud.http.service;

import com.smoc.cloud.common.http.server.utils.RedisModel;
import com.smoc.cloud.common.redis.smoc.identification.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 按账号限流
 * redis_cell 实现 漏桶算法、令牌算法的限流
 */
@Slf4j
@Service
public class AccountRateLimiter {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 账号限流判断
     *
     * @param account      业务账号
     * @param sendMessages 分拆后的短信数
     * @return
     */
    public Boolean limiter(String account, Integer sendMessages) {


        Boolean status = false;

        //没有设置限流
        RedisModel redisModel = getHttpServerKey(account);
        if (null == redisModel || redisModel.getSendRate() == 0) {
            return true;
        }

        //限流的账号、最高流速maxBurst、限流
        String key = RedisConstant.HTTP_SERVER_MESSAGE_LIMITER + account;
        Integer maxBurst = redisModel.getSendRate() * 2;
        status = isActionAllowed(key, maxBurst, redisModel.getSendRate(), 1, sendMessages);
        if (!status) {
            log.warn("[触发限流]账号：{}-触发消息发送限流", account);
        }
        return status;
    }

    /**
     * 查询http 协议业务账号 限流值
     *
     * @param account
     * @return
     */
    public RedisModel getHttpServerKey(String account) {
        //redis 查询
        String key = RedisConstant.HTTP_SERVER_KEY + account;
        boolean hasKey = redisTemplate.hasKey(key.trim());
        //log.info("[限流测试]account key：{},{}",hasKey,key);
        RedisModel redisModel = (RedisModel) redisTemplate.opsForValue().get(RedisConstant.HTTP_SERVER_KEY + account);
        //log.info("[限流测试]redisModel：{}",new Gson().toJson(redisModel));
        return redisModel;
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
