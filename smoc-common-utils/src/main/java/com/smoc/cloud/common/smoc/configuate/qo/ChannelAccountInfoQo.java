package com.smoc.cloud.common.smoc.configuate.qo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Setter
@Getter
public class ChannelAccountInfoQo {

    private String channelId;

    private String accountId;

    private String accountName;

    private String enterpriseId;

    private String enterpriseName;

    private String carrier;

    private String businessType;

    private String infoType;

    private String accountStatus;

    //类型：ACCOUNT_CHANNEL、ACCOUNT_CHANNEL_GROUP
    private String configType;
}
