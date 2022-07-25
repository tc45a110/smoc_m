package com.smoc.cloud.common.iot.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class IotPackageCardValidator {

    private String id;

    private String packageId;

    private String cardMsisdn;

    private String cardImsi;

    private String cardIccid;

    private String status;

    private String createdBy;

    private String createdTime;
}
