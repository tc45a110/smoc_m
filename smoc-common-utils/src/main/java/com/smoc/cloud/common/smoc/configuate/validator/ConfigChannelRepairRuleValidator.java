package com.smoc.cloud.common.smoc.configuate.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ConfigChannelRepairRuleValidator {
    private String id;
    private String channelId;
    private String businessId;
    private String businessType;
    private String carrier;
    private String channelRepairId;
    private String repairCode;
    private String repairStatus;
    private Integer sort;
    private String createdBy;
    private String createdTime;
    private String updatedBy;

    private String channelRepairName;
}
