package com.smoc.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 企信通管理中心
 *
 * @EnableFeignClients 开启FeignClient功能
 * @EnableOAuth2Client 开启OAuth2客户端
 * @EnableAsync开启异步
 */
@SpringBootApplication
@EnableFeignClients
@EnableOAuth2Client
@EnableAsync
class SmocServiceUiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmocServiceUiApplication.class, args);
    }

}
