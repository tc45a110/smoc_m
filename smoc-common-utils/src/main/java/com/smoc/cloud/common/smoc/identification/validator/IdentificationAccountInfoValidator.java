package com.smoc.cloud.common.smoc.identification.validator;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
public class IdentificationAccountInfoValidator {

    private String id;
    private String enterpriseId;
    private String identificationAccount;
    private String md5HmacKey;
    private String aesKey;
    private String aesIv;
    private BigDecimal identificationPrice;
    private BigDecimal identificationFacePrice;
    private String accountType;
    private String accountStatus;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private Date updatedTime;

    private String enterpriseName;
}
