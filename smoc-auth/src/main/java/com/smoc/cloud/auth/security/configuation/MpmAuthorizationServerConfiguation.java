package com.smoc.cloud.auth.security.configuation;


import com.smoc.cloud.auth.common.utils.MpmEncryptPasswordEncoder;
import com.smoc.cloud.auth.redis.MpmRedisTokenStore;
import com.smoc.cloud.auth.security.client.JdbcClientDetailsService;
import com.smoc.cloud.auth.security.exception.MpmWebResponseExceptionTranslator;
import com.smoc.cloud.auth.security.service.BaseUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

import javax.sql.DataSource;

/**
 * 授权服务器配置
 * 2019/4/10 23:29
 */
@Slf4j
@Configuration
@EnableAuthorizationServer
public class MpmAuthorizationServerConfiguation extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    BaseUserDetailService userDetailService;

    /**
     * @Title: tokenStore
     * @Description: 用户验证信息的保存策略，可以存储在内存中，关系型数据库中，redis中
     */
//    @Bean
//    public TokenStore tokenStore() {
//        return new JdbcTokenStore(dataSource);
//    }

    /**
     * 声明 clientDetails实现
     *
     * @return
     */
    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public MpmRedisTokenStore tokenStore() {
        return new MpmRedisTokenStore(redisConnectionFactory);
    }

    /**
     * 允许表单验证，浏览器直接发送post请求即可获取tocken
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {

        security.tokenKeyAccess("permitAll()")//对于CheckEndpoint控制器[框架自带的校验]的/oauth/token端点允许所有客户端发送器请求而不会被Spring-security拦截
                .checkTokenAccess("permitAll()")//要访问/oauth/check_token必须设置为permitAll()，但这样所有人都可以访问了，设为isAuthenticated()又导致访问不了，这个问题暂时没找到解决方案
                .allowFormAuthenticationForClients()//允许客户表单认证,不加的话/oauth/token无法访问
                .passwordEncoder(new MpmEncryptPasswordEncoder().getPasswordEncoder());//设置oauth_client_details中的密码编码器
    }

    /**
     * 定义通过验证服务注册了哪些客户端应用程序
     * 这个方法主要是用于校验注册的第三方客户端的信息，可以存储在数据库中
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 使用JdbcClientDetailsService客户端详情服务
        clients.withClientDetails(clientDetails());


    }

    /**
     * 这个方法主要的作用用于控制token的端点等信息
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //来支持 password grant type
        endpoints.authenticationManager(authenticationManager)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)  //支持GET  POST  请求获取token
                .tokenStore(tokenStore())
                // refresh_token需要userDetailsService
                .tokenServices(defaultTokenServices()).userDetailsService(userDetailService)
                //该字段设置设置refresh token是否 重复 使用
                //.reuseRefreshTokens(true)
                //认证异常翻译
                .exceptionTranslator(webResponseExceptionTranslator());
    }

    @Bean
    public WebResponseExceptionTranslator webResponseExceptionTranslator() {
        return new MpmWebResponseExceptionTranslator();
    }

    /**
     * <p>注意，自定义TokenServices的时候，需要设置@Primary，否则报错，</p>
     *
     * @return
     */
    @Primary
    @Bean
    public DefaultTokenServices defaultTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);
        //tokenServices.setClientDetailsService(clientDetailsService);
        // token有效期自定义设置，默认12小时,现在修改为半小时
        tokenServices.setAccessTokenValiditySeconds(60 * 60 * 24);
        // refresh_token默认30天,现在修改为半小时
        tokenServices.setRefreshTokenValiditySeconds(60 * 60 * 5);
        return tokenServices;
    }

}

