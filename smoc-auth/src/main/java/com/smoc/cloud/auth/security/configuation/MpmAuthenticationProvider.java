package com.smoc.cloud.auth.security.configuation;

import com.smoc.cloud.auth.common.utils.MpmEncryptPasswordEncoder;
import com.smoc.cloud.auth.security.service.BaseUserDetailService;
import com.smoc.cloud.common.response.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 用户自有权限提供者，加载用户权限
 * 用户自有权限提供者，加载用户数据库权限，生成UsernamePasswordAuthenticationToken
 */
public class MpmAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private BaseUserDetailService userDetailsService;

    public MpmAuthenticationProvider() {

    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        String userName = token.getName();

        //查询用户信息
        UserDetails userDetails = null;
        if (userName != null) {
            userDetails = userDetailsService.loadUserByUsername(userName);
        }

        //验证用户密码
        String password = userDetails.getPassword();
        if (!new MpmEncryptPasswordEncoder().getPasswordEncoder().matches(token.getCredentials().toString(), password)) {
            throw new BadCredentialsException(ResponseCode.USER_PASSWORD_NULL.getMessage());
        }

        //返回用户授权
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 返回true后才会执行上面的authenticate方法,这步能确保authentication能正确转换类型
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}
