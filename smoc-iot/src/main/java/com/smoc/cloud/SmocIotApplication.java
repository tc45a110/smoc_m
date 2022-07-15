package com.smoc.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * smoc-iot 物联网卡服务接口
 */
@SpringBootApplication
@EnableFeignClients
@EnableAsync
class SmocIotApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmocIotApplication.class, args);
    }

}
