package com.smoc.cloud.common.smoc.configuate.qo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 通道、接口信息
 */
@Setter
@Getter
public class ChannelInterfaceInfoQo {
    private String channelId;

    private String channelName;

    private String carrier;

    private String businessType;

    private String infoType;

    private String channelStatus;

    private String channelRunStatus;


    private String channelAccessAccount;

    private String channelAccessPassword;

    private String channelServiceUrl;

    private String spId;

    private String srcId;

    private String businessCode;

    private Integer connectNumber;

    private Integer maxSendSecond;

    private Integer heartbeatInterval;

    private String protocol;

}
