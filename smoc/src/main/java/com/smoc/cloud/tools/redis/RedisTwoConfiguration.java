package com.smoc.cloud.tools.redis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableCaching
public class RedisTwoConfiguration{
    Logger logger = LoggerFactory.getLogger(RedisTwoConfiguration.class);

    @Value("${spring.redis3.host}")
    private String host;

    @Value("${spring.redis3.port}")
    private int port;

    @Value("${spring.redis3.timeout}")
    private int timeout;

    @Value("${spring.redis3.password}")
    private String password;

    @Value("${spring.redis3.database}")
    private int database;

    @Bean(name = "jedisPool1")
    public JedisPool redisPoolFactory1() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password,database);
        return jedisPool;
    }
}


