package com.smoc.cloud.auth.data.provider.service;

import com.smoc.cloud.common.auth.entity.Token;
import com.smoc.cloud.common.constant.RedisConstant;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 处理用户缓存
 * 2019/5/30 14:58
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserCacheService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 清除用户缓存  缓存信息包括 用户信息、角色信息、菜单信息
     *
     * @param userId  projectName  userName
     * @return
     */
    public ResponseData clearUserCache(String projectName, String userId, String userName) {

        //用户信息
        boolean hasUserKey = redisTemplate.hasKey(RedisConstant.AUTH_USERS_PREFIX + ":" + userName);
        if (hasUserKey) {
            redisTemplate.delete(RedisConstant.AUTH_USERS_PREFIX + ":" + userName);
        }

        //角色信息
        boolean hasRoleKey = redisTemplate.hasKey(RedisConstant.AUTH_USER_ROLES + ":" + userId);
        if (hasRoleKey) {
            redisTemplate.delete(RedisConstant.AUTH_USER_ROLES + ":" + userId);
        }

        //菜单信息
        boolean hasMenuKey = redisTemplate.hasKey(RedisConstant.AUTH_USER_MENUS + ":" + projectName + ":" + userId);
        if (hasMenuKey) {
            redisTemplate.delete(RedisConstant.AUTH_USER_MENUS + ":" + projectName + ":" + userId);
        }

        //清除自服务平台缓存
        if("smoc-service".equals(projectName)){
            deleteServiceKey(userId);
        }

        log.info("[清除用户缓存][clearUsersCache]数据：{}-{}-{}",projectName,userName,userId);
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 缓存sso token  设置过期时间1 分钟
     * @param key
     * @param token
     * @return
     */
    public ResponseData cacheSSOToken(String key ,String token){
        redisTemplate.opsForValue().set("sso:token:"+key,token, 1*60, TimeUnit.SECONDS);
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据key 查询token 并删除token
     * @param key
     * @return
     */
    public ResponseData<Token> getSSOToken(String key){
        String tokenData = "";
        boolean hasMenuKey = redisTemplate.hasKey("sso:token:"+key);
        if(hasMenuKey) {
            tokenData = (String) redisTemplate.opsForValue().get("sso:token:" + key);
            //redisTemplate.delete("sso:token:" + key);
        }

        Token token = new Token();
        token.setToken(tokenData);
        return ResponseDataUtil.buildSuccess(token);
    }

    /**
     * 清除自服务平台缓存
     */
    private void deleteServiceKey(String userId) {
        redisTemplate.delete(RedisConstant.SERICE_UI_MENUS + ":" + userId + ":" + "文本短信");
        redisTemplate.delete(RedisConstant.SERICE_UI_MENUS + ":" + userId + ":" + "视频短信");
        redisTemplate.delete(RedisConstant.SERICE_UI_MENUS + ":" + userId + ":" + "国际短信");
        redisTemplate.delete(RedisConstant.SERICE_UI_MENUS + ":" + userId + ":" + "智能短信");
        redisTemplate.delete(RedisConstant.SERICE_UI_MENUS + ":" + userId + ":" + "错误码");
    }
}
