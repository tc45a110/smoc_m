package com.smoc.cloud.common.iot.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class IotProductCardValidator {

    private String id;

    private String productId;

    private String cardMsisdn;

    private String cardImsi;

    private String cardIccid;

    private String status;

    private String createdBy;

    private String createdTime;
}
