package com.smoc.cloud.filters.grpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 过滤服务 gRPC服务
 */
@SpringBootApplication
@EnableDiscoveryClient
public class SmocFiltersGrpcApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmocFiltersGrpcApplication.class, args);
    }

}
