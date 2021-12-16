package com.smoc.cloud.common.smoc.configuate.qo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
public class ChannelBasicInfoQo {
    private String channelId;
    private String channelName;
    private String carrier;
    private String businessType;
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
    private BigDecimal channelPrice;
    private String province;
}
