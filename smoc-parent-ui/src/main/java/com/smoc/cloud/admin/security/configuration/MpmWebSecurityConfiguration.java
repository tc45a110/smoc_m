package com.smoc.cloud.admin.security.configuration;

import com.smoc.cloud.admin.oauth2.service.OauthTokenService;
import com.smoc.cloud.admin.security.filter.MpmSecurityMetadataSourceFilter;
import com.smoc.cloud.admin.security.filter.MpmUsernamePasswordAuthenticationFilter;
import com.smoc.cloud.admin.security.handler.*;
import com.smoc.cloud.admin.security.properties.SystemProperties;
import com.smoc.cloud.admin.security.provider.MpmAuthenticationProvider;
import com.smoc.cloud.admin.security.remote.service.UserDetailsService;
import com.smoc.cloud.admin.security.sms.MessageConfig;
import com.smoc.cloud.admin.sso.filter.PhoneLoginAuthenticationFilter;
import com.smoc.cloud.admin.sso.filter.TokenLoginAuthenticationFilter;
import com.smoc.cloud.admin.sso.handler.PhoneLoginAuthenticationFailureHandler;
import com.smoc.cloud.admin.sso.handler.PhoneLoginAuthenticationSuccessHandler;
import com.smoc.cloud.admin.sso.provider.PhoneAuthenticationProvider;
import com.smoc.cloud.admin.sso.provider.TokenAuthenticationProvider;
import com.smoc.cloud.admin.sso.service.PhoneUserDetailService;
import com.smoc.cloud.admin.sso.service.TokenUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;
import java.util.List;


/**
 * 启动、配置spring security类
 * <p>
 * Description:启动、配置spring security类 。配置相关系统安全策略、拦截策略、登录策略、安全处理逻辑
 * </p>
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class MpmWebSecurityConfiguration extends WebSecurityConfigurerAdapter {




    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private OauthTokenService oauthTokenService;

    @Autowired
    private PhoneUserDetailService phoneUserDetailService;

    @Autowired
    private TokenUserDetailService tokenUserDetailService;

    @Autowired
    private SystemProperties systemProperties;

    @Autowired
    private MessageConfig messageConfig;

	@Bean
	public MpmSecurityMetadataSourceFilter mpmSecurityMetadataSourceFilter(){
	    return new MpmSecurityMetadataSourceFilter(oauthTokenService);
    }

    /**
     * 设置spring security不进行拦截的静态文件
     */
    @Override
    public void configure(WebSecurity web) throws Exception {

        log.info("[系统启动][安全配置]数据:不进行拦截的静态文件或url");
        web.ignoring().antMatchers("/static/**", "/**/*.js");
        web.ignoring().antMatchers("/static/**", "/**/*.css");
        web.ignoring().antMatchers("/static/**", "/**/*.woff2");
        web.ignoring().antMatchers("/static/**", "/**/*.jpg");
        web.ignoring().antMatchers("/static/**", "/**/*.ico");
        web.ignoring().antMatchers("/static/**", "/**/*.png");
        web.ignoring().antMatchers("/404");
        web.ignoring().antMatchers("/500");
        web.ignoring().antMatchers("/**/error");
        web.ignoring().antMatchers("/**/api/entBiz/*");
        web.ignoring().antMatchers("/**/verifyCode");
        web.ignoring().antMatchers("/**/verifyCode/*");
        web.ignoring().antMatchers("/**/login/*");
        web.ignoring().antMatchers("/**/getPublicKey");

        //web.ignoring().antMatchers("/contacts/upFiles");
        //web.ignoring().antMatchers("/group/save/*");
    }

    /**
     * 设置过滤、权限处理逻辑、权限登录异常等
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        log.info("[系统启动][安全配置]数据:设置过滤、权限处理逻辑、权限登录异常等");
        //http.addFilterAfter(smsValidateCodeFilter, MpmUsernamePasswordAuthenticationFilter.class)；

        http.addFilterBefore(getPhoneLoginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(getTokenLoginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        /**
         * 登录验证
         */
        http.addFilterAfter(MpmUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.formLogin().loginPage("/login").permitAll();

        /**
         * 加载系统权限，并对请求进行拦截，分析出请求资源所需要的角色
         */
        http.authorizeRequests().anyRequest().authenticated()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
                        fsi.setSecurityMetadataSource(mpmSecurityMetadataSourceFilter());
                        fsi.setAccessDecisionManager(accessDecisionManager());
                        fsi.setAuthenticationManager(authenticationManagerBean());
                        return fsi;
                    }
                });

        /**
         * 授权失败或访问无权限资源时处理,现在有两种情况，第一种不需要登录的非法访问 不能跳转到登录页，自己扩展了
         * LoginUrlAuthenticationEntryPoint
         */
        http.exceptionHandling().authenticationEntryPoint(new MpmAuthenticationEntryPoint("/login"))
                .accessDeniedHandler(new MpmAccessDeniedExceptionHandler());

        /**
         * 由于开启了 csrf 退出不能采用 get模式，现在自定义了 logout
         */
        // http.logout().logoutUrl("/logout").logoutSuccessUrl("/user/login")
        // .logoutSuccessHandler(new MpmLogoutSuccessHandler()).permitAll();

        /**
         * 解决spring security iframe 不显示问题
         */
        http.headers().frameOptions().sameOrigin();

        /**
         * csrf 启用，默认就是开启的
         */
       http.csrf().disable();

    }

    /**
     * 用户验证
     * 设置权限验证提供者 这采用DaoAuthenticationProvider 加载用户自有权限，配合mpmSecurityMetadataSource
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        log.info("[系统启动][安全配置]数据:设置用户权限提供者：MpmAuthenticationProvider");
        auth.authenticationProvider(new MpmAuthenticationProvider(userDetailsService));
        auth.authenticationProvider(phoneAuthenticationProvider());
        auth.authenticationProvider(tokenAuthenticationProvider());

    }

    @Bean
    public PhoneAuthenticationProvider phoneAuthenticationProvider(){
        PhoneAuthenticationProvider provider = new PhoneAuthenticationProvider();
        // 设置userDetailsService
        provider.setUserDetailsService(phoneUserDetailService);
        // 禁止隐藏用户未找到异常
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }

    @Bean
    public TokenAuthenticationProvider tokenAuthenticationProvider(){
        TokenAuthenticationProvider provider = new TokenAuthenticationProvider();
        // 设置userDetailsService
        provider.setUserDetailsService(tokenUserDetailService);
        // 禁止隐藏用户未找到异常
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }

    /**
     * 自定义用户登录，并设置相应拦截策略
     *
     * @return
     */
    @Bean
    UsernamePasswordAuthenticationFilter MpmUsernamePasswordAuthenticationFilter() {

        log.info("[系统启动][安全配置]数据:自定义用户登录，并设置相应拦截策略");
        MpmUsernamePasswordAuthenticationFilter mpmUsernamePasswordAuthenticationFilter = new MpmUsernamePasswordAuthenticationFilter();
        mpmUsernamePasswordAuthenticationFilter.setPostOnly(true);
        mpmUsernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());

        mpmUsernamePasswordAuthenticationFilter
                .setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(systemProperties.getLoginPostPath(), "POST"));
        mpmUsernamePasswordAuthenticationFilter.setUsernameParameter("userName");
        mpmUsernamePasswordAuthenticationFilter.setPasswordParameter("password");
        mpmUsernamePasswordAuthenticationFilter.setVerifyCodeParameter("verifyCode");

        //登录失败处理
        mpmUsernamePasswordAuthenticationFilter
                .setAuthenticationFailureHandler(new MpmAuthenticationFailureHandler());
        //登录成功处理
        mpmUsernamePasswordAuthenticationFilter
                .setAuthenticationSuccessHandler(new MpmAuthenticationSuccessHandler(oauthTokenService,systemProperties,messageConfig));
        return mpmUsernamePasswordAuthenticationFilter;
    }

    /**
     * 手机验证码登陆过滤器
     * @return
     */
    @Bean
    public PhoneLoginAuthenticationFilter getPhoneLoginAuthenticationFilter() {
        PhoneLoginAuthenticationFilter filter = new PhoneLoginAuthenticationFilter();
        try {
            filter.setAuthenticationManager(this.authenticationManagerBean());
        } catch (Exception e) {
            e.printStackTrace();
        }
        filter.setAuthenticationSuccessHandler(new PhoneLoginAuthenticationSuccessHandler(oauthTokenService,systemProperties));
        filter.setAuthenticationFailureHandler(new PhoneLoginAuthenticationFailureHandler());
        return filter;
    }

    @Bean
    public TokenLoginAuthenticationFilter getTokenLoginAuthenticationFilter() {
        TokenLoginAuthenticationFilter filter = new TokenLoginAuthenticationFilter();
        try {
            filter.setAuthenticationManager(this.authenticationManagerBean());
        } catch (Exception e) {
            e.printStackTrace();
        }
        filter.setAuthenticationSuccessHandler(new MpmAuthenticationSuccessHandler(oauthTokenService,systemProperties,messageConfig));
        filter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/login?error"));
        return filter;
    }

    /**
     * 是决定对资源是否有访问权限的地方，没权限的时候返回对应异常
     */
    @Bean(name = "accessDecisionManager")
    public AccessDecisionManager accessDecisionManager() {

        log.info("[系统启动][安全配置]数据:设置权限验证管理器：MpmAccessDecisionManager");
        List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<AccessDecisionVoter<? extends Object>>();
        decisionVoters.add(new RoleVoter());
        decisionVoters.add(new AuthenticatedVoter());
        decisionVoters.add(webExpressionVoter());// 启用表达式投票器
        MpmAccessDecisionManager accessDecisionManager = new MpmAccessDecisionManager(decisionVoters);
        return accessDecisionManager;
    }

    /**
     * 权限管理者，权限管理在此，但是真正验证操作的是authenticationProvider
     */
    @Bean(name = "authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() {
        AuthenticationManager authenticationManager = null;
        try {
            authenticationManager = super.authenticationManagerBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authenticationManager;
    }

    /**
     * 表达式控制器
     */
    @Bean(name = "expressionHandler")
    public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler webSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        return webSecurityExpressionHandler;
    }

    /**
     * 表达式投票器
     */
    @Bean(name = "expressionVoter")
    public WebExpressionVoter webExpressionVoter() {
        WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
        webExpressionVoter.setExpressionHandler(webSecurityExpressionHandler());
        return webExpressionVoter;
    }
}
