package com.smoc.cloud.filters.grpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * smoc grpc 过滤服务
 */
@SpringBootApplication
@EnableDiscoveryClient
public class SmocFiltersGrpcServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmocFiltersGrpcServerApplication.class, args);
    }

}
