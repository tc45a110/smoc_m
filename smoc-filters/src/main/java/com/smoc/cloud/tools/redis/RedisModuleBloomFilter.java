package com.smoc.cloud.tools.redis;

/**
 * redis BloomFilter 布隆过滤器module 基于位图算法
 * 功能：海量数据（亿级）查重
 * 优点：占用内存极少，并且插入和查询速度都足够快
 * 缺点：随着数据的增加，误判率会增加；还有无法判断数据一定存在；另外还有一个重要缺点，无法删除数据。
 */
public class RedisModuleBloomFilter {
}
