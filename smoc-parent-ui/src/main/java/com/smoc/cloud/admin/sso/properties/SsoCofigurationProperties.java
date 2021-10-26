package com.smoc.cloud.admin.sso.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * OAuth2  Token 属性配置
 * 2019/4/14 15:26
 **/
@Slf4j
@Component
@ConfigurationProperties(prefix = "com.manpuman.cloud.meip.sso")
public class SsoCofigurationProperties {

    private String apiKey;

    private String apiSecret;

    private String serverUrl;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
