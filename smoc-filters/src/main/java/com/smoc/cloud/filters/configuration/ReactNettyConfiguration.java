package com.smoc.cloud.filters.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorResourceFactory;

/**
 * Netty 线程调优
 */
@Slf4j
@Configuration
public class ReactNettyConfiguration {
    
    @Bean
    public ReactorResourceFactory reactorClientResourceFactory() {
        log.info("[netty]:{}", "reactor.netty.ioSelectCount");
        System.setProperty("reactor.netty.ioSelectCount", "1");
        System.setProperty("reactor.ipc.netty.selectCount", "1");
        // 这里工作线程数为2-4倍都可以。看具体情况
        System.setProperty("reactor.netty.ioWorkerCount", String.valueOf(Math.max(Runtime.getRuntime().availableProcessors() * 4, 4)));
        System.setProperty("reactor.ipc.netty.workerCount", String.valueOf(Math.max(Runtime.getRuntime().availableProcessors() * 4, 4)));
        return new ReactorResourceFactory();
    }
}
