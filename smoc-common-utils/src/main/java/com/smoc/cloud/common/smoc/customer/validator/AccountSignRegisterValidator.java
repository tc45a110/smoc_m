package com.smoc.cloud.common.smoc.customer.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class AccountSignRegisterValidator {

    private String id;
    private String account;
    private String sign;
    private String signExtendNumber;
    private String extendType;
    private String extendData;
    private String enterpriseId;
    private String appName;
    private String serviceType;
    private String mainApplication;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private Date updatedTime;
}
