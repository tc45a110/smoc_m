package com.somc.cloud.gateway.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Slf4j
@Setter
@Getter
@Component
@ConfigurationProperties(value = IntelligenceProperties.PREFIX)
public class IntelligenceProperties {

    public static final String PREFIX = "com.smoc.cloud.gateway.configuration.intelligence";

    private String account;

    private String password;

    private String domain;

    private String viewAddress;

    private String serverAddress;

    private String resourceFileRootPath;

    //回执是否验证签名
    private Boolean callbackIsSign;

}
