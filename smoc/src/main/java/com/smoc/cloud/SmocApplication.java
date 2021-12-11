package com.smoc.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * smoc管理系统
 */
@SpringBootApplication
@EnableFeignClients
@EnableAsync
class SmocApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmocApplication.class, args);
    }

}
