package com.smoc.cloud.filters.grpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
public class SmocFilterGrpcClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmocFilterGrpcClientApplication.class, args);
    }
}
