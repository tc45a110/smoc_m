package com.smoc.cloud.tools.redis;

import com.smoc.cloud.common.filters.utils.RedisFilterConstant;
import io.rebloom.client.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;

/**
 * redis BloomFilter 布隆过滤器module 基于位图算法
 * 功能：海量数据（亿级）查重
 * 优点：占用内存极少，并且插入和查询速度都足够快
 * 缺点：随着数据的增加，误判率会增加；还有无法判断数据一定存在；另外还有一个重要缺点，无法删除数据。
 */
@Slf4j
@Service
public class RedisModuleBloomFilter {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 手机号是否存在检测
     *
     * @param filterName 过滤器名称
     * @param phone      手机号
     * @return true 表示存在
     */
    public boolean isExist(String filterName, String phone) {

        boolean booleans = false;
        try {
            //log.info("[名单过滤]数据：应用过滤器：{}，开始：{}", filterName, System.currentTimeMillis());
            booleans = getClient().exists(filterName, phone);
            //log.info("[名单过滤]数据：应用过滤器：{}，结束：{}", filterName, System.currentTimeMillis());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return booleans;
    }

    /**
     * 多手机号 是否存在检测
     *
     * @param filterName
     * @param phones     手机号数组
     * @return 返回对应的数组，true 表示存在
     */
    public boolean[] isExist(String filterName, String[] phones) {

        if (null == phones || phones.length < 1) {
            return null;
        }

        if (phones.length > 1000000) {
            return null;
        }

        boolean[] booleans = null;
        try {

            //log.info("[名单过滤]数据：应用过滤器：{}，开始：{}", filterName, System.currentTimeMillis());
            booleans = getClient().existsMulti(filterName, phones);
            //log.info("[名单过滤]数据：应用过滤器：{}，结束：{}", filterName, System.currentTimeMillis());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return booleans;
    }


    /**
     * 数据添加到过滤器
     *
     * @param filterName 过滤器名称
     * @param phones     要添加的手机号
     * @return
     */
    public boolean[] addFilter(String filterName, String[] phones) {

        if (null == phones || phones.length < 1) {
            return null;
        }

        boolean[] booleans = null;
        try {
            log.info("[过滤器添加数据]数据：应用过滤器：{}，开始：{}", filterName, System.currentTimeMillis());
            booleans = this.getClient().addMulti(filterName, phones);
            log.info("[过滤器添加数据]数据：应用过滤器：{}，结束：{}", filterName, System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return booleans;
    }

    /**
     * 初始化过滤器
     */
    public void initFilter() {

        try {
            Client client = this.getClient();

            log.info("[初始化过滤器]数据：应用过滤器开始：{}", System.currentTimeMillis());
            //初始化过滤器 投诉
            client.createFilter(RedisFilterConstant.REDIS_BLOOM_FILTERS_SYSTEM_BLACK_COMPLAINT, 500000, 0.0001);
            //初始化过滤器 回T
            client.createFilter(RedisFilterConstant.REDIS_BLOOM_FILTERS_SYSTEM_BLACK_T, 200000, 0.0001);
            log.info("[初始化过滤器]数据：应用过滤器结束：{}", System.currentTimeMillis());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Client getClient() {

        Client client = new Client(jedisPool);
        return client;
    }

    public Client getClient(JedisPool pool) {

        Client client = new Client(pool);

        return client;
    }
}
