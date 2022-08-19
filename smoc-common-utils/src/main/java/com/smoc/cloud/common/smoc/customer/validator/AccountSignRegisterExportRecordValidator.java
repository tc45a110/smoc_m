package com.smoc.cloud.common.smoc.customer.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class AccountSignRegisterExportRecordValidator {

    private String id;

    private String registerOrderNo;

    private String carrier;

    private Integer registerNumber;

    private String registerStatus;

    private String createdBy;

    private String createdTime;
}
