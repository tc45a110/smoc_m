package com.smoc.cloud.common.smoc.saler.qo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerChannelInfoQo {

    private String channelId;

    private String channelName;

    private String carrier;

    private String businessType;

    private String infoType;

    private String channelStatus;

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

    private String salerId;
}
