package com.smoc.cloud.auth.security.service;

import com.smoc.cloud.auth.data.provider.entity.BaseRole;
import com.smoc.cloud.auth.data.provider.service.BaseRoleService;
import com.smoc.cloud.auth.data.provider.service.BaseUserService;
import com.smoc.cloud.auth.security.token.BaseUserDetail;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息服务
 * 实现 Spring Security的UserDetailsService接口方法，用于身份认证
 * 2019/4/10 23:29
 */
@Slf4j
@Service("userDetailService")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BaseUserDetailService implements UserDetailsService {

    @Autowired
    protected BaseUserService baseUserService;
    @Autowired
    private BaseRoleService baseRoleService;


    /**
     * 根据用户名查找账户信息并返回用户信息实体
     *
     * @param userName 用户名
     * @return 用于身份认证的 UserDetails 用户信息实体
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        //加载用户信息
        ResponseData<SecurityUser> securityUserData = baseUserService.findUserByUserName(userName);
        if (securityUserData.getData() == null || !ResponseCode.SUCCESS.getCode().equals(securityUserData.getCode())) {
            log.error("{}：{}", userName, ResponseCode.USER_NOT_EXIST.getMessage());
            throw new UsernameNotFoundException(ResponseCode.USER_NOT_EXIST.getMessage());
        }

        SecurityUser sysUser = securityUserData.getData();

        //log.info("user:{}",sysUser.getUserName());

        //log.info("user:{}",sysUser.getPassword());


        //下面保留字段可以管理用户可用性、过期性、有效性、锁定性
        // 可用性 :true:可用 false:不可用
        boolean enabled = isActive(1);
        // 过期性 :true:没过期 false:过期
        boolean accountNonExpired = true;
        // 有效性 :true:凭证有效 false:凭证无效
        boolean credentialsNonExpired = true;
        // 锁定性 :true:未锁定 false:已锁定
        boolean accountNonLocked = true;

        //加载用户角色信息
        ResponseData<List<BaseRole>> baseRoleListResponseData = baseRoleService.getRoleByUserId(sysUser.getId());
        if (!ResponseCode.SUCCESS.getCode().equals(baseRoleListResponseData.getCode()) || baseRoleListResponseData.getData() == null) {
            log.info(" {}：{}", userName, ResponseCode.USER_UNAUTH.getMessage());
            throw new UsernameNotFoundException(ResponseCode.USER_UNAUTH.getMessage());
        }
        //log.info("加载登录用户角色............");

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        baseRoleListResponseData.getData().forEach(baseRole -> {
            // 存储用户、角色信息到GrantedAuthority，并放到GrantedAuthority列表
            GrantedAuthority authority = new SimpleGrantedAuthority(baseRole.getRoleCode());
            grantedAuthorities.add(authority);

        });

        // 返回带有用户权限信息的User
        org.springframework.security.core.userdetails.User securityUser = new org.springframework.security.core.userdetails.User(sysUser.getUserName(),
                sysUser.getPassword(), enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, grantedAuthorities);

        return new BaseUserDetail(sysUser, securityUser);
    }

    private boolean isActive(int active) {
        return active == 1;
    }

}
