package com.smoc.cloud.tools.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis配置  使用Jackson2JsonRedisSerializer 序列化
 * 2019/4/4 14:52
 **/
@Configuration
@EnableCaching
public class RedisPrimaryConfiguration extends RedisConfiguration {

    @Value("${spring.redis.database}")
    private int dbIndex;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.timeout}")
    private int timeout;

    /**
     * 配置redis连接工厂
     */
    @Bean
    public RedisConnectionFactory defaultRedisConnectionFactory() {
        return createJedisConnectionFactory(dbIndex, host, port, password, timeout);
    }

    /**
     * 配置redisTemplate
     * 注入方式使用@Resource(name="") 方式注入
     */
    @Bean(name = "defaultRedisTemplate")
    public RedisTemplate defaultRedisTemplate() {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(defaultRedisConnectionFactory());
        setSerializer(template);
        /**
         * 非spring注入使用RedisTemplate,需先调用afterPropertiesSet()方法
         * 不然会报：JedisConnectionFactory was not initialized through afterPropertiesSet()
         */
        template.afterPropertiesSet();
        return template;
    }

}
