package com.smoc.cloud.sign.mode;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignChannel {

    //业务账号
    private String accountId;
    //通道id
    private String channelId;
    //通道名称
    private String channelName;
    //运营商
    private String carrier;
    //码号
    private String srcId;
    //接入省份
    private String accessProvince;

    //接入省份
    private String accessCity;

}
