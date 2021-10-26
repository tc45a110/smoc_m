package com.smoc.cloud.auth.security.configuation;

import com.smoc.cloud.auth.data.provider.service.BaseRoleService;
import com.smoc.cloud.auth.security.service.BaseUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


/**
 * 安全配置
 *
 * @ EnableWebSecurity 启用web安全配置
 * @ EnableGlobalMethodSecurity 启用全局方法安全注解，就可以在方法上使用注解来对请求进行过滤
 * 2019/4/10 23:29
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MpmSecurityConfiguation extends WebSecurityConfigurerAdapter {

    /**
     * 注入用户信息服务
     */
    @Autowired
    BaseUserDetailService userDetailService;

    @Autowired
    BaseRoleService baseRoleService;

    /**
     * 全局用户信息
     *
     * @param auth 认证管理
     * @throws Exception 用户认证异常信息
     */
    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    /**
     * 认证管理
     *
     * @return 认证管理对象
     * @throws Exception 认证异常信息
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    /**
     * 全局用户信息
     *
     * @param auth 认证管理
     * @throws Exception 用户认证异常信息
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //auth.userDetailsService(userDetailService);
        auth.authenticationProvider(authenticationProvider());
    }

    /**
     * 权限验证
     *
     * @return
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        AuthenticationProvider authenticationProvider = new MpmAuthenticationProvider();
        return authenticationProvider;
    }

    /**
     * http安全配置
     *
     * @param http http安全对象
     * @throws Exception http安全异常信息
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers("/auth/token/getUserAuthorizationByToken/*").permitAll()//开放的资源不用授权
                .antMatchers("/authority/org/save/*").permitAll()//开放的资源不用授权
                .antMatchers("/authority/user/save/*").permitAll()//开放的资源不用授权
                .antMatchers("/authority/user/closeUser/*/*").permitAll()//开放的资源不用授权
                .antMatchers("/auth/oauth/getUser/*").permitAll()
                .antMatchers("/oauth/getUser/*").permitAll()
                .antMatchers("/oauth/token").permitAll()
                .antMatchers("/flow/save/*").permitAll()
                .anyRequest().authenticated().and()
                .httpBasic().and().csrf().disable();

    }


}
