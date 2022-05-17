package com.smoc.cloud.identity.configuration;

import com.smoc.cloud.common.gateway.utils.AESConstUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 配置文件
 */
@Slf4j
@Component
@ConfigurationProperties(prefix = "com.smoc.cloud.identification.configuration")
public class IdentificationConfigurationProperties {


    private String appKey;

    private String appScrect;

    private String serviceUri;


    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        log.info("appKey:{}",appKey);
        this.appKey = appKey;
    }

    public String getAppScrect() {
        return appScrect;
    }

    public void setAppScrect(String appScrect) {
        log.info("appScrect:{}",appScrect);
        this.appScrect = appScrect;
    }

    public String getServiceUri() {
        return serviceUri;
    }

    public void setServiceUri(String serviceUri) {
        log.info("serviceUri:{}",serviceUri);
        this.serviceUri = serviceUri;
    }
}
