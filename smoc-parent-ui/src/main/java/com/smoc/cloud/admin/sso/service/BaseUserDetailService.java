package com.smoc.cloud.admin.sso.service;


import com.smoc.cloud.admin.oauth2.service.OauthTokenService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Created by fp295 on 2018/4/16.
 */
@Slf4j
public abstract class BaseUserDetailService implements UserDetailsService {

    @Autowired
    public OauthTokenService oauthTokenService;

    @Override
    public UserDetails loadUserByUsername(String var1) throws UsernameNotFoundException {

        SecurityUser baseUser = getUser(var1);

        //判断用户是否合法
        if (null == baseUser) {
            throw new UsernameNotFoundException("用户不存在");
        }

        Collection<GrantedAuthority> auths = new ArrayList<>();
        for (String roleCode : baseUser.getAuthorities()) {
            auths.add(new SimpleGrantedAuthority(roleCode));
        }

        return new User(var1, var1, true, true, true, true, auths);
    }

    protected abstract SecurityUser getUser(String var1);

    private boolean isActive(int active) {
        return active == 1;
    }

}
