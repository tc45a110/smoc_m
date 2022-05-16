package com.smoc.cloud.common.redis;

public class RedisConstant {

    //防止重放攻击key
    public static final String NONCE = "gateway:nonce:";

    //认证服务的订单查重
    public static final String ORDERS = "gateway:identification:orders:";

    //同步到网关的账户key
    public static final String HTTP_SERVER_KEY = "gateway:http:server:account:";

    //redis限流时候，用到的临时限流key
    public static final String HTTP_SERVER_MESSAGE_LIMITER = "gateway:http:server:request:limiter:";

    //redis限流时候，用到的临时限流key
    public static final String HTTP_SERVER_SUBMIT_LIMITER = "gateway:http:server:submit:limiter:";

//    public static final String HTTP_SERVER_ORDERS = "gateway:http:server:orders:";
}
