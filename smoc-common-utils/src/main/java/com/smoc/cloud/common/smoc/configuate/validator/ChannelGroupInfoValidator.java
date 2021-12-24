package com.smoc.cloud.common.smoc.configuate.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ChannelGroupInfoValidator {
    private String productId;
    private String productName;
    private String carrier;
    private String businessType;
    private String infoType;
    private String maskProvince;
    private String channelGroupIntroduce;
    private String channelGroupProcess;
    private String channelGroupStatus;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

}
