package com.smoc.cloud.auth.api;

import com.smoc.cloud.auth.data.provider.service.UserCacheService;
import com.smoc.cloud.common.auth.entity.Token;
import com.smoc.cloud.common.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

/**
 * 用户缓存管理
 * 2019/5/30 15:06
 **/
@Slf4j
@RestController
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class UserCacheController {

    @Autowired
    private UserCacheService userCacheService;

    /**
     * 清除用户缓存  缓存信息包括 用户信息、角色信息、菜单信息
     *
     * @return
     */
    @RequestMapping(value = "/clearUserCache/{projectName}/{userId}/{userName}", method = RequestMethod.GET)
    public ResponseData clearUserCache(@PathVariable String projectName, @PathVariable String userId, @PathVariable String userName) {

        return userCacheService.clearUserCache(projectName, userId, userName);
    }

    /**
     * 缓存sso 的token
     * @param key
     * @param token
     * @return
     */
    @RequestMapping(value = "/cacheSSOToekn/{key}/{token}", method = RequestMethod.GET)
    public ResponseData cacheSSOToekn(@PathVariable String key, @PathVariable String token) {
        return userCacheService.cacheSSOToken(key, token);
    }

    /**
     * 获取sso token  并使token 失效
     * @param key
     * @return
     */
    @RequestMapping(value = "/getSSOToken/{key}", method = RequestMethod.GET)
    public ResponseData<Token> getSSOToken(@PathVariable String key) {
        return userCacheService.getSSOToken(key);
    }

}
