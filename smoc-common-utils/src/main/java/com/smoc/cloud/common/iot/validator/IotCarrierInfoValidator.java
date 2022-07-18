package com.smoc.cloud.common.iot.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class IotCarrierInfoValidator {

    private String id;

    private String carrier;

    private String carrierName;

    private String carrierIdentifying;

    private String carrierUsername;

    private String carrierPassword;

    private String carrierServerUrl;

    private String apiType;

    private String carrierStatus;

    private String createdBy;

    private String createdTime;

    private String updatedBy;

    private Date updatedTime;


}
