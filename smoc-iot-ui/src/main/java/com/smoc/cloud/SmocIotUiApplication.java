package com.smoc.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

/**
 * smoc-iot 物联网卡服务
 *
 * @EnableFeignClients 开启FeignClient功能
 * @EnableOAuth2Client 开启OAuth2客户端
 * @EnableAsync开启异步
 */
@SpringBootApplication
@EnableFeignClients
@EnableOAuth2Client
@EnableAsync
class SmocIotUiApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmocIotUiApplication.class, args);
    }

}
