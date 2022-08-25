package com.smoc.cloud.api.remote.cmcc.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CmccRedisCacheUtils {

    @Resource(name = "defaultRedisTemplate")
    private RedisTemplate redisTemplate;


    /**
     * 获取本地token
     *
     * @return
     */
    public String getLocalToken(String carrierIdentifying) {
        String token = (String) redisTemplate.opsForValue().get("iot:remote:api:cmcc:token:"+carrierIdentifying);
        return token;
    }

    /**
     * 本地缓存token
     *
     * @param token
     */
    public void saveLocalToken(String carrierIdentifying,String token, Long seconds, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set("iot:remote:api:cmcc:token:"+carrierIdentifying, token, seconds, timeUnit);
    }


    /**
     * 获取token的8位序列号
     *
     * @return
     */
    public String getSequence() {
        String sequence = "00000000";
        long id = this.generate("iot:remote:api:cmcc:sequence");
        if (id == 0) {
            return sequence;
        }
        sequence = id + "";
        int idLength = sequence.length();
        for (int i = 0; i < 8 - idLength; i++) {
            sequence = "0" + sequence;
        }
        return sequence;
    }

    /**
     * @param key
     * @return
     * @Title: generate  自增长
     * @Description: Atomically increments by one the current value.
     */
    public long generate(String key) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        return counter.incrementAndGet();
    }
}
