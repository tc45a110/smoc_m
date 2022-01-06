package com.smoc.cloud.common.smoc.customer.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class EnterpriseWebAccountInfoValidator {
    private String id;
    private String enterpriseId;
    private String accountType;
    private String webLoginName;
    private String webLoginPassword;
    private String accountStatus;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private Date updatedTime;

}
