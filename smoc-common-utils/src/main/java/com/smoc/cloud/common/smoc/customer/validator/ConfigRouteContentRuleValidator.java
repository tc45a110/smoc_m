package com.smoc.cloud.common.smoc.customer.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
public class ConfigRouteContentRuleValidator {

    private String id;
    private String accountId;
    private String businessId;
    private String businessType;
    private String carrier;
    private String areaCodes;
    private String routeContent;
    private String channelId;
    private String routeCode;
    private String routeStatus;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private Date updatedTime;

    private String mobileNum;
    private Integer minContent;
    private Integer maxContent;
    private String routeReverseContent;

    private String accountName;
    private String channelName;
    private String errorRemark;
}
