package com.somc.cloud.gateway.redis.service;

import com.smoc.cloud.common.redis.RedisConstant;
import com.smoc.cloud.common.redis.RedisModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class DataService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedisTemplate<String, String> stringRedisTemplate;


    /**
     * 防止重放攻击
     *
     * @param signatureNonce
     * @return
     */
    public boolean nonce(String signatureNonce) {

        //判断是否存在key
        boolean hasKey = stringRedisTemplate.hasKey(RedisConstant.NONCE + signatureNonce);
        if (hasKey) {
            return true;
        }

        //把数据放到redis里
        stringRedisTemplate.opsForValue().set(RedisConstant.NONCE + signatureNonce, signatureNonce);
        stringRedisTemplate.expire(RedisConstant.NONCE + signatureNonce, 10 * 60, TimeUnit.SECONDS);
        return false;
    }

    /**
     * 查询http 短信发送服务秘钥
     *
     * @param account
     * @return
     */
    public RedisModel getHttpServerKey(String account) {
        //redis 查询
        RedisModel redisModel = (RedisModel) redisTemplate.opsForValue().get(RedisConstant.HTTP_SERVER_KEY + account);
        return redisModel;
    }

    /**
     * 判断订单号是否重复
     *
     * @param orderNo
     * @return
     */
    public boolean orderNoRepeat(String orderNo) {

        //判断是否存在key
        boolean hasKey = stringRedisTemplate.hasKey(RedisConstant.ORDERS + orderNo);
        if (hasKey) {
            return true;
        }
        return false;
    }

    /**
     * 保存订单号
     *
     * @param orderNo
     * @return
     */
    @Async
    public void saveOrderNo(String orderNo) {
        //把数据放到redis里
        stringRedisTemplate.opsForValue().set(RedisConstant.ORDERS + orderNo, orderNo);
        stringRedisTemplate.expire(RedisConstant.ORDERS + orderNo, 30, TimeUnit.DAYS);
    }

    /**
     * 账号限流判断 提交次数限流
     *
     * @param account 业务账号
     * @return
     */
    public Boolean limiter(String account) {

        Boolean status = false;

        //没有设置限流
        RedisModel redisModel = getHttpServerKey(account);
        if (null == redisModel || redisModel.getSubmitRate() == 0) {
            return true;
        }

        //限流的账号、最高流速maxBurst、限流
        String key = RedisConstant.HTTP_SERVER_SUBMIT_LIMITER + account;
        Integer maxBurst = redisModel.getSubmitRate() * 2;
        status = isActionAllowed(key, maxBurst, redisModel.getSubmitRate(), 1, 1);
        if (!status) {
            log.warn("[触发限流]账号：{}-触发提交任务限流", account);
        }

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
