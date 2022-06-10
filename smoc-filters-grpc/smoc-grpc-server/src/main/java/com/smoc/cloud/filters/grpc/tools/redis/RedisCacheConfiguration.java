package com.smoc.cloud.filters.grpc.tools.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableCaching
public class RedisCacheConfiguration extends CachingConfigurerSupport {

    @Value("${spring.redis2.host}")
    private String host;

    @Value("${spring.redis2.port}")
    private int port;

    @Value("${spring.redis2.timeout}")
    private int timeout;

    @Value("${spring.redis2.password}")
    private String password;

    @Value("${spring.redis2.database}")
    private int database;

    @Bean
    public JedisPool redisPoolFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password,database);
        return jedisPool;
    }
}


