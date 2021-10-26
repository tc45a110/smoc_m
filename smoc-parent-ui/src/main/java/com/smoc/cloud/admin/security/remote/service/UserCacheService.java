package com.smoc.cloud.admin.security.remote.service;

import com.smoc.cloud.admin.security.remote.client.UserCacheFeignClient;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

/**
 * 用户缓存服务
 * 2019/5/12 22:28
 */
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserCacheService {

    @Autowired
    private UserCacheFeignClient userCacheFeignClient;

    /**
     * 清除用户缓存  缓存信息包括 用户信息、角色信息、菜单信息
     *
     * @return
     */
    public ResponseData clearUserCache(String projectName, String userId, String userName) {
        try {
            ResponseData data = this.userCacheFeignClient.clearUserCache(projectName, userId, userName);
            return data;
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 缓存sso 的token
     *
     * @param key
     * @param token
     * @return
     */
    public ResponseData cacheSSOToekn(String key, String token) {
        try {
            ResponseData data = this.userCacheFeignClient.cacheSSOToekn(key, token);
            return data;
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
