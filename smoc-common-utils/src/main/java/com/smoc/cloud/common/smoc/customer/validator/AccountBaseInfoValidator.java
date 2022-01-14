package com.smoc.cloud.common.smoc.customer.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Setter
@Getter
public class AccountBaseInfoValidator {
    private String accountId;
    private String enterpriseId;
    private String accountName;
    private String account;
    private String accountPassword;
    private String businessType;
    private String carrier;
    private String infoType;
    private String extendCode;
    private Integer randomExtendCodeLength;
    private String accountChannelType;
    private String poistCarrier;
    private String transferType;
    private String accountProcess;
    private String accountStauts;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;


}
