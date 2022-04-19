package com.smoc.cloud.common.http.server.utils;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RedisModel {

    //业务类型
    private String businessType;

    //md5 key
    private String md5HmacKey;

    //提交速率  秒
    private Integer submitRate;

    //发送速率  秒
    private Integer sendRate;

    //客户鉴权IP
    private String ips;


}
