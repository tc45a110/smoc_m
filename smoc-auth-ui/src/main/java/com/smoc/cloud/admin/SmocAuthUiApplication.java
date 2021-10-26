package com.smoc.cloud.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

/**
 * 满扑满管理授权中心
 *
 * @EnableFeignClients 开启FeignClient功能
 * @EnableOAuth2Client 开启OAuth2客户端
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableOAuth2Client
class SmocAuthUiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmocAuthUiApplication.class, args);
    }

}
