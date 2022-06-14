package com.smoc.cloud.filters.grpc.filters.utils;

public class RedisFilterConstant {

    //布隆过滤器 系统白名单
    public static final String REDIS_BLOOM_FILTERS_SYSTEM_WHITE = "redis:bloom:filters:system:white:system_white_list";
    //布隆过滤器 系统投诉黑名单
    public static final String REDIS_BLOOM_FILTERS_SYSTEM_BLACK_COMPLAINT = "redis:bloom:filters:system:black:system_black_complaint";
    //布隆过滤器 回T黑名单
    public static final String REDIS_BLOOM_FILTERS_SYSTEM_BLACK_T = "redis:bloom:filters:system:black:system_black_t_list";

}
