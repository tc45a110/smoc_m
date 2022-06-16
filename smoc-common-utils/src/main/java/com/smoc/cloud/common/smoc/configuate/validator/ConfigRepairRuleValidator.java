package com.smoc.cloud.common.smoc.configuate.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Setter
@Getter
public class ConfigRepairRuleValidator {
    private String id;
    private String businessId;
    private String businessType;
    private String repairCode;
    private int repairData;
    private String repairStatus;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private Date updatedTime;

}
