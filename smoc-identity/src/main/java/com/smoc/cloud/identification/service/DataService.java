package com.smoc.cloud.identification.service;

import com.smoc.cloud.common.redis.smoc.identification.KeyEntity;
import com.smoc.cloud.common.redis.smoc.identification.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
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

}
