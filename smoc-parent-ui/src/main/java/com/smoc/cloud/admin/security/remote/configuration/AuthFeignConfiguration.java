package com.smoc.cloud.admin.security.remote.configuration;

import com.smoc.cloud.admin.security.remote.client.*;
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
public class AuthFeignConfiguration {

    private String mpmAuth = "http://smoc-auth/auth";

    @Bean
    SysUserFeignClient sysUserFeignClient() {

        SysUserFeignClient sysUserFeignClient =
                Feign.builder()
                        .client(new OkHttpClient())
                        .contract(new SpringMvcContract())
                        .encoder(new GsonEncoder())
                        .decoder(new GsonDecoder())
                        .logger(new Logger.ErrorLogger())
                        .logLevel(Logger.Level.FULL)
                        .target(SysUserFeignClient.class, mpmAuth);

        return sysUserFeignClient;

    }

    @Bean
    SysOrgFeignClient sysOrgFeignClient() {

        SysOrgFeignClient sysOrgFeignClient =
                Feign.builder()
                        .client(new OkHttpClient())
                        .contract(new SpringMvcContract())
                        .encoder(new GsonEncoder())
                        .decoder(new GsonDecoder())
                        .logger(new Logger.ErrorLogger())
                        .logLevel(Logger.Level.FULL)
                        .target(SysOrgFeignClient.class, mpmAuth);

        return sysOrgFeignClient;

    }

    @Bean
    UserCacheFeignClient userCacheFeignClient() {

        UserCacheFeignClient userCacheFeignClient =
                Feign.builder()
                        .client(new OkHttpClient())
                        .contract(new SpringMvcContract())
                        .encoder(new GsonEncoder())
                        .decoder(new GsonDecoder())
                        .logger(new Logger.ErrorLogger())
                        .logLevel(Logger.Level.FULL)
                        .target(UserCacheFeignClient.class, mpmAuth);

        return userCacheFeignClient;

    }

    @Bean
    DictFeignClient dictFeignClient() {

        DictFeignClient dictFeignClient =
                Feign.builder()
                        .client(new OkHttpClient())
                        .contract(new SpringMvcContract())
                        .encoder(new GsonEncoder())
                        .decoder(new GsonDecoder())
                        .logger(new Slf4jLogger())
                        .logLevel(Logger.Level.FULL)
                        .target(DictFeignClient.class, mpmAuth);

        return dictFeignClient;

    }

    @Bean
    DictTypeFeignClient dictTypeFeignClient() {

        DictTypeFeignClient dictTypeFeignClient =
                Feign.builder()
                        .client(new OkHttpClient())
                        .contract(new SpringMvcContract())
                        .encoder(new GsonEncoder())
                        .decoder(new GsonDecoder())
                        .logger(new Slf4jLogger())
                        .logLevel(Logger.Level.FULL)
                        .target(DictTypeFeignClient.class, mpmAuth);

        return dictTypeFeignClient;

    }

    @Bean
    WeixinFeignClient weixinFeignClient() {

        WeixinFeignClient weixinFeignClient =
                Feign.builder()
                        .client(new OkHttpClient())
                        .contract(new SpringMvcContract())
                        .encoder(new GsonEncoder())
                        .decoder(new GsonDecoder())
                        .logger(new Slf4jLogger())
                        .logLevel(Logger.Level.FULL)
                        .target(WeixinFeignClient.class, mpmAuth);

        return weixinFeignClient;

    }


}
