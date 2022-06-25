package com.smoc.cloud.tools.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;


@Slf4j
@EnableCaching
@Configuration
public class RedisThreeConfiguration extends RedisConfiguration {

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

    /**
     * 配置redis连接工厂
     *
     * @Primary 默认会注入@Primary配置的组件
     */
    @Primary
    @Bean
    public RedisConnectionFactory cacheRedisConnectionFactory() {
        int database = this.database;
        int port = this.port;
        String host = this.host;
        String password = this.password;
        int timeout = this.timeout;
        return createJedisConnectionFactory(database, host, port, password, timeout);
    }


    /**
     * 配置redisTemplate
     * 注入方式使用@Resource(name="")  名称注入
     */
    @Bean(name = "redisTemplate3")
    public RedisTemplate redisTemplate3() {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(cacheRedisConnectionFactory());
        setSerializer(template);

        /**
         * 非spring注入使用RedisTemplate,需先调用afterPropertiesSet()方法
         * 不然会报：JedisConnectionFactory was not initialized through afterPropertiesSet()
         */

        template.afterPropertiesSet();
        return template;
    }
}
