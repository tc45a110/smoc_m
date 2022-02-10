package com.somc.cloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * smoc网关服务
 */
@Slf4j
@SpringBootApplication
@EnableDiscoveryClient
public class SmocGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmocGatewayApplication.class, args);
    }

}
