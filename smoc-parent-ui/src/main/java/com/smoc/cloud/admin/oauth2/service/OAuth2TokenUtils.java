package com.smoc.cloud.admin.oauth2.service;

import com.smoc.cloud.admin.oauth2.entity.MpmOAuth2RefreshToken;
import com.smoc.cloud.admin.oauth2.properties.OAuth2TokenCofigurationProperties;
import com.smoc.cloud.admin.security.properties.SystemProperties;
import com.smoc.cloud.common.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * OAuth2Token 工具包
 * 2019/4/14 11:39
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OAuth2TokenUtils {


    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private OAuth2TokenCofigurationProperties oAuth2TokenCofigurationProperties;

    @Autowired
    private SystemProperties systemProperties;

    /**
     * 组织oauth2 密码授权模式 认证信息集合的对象
     *
     * @param username
     * @param password
     * @return
     */
    private ResourceOwnerPasswordResourceDetails getDetail(String username, String password) {

        ResourceOwnerPasswordResourceDetails resource = getDetail();
        resource.setUsername(username);
        resource.setPassword(password);
        resource.setGrantType("password");

        return resource;

    }

    /**
     * 客户端模式
     *
     * @return
     */
    private ClientCredentialsResourceDetails getClientDetail() {

        ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
        resource.setClientId(oAuth2TokenCofigurationProperties.getClientId());
        resource.setClientSecret(oAuth2TokenCofigurationProperties.getClientSecret());
        resource.setGrantType("client_credentials");
        return resource;

    }

    /**
     * 默认组织oauth2 密码授权模式 认证信息集合的对象
     *
     * @return
     */
    private ResourceOwnerPasswordResourceDetails getUserDetail() {

        ResourceOwnerPasswordResourceDetails resource = getDetail();
        resource.setUsername(oAuth2TokenCofigurationProperties.getUserName());
        resource.setPassword(oAuth2TokenCofigurationProperties.getPassword());
        resource.setGrantType("password");

        return resource;

    }


    /**
     * 组织oauth2 客户端授权模式  认证信息集合的对象
     *
     * @return
     */
    private ResourceOwnerPasswordResourceDetails getDetail() {
        ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();
        //List<String> scopes = new ArrayList<String>();
        //scopes.add(oAuth2TokenCofigurationProperties.getScopes());
        resource.setClientId(oAuth2TokenCofigurationProperties.getClientId());
        resource.setClientSecret(oAuth2TokenCofigurationProperties.getClientSecret());
        //resource.setGrantType(oAuth2TokenCofigurationProperties.getGrantType());
        //resource.setScope(scopes);

        return resource;
    }

    /**
     * 获得token(密码模式)
     *
     * @param username
     * @param password
     * @return
     */
    public Map<String,Object> getAccessToken(String username, String password) {

        Map<String,Object> map =  new HashMap();
        try {
            AccessTokenRequest accessTokenRequest = new DefaultAccessTokenRequest();
            OAuth2ClientContext oAuth2ClientContext = new DefaultOAuth2ClientContext(accessTokenRequest);
            //通过负载均衡获取 host
            String host = this.getAuthHost();
            ResourceOwnerPasswordResourceDetails client = this.getDetail(username, password);
            client.setAccessTokenUri(host + "/oauth/token");
            OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(client, oAuth2ClientContext);
            //log.info("[用户登录][系统安全][{}]数据：token",new Gson().toJson(restTemplate.getAccessToken()));
            map.put("token", restTemplate.getAccessToken());
        } catch (NullPointerException e) {
            log.warn("[用户登录][系统安全][{}]数据：登录失败",username);
            //e.printStackTrace();
            map.put("error", ResponseCode.USER_NOT_EXIST);
        } catch (Exception e) {
            log.warn("[用户登录][系统安全][{}]数据：登录失败",username);
            //e.printStackTrace();
            if(e.getMessage().contains("Access token denied.")){
                map.put("error", ResponseCode.USER_PASSWORD_NULL);
            }
            if(e.getMessage().contains("Error requesting access token.")){
                map.put("error", ResponseCode.USER_NOT_EXIST);
            }
        }

        return map;
    }

    /**
     * 通过refreshToken 重新获取token
     *
     * @param refreshToken
     * @return
     */
    public OAuth2AccessToken refreshAccessToken(String refreshToken) {

        ResourceOwnerPasswordAccessTokenProvider provider = new ResourceOwnerPasswordAccessTokenProvider();
        MpmOAuth2RefreshToken oAuth2RefreshToken = new MpmOAuth2RefreshToken();
        oAuth2RefreshToken.setRefreshToken(refreshToken);
        OAuth2AccessToken accessToken = provider.refreshAccessToken(getDetail(), oAuth2RefreshToken, new DefaultAccessTokenRequest());
        return accessToken;
    }

    /**
     * 客户端申请校验
     *
     * @param tokenValue
     * @return
     */
    public OAuth2Authentication checkAccessToken(String tokenValue) {
        if (StringUtils.isEmpty(tokenValue)) {
            return null;
        }
//        try {
//            RestTemplate restTemplate = new RestTemplate();
//            OAuth2Authentication oAuth2Authentication = restTemplate.getForObject(oAuth2TokenCofigurationProperties.getCheckTokenUri() + "?token=" + tokenValue, OAuth2Authentication.class);
//            return oAuth2Authentication;
//        } catch (Exception e) {
//            log.error("checkTokenInOauth2Client failure:", e);
//            return null;
//        }
        return null;
    }

    public OAuth2RestTemplate getOAuth2RestTemplate() {

        try {
            AccessTokenRequest accessTokenRequest = new DefaultAccessTokenRequest();
            OAuth2ClientContext oAuth2ClientContext = new DefaultOAuth2ClientContext(accessTokenRequest);

            //通过负载均衡获取 host
            String host = this.getAuthHost();
            ResourceOwnerPasswordResourceDetails client = this.getUserDetail();
            client.setAccessTokenUri(host + "/oauth/token");

            OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(client, oAuth2ClientContext);

            //log.info("启动token：{}", restTemplate.getAccessToken());
            return restTemplate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 组织加载系统角色、资源的url
     *
     * @return
     */
    public String getAccessTokenUrl() {

        //通过负载均衡获取 host
        String host = this.getAuthHost();
        String uri = host + "/roleAuth/getMenus/" + systemProperties.getSystemMarking();
        return uri;
    }

    public String getAuthHost() {

        String host = "";

        //通过负载均衡获取 host
        ServiceInstance serviceInstance = loadBalancerClient.choose("smoc-auth");
        if (null != serviceInstance) {
            host = String.format("http://%s:%s%s", serviceInstance.getHost(), serviceInstance.getPort(), "/auth");
            //log.info("负载均衡获取 host:" + host);
        } else {
            host = String.format("http://%s%s",systemProperties.getAuthUri(), "/auth");
            //log.info("默认 host:" + host);
        }

        return host;
    }


}
