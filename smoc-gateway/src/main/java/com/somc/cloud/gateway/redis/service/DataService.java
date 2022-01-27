package com.somc.cloud.gateway.redis.service;

import com.smoc.cloud.common.redis.smoc.identification.KeyEntity;
import com.smoc.cloud.common.redis.smoc.identification.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class DataService {

    @Resource
    private RedisTemplate<String, KeyEntity> redisTemplate;

    @Resource
    private RedisTemplate<String, String> stringRedisTemplate;

    /**
     * 查询对应的 密钥
     *
     * @param identificationAccount
     * @return
     */
    public KeyEntity getKey(String identificationAccount) {
        //redis 查询
        KeyEntity keyEntity = redisTemplate.opsForValue().get(RedisConstant.KEY + identificationAccount);
        return keyEntity;
    }

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
        stringRedisTemplate.expire(RedisConstant.NONCE + signatureNonce, 5 * 60, TimeUnit.SECONDS);
        return false;
    }

}
