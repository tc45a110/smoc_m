package com.smoc.cloud.auth.data.provider.service;

import com.smoc.cloud.common.constant.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 微信公众号 token 服务器缓存
 */
@Slf4j
@Service
public class WeixinTokenService {


    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 获取微信access_token
     *
     * @return
     */
    public String getWeixinAccessToken() {
        //微信access_token
        String accessToken = "";
        boolean hasUserKey = redisTemplate.hasKey(RedisConstant.MODULE_AUTH_PREFIX + ":weixin:access_token");
        if (hasUserKey) {
            accessToken = redisTemplate.opsForValue().get(RedisConstant.MODULE_AUTH_PREFIX + ":weixin:access_token");
        }
        log.info("[微信accessToken][查询]数据:{}", accessToken);
        return accessToken;
    }

    /**
     * 保存微信access_token
     *
     * @return
     */
    public void saveWeixinAccessToken(String accessToken) {
        redisTemplate.opsForValue().set(RedisConstant.MODULE_AUTH_PREFIX + ":weixin:access_token", accessToken, 2 * 60 * 60, TimeUnit.SECONDS);
        log.info("[微信accessToken][保存]数据:{}", accessToken);
    }

    /**
     * 获取微信jsapi_ticket
     *
     * @return
     */
    public String getWeixinJsapiTicket() {
        //微信jsapi_ticket
        String jsapiTicket = "";
        boolean hasUserKey = redisTemplate.hasKey(RedisConstant.MODULE_AUTH_PREFIX + ":weixin:jsapi_ticket");
        if (hasUserKey) {
            jsapiTicket = redisTemplate.opsForValue().get(RedisConstant.MODULE_AUTH_PREFIX + ":weixin:jsapi_ticket");
        }
        log.info("[微信JsapiTicket][查询]数据:{}", jsapiTicket);
        return jsapiTicket;
    }

    /**
     * 保存微信jsapi_ticket
     *
     * @return
     */
    public void saveWeixinJsapiTicket(String jsapiTicket) {
        redisTemplate.opsForValue().set(RedisConstant.MODULE_AUTH_PREFIX + ":weixin:access_token", jsapiTicket, 2 * 60 * 60, TimeUnit.SECONDS);
        log.info("[微信JsapiTicket][保存]数据:{}", jsapiTicket);
    }
}
