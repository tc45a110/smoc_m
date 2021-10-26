package com.smoc.cloud.auth.security.service;

import com.smoc.cloud.auth.redis.MpmRedisTokenStore;
import com.smoc.cloud.auth.security.token.BaseUserDetail;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * oauth2 token验证
 * 2019/4/4 23:02
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MpmTokenService {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;


    @Bean
    public MpmRedisTokenStore tokenStore() {
        return new MpmRedisTokenStore(redisConnectionFactory);
    }

    public ResponseData<SecurityUser> getUserByToken(String token) {
        //根据token获取 OAuth2Authentication
        OAuth2Authentication oAuth2Authentication = tokenStore().readAuthentication(token);

        //token不存在
        if (null == oAuth2Authentication) {
            log.info("[token登录][token]数据:{}{}", token, ResponseCode.TOKEN_INVALID.getMessage());
            return ResponseDataUtil.buildError(ResponseCode.TOKEN_INVALID);
        }

        //log.info("登陆token:"+token);
        //用户信息
        BaseUserDetail userDetail = (BaseUserDetail) oAuth2Authentication.getPrincipal();

        //转传输对象User
        SecurityUser user = userDetail.getSecurityUser();

        //组织用户拥有的角色
        List<String> authorities = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : userDetail.getAuthorities()) {
            authorities.add(grantedAuthority.getAuthority());
        }
        user.setAuthorities(authorities);
        return ResponseDataUtil.buildSuccess(user);
    }


}
