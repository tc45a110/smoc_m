package com.smoc.cloud.utils;

import com.smoc.cloud.common.utils.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class RandomService {

    /**
     * Redis key前缀
     */
    public static String PREFIX = "auth:enterprise:code";


    @Resource
    private RedisTemplate<String, String> stringRedisTemplate;

    /**
     * 获取len位字母字符串
     *
     * @param len 随机字符长度
     * @return
     */
    public String getRandomStr(int len) {
        String random = RandomUtil.generateByShuffle(len);
        return hasKey(random, len);
    }

    /**
     * 判断 random是否存在
     *
     * @param random
     * @return
     */
    public String hasKey(String random, int len) {
        Boolean hasKey = stringRedisTemplate.hasKey(PREFIX + ":" + random);
        if (null == hasKey || !hasKey) {
            return random;
        }
        random = RandomUtil.generateByShuffle(len);
        return hasKey(random, len);
    }
}
