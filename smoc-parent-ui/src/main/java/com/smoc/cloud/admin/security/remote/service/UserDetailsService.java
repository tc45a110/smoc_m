package com.smoc.cloud.admin.security.remote.service;

import com.smoc.cloud.admin.oauth2.service.OauthTokenService;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 基于spring security 的用户接口实现
 * 2019/4/16 12:39
 **/
@Slf4j
@Service
public class UserDetailsService implements IUserDetailsService {

    @Autowired
    private OauthTokenService oauthTokenService;

    @Override
    public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {

        //log.info("登陆token:"+token);
        //用户信息
        ResponseData<SecurityUser> userResponseData = oauthTokenService.getUser(token);
        //判断用户是否合法
        if (!ResponseCode.SUCCESS.getCode().equals(userResponseData.getCode())) {
            throw new UsernameNotFoundException(userResponseData.getMessage());
        }

        Collection<GrantedAuthority> auths = new ArrayList<>();
        for (String roleCode : userResponseData.getData().getAuthorities()) {
            auths.add(new SimpleGrantedAuthority(roleCode));
        }

        return new User(token, token, true, true, true, true, auths);
    }
}
