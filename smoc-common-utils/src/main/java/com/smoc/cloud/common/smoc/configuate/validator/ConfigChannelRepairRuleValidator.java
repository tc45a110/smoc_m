package com.smoc.cloud.common.smoc.configuate.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Setter
@Getter
public class ConfigChannelRepairRuleValidator {
    private String id;
    private String channelId;
    private String businessId;
    private String businessType;
    private String channelRepairId;
    private String repairCode;
    private String repairStatus;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;


}
