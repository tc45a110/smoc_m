package com.smoc.cloud.utils;

import com.smoc.cloud.common.utils.RandomUtil;
import com.smoc.cloud.sequence.repository.SequenceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Slf4j
@Service
public class RandomService {

    @Resource
    private SequenceRepository sequenceRepository;

    /**
     * Redis key前缀
     */
    public static String PREFIX = "auth:enterprise:code";


    @Resource(name = "defaultRedisTemplate")
    private RedisTemplate<String, String> stringRedisTemplate;

    public String getBusinessAccount(String code){

        if(StringUtils.isEmpty(code)){
            return sequenceRepository.findSequence("BUSINESS_ACCOUNT")+"";
        }

        String prefix = code.substring(0,1);

        String sequence = ""+sequenceRepository.findSequence(prefix);

        //如果为空，标识企业不是英文字符
        if(StringUtils.isEmpty(sequence) || "null".equals(sequence)){
            return sequenceRepository.findSequence("ACCOUNT_RANDOM")+"";
        }

        return sequence;

    }

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
