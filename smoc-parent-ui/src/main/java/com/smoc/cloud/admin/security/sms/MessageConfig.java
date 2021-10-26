package com.smoc.cloud.admin.security.sms;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 发送短信
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "spring.message")
public class MessageConfig {

    //用户名
    public String username;

    public String password;

    //模板ID
    public String templateid;

    public String url;

}
