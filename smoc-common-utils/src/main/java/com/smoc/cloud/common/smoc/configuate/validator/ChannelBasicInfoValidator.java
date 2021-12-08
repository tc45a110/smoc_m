package com.smoc.cloud.common.smoc.configuate.validator;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
public class ChannelBasicInfoValidator {
    private String channelId;
    private String channelName;
    private String carrier;
    private BigDecimal maxComplaintRate;
    private String accessProvince;
    private String channelProvder;
    private String infoType;
    private String businessAreaType;
    private String supportAreaCodes;
    private String maskProvince;
    private String reportEnable;
    private String priceStyle;
    private String signType;
    private String upMessageEnable;
    private String transferEnable;
    private String transferType;
    private String channelIntroduce;
    private String channelProcess;
    private String channelRunStatus;
    private String channelStatus;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private Date updatedTime;

}
