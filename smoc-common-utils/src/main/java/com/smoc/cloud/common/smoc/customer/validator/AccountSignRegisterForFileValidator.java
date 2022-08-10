package com.smoc.cloud.common.smoc.customer.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class AccountSignRegisterForFileValidator {

    private String id;
    private String registerSignId;
    private String account;
    private String channelId;
    private String channelName;
    private String accessProvince;
    private String registerCarrier;
    private String registerCodeNumber;
    private String registerExtendNumber;
    private String registerSign;
    private String numberSegment;
    private String registerExpireDate;
    private String registerStatus;
    private String registerFiles;
    private String updatedBy;
    private String updatedTime;
    private String createdTime;
}
