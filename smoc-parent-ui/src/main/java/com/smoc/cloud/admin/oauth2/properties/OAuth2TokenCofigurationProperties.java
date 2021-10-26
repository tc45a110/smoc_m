package com.smoc.cloud.admin.oauth2.properties;

import com.smoc.cloud.admin.utils.DES;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * OAuth2  Token 属性配置
 * 2019/4/14 15:26
 **/
@Slf4j
@Component
@ConfigurationProperties(prefix = "com.manpuman.cloud.oauth2")
public class OAuth2TokenCofigurationProperties {

    //授权客户端ID
    private String clientId;

    //授权客户端密码
    private String clientSecret;

    //管理员用户
    private String userName;

    //管理员密码
    private String password;


    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {

        log.info("[系统启动][数据初始化]数据:客户端密码解密:{}", password);
        password = DES.bank_decrypt(password);
        this.password = password;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {

        log.info("[系统启动][数据初始化]数据:客户端密码解密:{}", clientSecret);
        clientSecret = DES.bank_decrypt(clientSecret);
        this.clientSecret = clientSecret;
    }
}
