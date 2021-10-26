package com.smoc.cloud.admin.remote.configuration;

import com.smoc.cloud.admin.remote.client.*;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * feign 客户端配置
 * 2019/5/11 00:03
 */
@Slf4j
@Configuration
public class MpmAuthFeignConfiguration {

    private String mpmAuth = "http://smoc-auth/auth";

    //加载ribbon的配置文件
    public MpmAuthFeignConfiguration() {

        log.info("[系统启动][加载smoc-auth]数据:FeignClient到spring IOC容器进行对象管理");
    }

    @Bean
    UsersFeignClient usersFeignClient() {

        UsersFeignClient usersFeignClient =
                Feign.builder()
                        .client(new OkHttpClient())
                        .contract(new SpringMvcContract())
                        .encoder(new GsonEncoder())
                        .decoder(new GsonDecoder())
                        .logger(new Slf4jLogger())
                        .logLevel(Logger.Level.FULL)
                        .target(UsersFeignClient.class, mpmAuth);

        return usersFeignClient;

    }

    @Bean
    ClientsFeignClient clientsFeignClient() {

        ClientsFeignClient clientsFeignClient =
                Feign.builder()
                        .client(new OkHttpClient())
                        .contract(new SpringMvcContract())
                        .encoder(new GsonEncoder())
                        .decoder(new GsonDecoder())
                        .logger(new Slf4jLogger())
                        .logLevel(Logger.Level.FULL)
                        .target(ClientsFeignClient.class, mpmAuth);

        return clientsFeignClient;

    }

    @Bean
    MenusFeignClient menusFeignClient() {

        MenusFeignClient menusFeignClient =
                Feign.builder()
                        .client(new OkHttpClient())
                        .contract(new SpringMvcContract())
                        .encoder(new GsonEncoder())
                        .decoder(new GsonDecoder())
                        .logger(new Slf4jLogger())
                        .logLevel(Logger.Level.FULL)
                        .target(MenusFeignClient.class, mpmAuth);

        return menusFeignClient;

    }

    @Bean
    RoleAuthFeignClient roleAuthFeignClient() {

        RoleAuthFeignClient roleAuthFeignClient =
                Feign.builder()
                        .client(new OkHttpClient())
                        .contract(new SpringMvcContract())
                        .encoder(new GsonEncoder())
                        .decoder(new GsonDecoder())
                        .logger(new Slf4jLogger())
                        .logLevel(Logger.Level.FULL)
                        .target(RoleAuthFeignClient.class, mpmAuth);

        return roleAuthFeignClient;

    }

    @Bean
    RolesFeignClient rolesFeignClient() {

        RolesFeignClient rolesFeignClient =
                Feign.builder()
                        .client(new OkHttpClient())
                        .contract(new SpringMvcContract())
                        .encoder(new GsonEncoder())
                        .decoder(new GsonDecoder())
                        .logger(new Slf4jLogger())
                        .logLevel(Logger.Level.FULL)
                        .target(RolesFeignClient.class, mpmAuth);

        return rolesFeignClient;

    }

    @Bean
    OrgFeignClient orgFeignClient() {

        OrgFeignClient orgFeignClient =
                Feign.builder()
                        .client(new OkHttpClient())
                        .contract(new SpringMvcContract())
                        .encoder(new GsonEncoder())
                        .decoder(new GsonDecoder())
                        .logger(new Slf4jLogger())
                        .logLevel(Logger.Level.FULL)
                        .target(OrgFeignClient.class, mpmAuth);

        return orgFeignClient;

    }

    @Bean
    SystemFeignClient systemFeignClient() {

        SystemFeignClient systemFeignClient =
                Feign.builder()
                        .client(new OkHttpClient())
                        .contract(new SpringMvcContract())
                        .encoder(new GsonEncoder())
                        .decoder(new GsonDecoder())
                        .logger(new Slf4jLogger())
                        .logLevel(Logger.Level.FULL)
                        .target(SystemFeignClient.class, mpmAuth);

        return systemFeignClient;

    }

}
