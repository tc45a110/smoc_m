package com.smoc.cloud;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * smoc身份认证服务
 */
@SpringBootApplication
@EnableFeignClients
@EnableAsync
class SmocIdentifyApplication {


    public static void main(String[] args) {
        SpringApplication.run(SmocIdentifyApplication.class, args);
    }

}
