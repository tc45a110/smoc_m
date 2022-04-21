package com.smoc.cloud.http;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * smoc htt短信发送服务
 */
@SpringBootApplication
@EnableFeignClients
@EnableScheduling // 开启定时任务
@EnableAsync
class SmocHttpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmocHttpServerApplication.class, args);
    }

}
