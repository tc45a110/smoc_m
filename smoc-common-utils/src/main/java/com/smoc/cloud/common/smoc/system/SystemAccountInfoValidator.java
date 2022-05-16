package com.smoc.cloud.common.smoc.system;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
public class SystemAccountInfoValidator {

    private String id;

    private String enterpriseId;

    private String account;

    private String md5HmacKey;

    private String aesKey;

    private String aesIv;

    private Integer submitLimiter;

    private String identifyIp;

    private BigDecimal price;

    private BigDecimal secondPrice;

    private BigDecimal thirdPrice;

    private BigDecimal grantingCredit;

    private String businessType;

    private String accountType;

    private String isGateway;

    private String accountStatus;

    private String createdBy;

    private String createdTime;

    private String updatedBy;

    private Date updatedTime;

    private String enterpriseName;
}
