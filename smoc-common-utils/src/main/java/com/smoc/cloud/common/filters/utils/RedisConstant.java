package com.smoc.cloud.common.filters.utils;

/**
 * redis常量
 */
public class RedisConstant {

    /**
     * 系统级别过滤配置数据加载
     */
    //系统配置-系统参数前缀
    public static final String FILTERS_CONFIG_SYSTEM = "filters:config:system:";
    //系统黑名单
    public static final String FILTERS_CONFIG_SYSTEM_BLACK = FILTERS_CONFIG_SYSTEM + "list:black";
    //系统白名单
    public static final String FILTERS_CONFIG_SYSTEM_WHITE = FILTERS_CONFIG_SYSTEM + "list:white";

    //系统关键词
    public static final String FILTERS_CONFIG_SYSTEM_WORDS = FILTERS_CONFIG_SYSTEM + "words:";
    //系统敏感词
    public static final String FILTERS_CONFIG_SYSTEM_WORDS_SENSITIVE = FILTERS_CONFIG_SYSTEM_WORDS + "sensitive";
    //系统审核词
    public static final String FILTERS_CONFIG_SYSTEM_WORDS_CHECK = FILTERS_CONFIG_SYSTEM_WORDS + "check";
    //系统白词
    public static final String FILTERS_CONFIG_SYSTEM_WORDS_WHITE = FILTERS_CONFIG_SYSTEM_WORDS + "white";


    /**
     * 账户级过滤
     */
    //账户参数-参数前缀
    public static final String FILTERS_CONFIG_ACCOUNT = "filters:config:account:";

    //账户参数-号码过滤参数
    public static final String FILTERS_CONFIG_ACCOUNT_COMMON = FILTERS_CONFIG_ACCOUNT + "common:";
    //账户参数-号码过滤参数
    public static final String FILTERS_CONFIG_ACCOUNT_NUMBER = FILTERS_CONFIG_ACCOUNT + "number:";
    //账户参数-内容过滤参数
    public static final String FILTERS_CONFIG_ACCOUNT_MESSAGE = FILTERS_CONFIG_ACCOUNT + "message:";

    /**
     * 临时数据，表示有有效期，基本上都是限流、限量
     */
    //限流、限制
    public static final String FILTERS_TEMPORARY_LIMIT = "filters:temporary:limit:";
    //账户参数-运营商日限流Key  DATE 格式为 yyyyMMdd
    public static final String FILTERS_TEMPORARY_LIMIT_DAILY_CARRIER_ACCOUNT_DATE = FILTERS_TEMPORARY_LIMIT + "daily:";
    //单号码账号级发送频率限制临时限流数据
    public static final String FILTERS_TEMPORARY_LIMIT_ACCOUNT_NUMBER = FILTERS_TEMPORARY_LIMIT;
    //单号码系统级级发送频率限制临时限流数据
    public static final String FILTERS_TEMPORARY_LIMIT_SYSTEM_NUMBER = FILTERS_TEMPORARY_LIMIT+"system:";


}
