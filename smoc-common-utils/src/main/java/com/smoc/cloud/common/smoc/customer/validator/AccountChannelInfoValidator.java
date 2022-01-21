package com.smoc.cloud.common.smoc.customer.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class AccountChannelInfoValidator {
    private String id;
    private String accountId;
    private String configType;
    private String carrier;
    private String channelGroupId;
    private String channelId;
    private String channelPriority;
    private int channelWeight;
    private String channelSource;
    private String changeSource;
    private String channelStatus;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

    private String accountChannelType;
}
