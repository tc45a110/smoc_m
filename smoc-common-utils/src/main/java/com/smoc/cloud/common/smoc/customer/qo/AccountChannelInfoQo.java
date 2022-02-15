package com.smoc.cloud.common.smoc.customer.qo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class AccountChannelInfoQo {

    private String id;
    private String channelId;
    private String channelGroupId;
    private String accountId;
    private String carrier;
    private String channelName;
    private String channelCarrier;
    private String channelInfoType;
    private String channelIntroduce;
    private String protocol;

    private String accountChannelType;
}
