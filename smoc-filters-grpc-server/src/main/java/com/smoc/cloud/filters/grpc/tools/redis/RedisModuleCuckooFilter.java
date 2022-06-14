package com.smoc.cloud.filters.grpc.tools.redis;

import com.smoc.cloud.filters.grpc.filters.utils.RedisFilterConstant;
import io.rebloom.client.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * redis CuckooFilter 布谷鸟过滤器module 基于位图算法
 * 功能：海量数据（亿级）查重
 * 优点：占用内存极少，并且插入和查询速度都足够快
 * 缺点：随着数据的增加，误判率会增加；还有无法判断数据一定存在；另外还有一个重要缺点，无法删除数据。
 */
@Slf4j
@Service
public class RedisModuleCuckooFilter {

    @Autowired
    private JedisPool jedisPool;


    /**
     * 手机号是否存在过滤器中
     *
     * @param filterName 过滤器名称
     * @param phone      手机号
     * @return true 表示存在
     */
    public boolean isExist(String filterName, String phone) {

        boolean booleans = false;
        try {
            //log.info("[名单过滤]数据：应用过滤器：{}，开始：{}", filterName, System.currentTimeMillis());
            booleans = getClient().cfExists(filterName, phone);
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
     * @param phone      要添加的手机号
     * @return
     */
    public Boolean addFilter(String filterName, String phone) {
        Boolean booleans = false;
        try {
            log.info("[过滤器添加数据]数据：应用过滤器：{}，开始：{}", filterName, System.currentTimeMillis());
            booleans = this.getClient().cfAddNx(filterName, phone);
            log.info("[过滤器添加数据]数据：应用过滤器：{}，结束：{}", filterName, System.currentTimeMillis());
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
    public List<Boolean> addFilter(String filterName, String[] phones) {

        if (null == phones || phones.length > 0) {
            return new ArrayList<>();
        }
        List<Boolean> booleans = new ArrayList<>();
        try {
            log.info("[过滤器添加数据]数据：应用过滤器：{}，开始：{}", filterName, System.currentTimeMillis());
            booleans = this.getClient().cfInsertNx(filterName, phones);
            log.info("[过滤器添加数据]数据：应用过滤器：{}，结束：{}", filterName, System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return booleans;
    }

    /**
     * 数据从过滤器删除
     *
     * @param filterName 过滤器名称
     * @param phone      要删除的手机号
     * @return
     */
    public boolean deleteFilter(String filterName, String phone) {

        if (null == phone) {
            return false;
        }

        boolean booleans = false;
        try {
            log.info("[过滤器删除数据]数据：应用过滤器：{}，开始：{}", filterName, System.currentTimeMillis());
            booleans = this.getClient().cfDel(filterName, phone);
            log.info("[过滤器删除数据]数据：应用过滤器：{}，结束：{}", filterName, System.currentTimeMillis());
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
            //初始化过滤器 系统白名单
            client.createFilter(RedisFilterConstant.REDIS_BLOOM_FILTERS_SYSTEM_WHITE, 100000, 0.0001);
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
