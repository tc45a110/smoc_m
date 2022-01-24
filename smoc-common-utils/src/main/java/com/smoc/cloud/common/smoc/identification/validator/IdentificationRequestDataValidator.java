package com.smoc.cloud.common.smoc.identification.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class IdentificationRequestDataValidator {

    private String id;
    private String identificationAccount;
    private String orderNo;
    private String orderType;
    private String requestData;
    private String responceData;
    private String createdBy;
    private String createdTime;

    private String enterpriseName;
}
