package com.smoc.cloud.common.smoc.customer.qo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Setter
@Getter
public class AccountInfoQo {

    private String accountId;

    private String enterpriseId;

    private String enterpriseName;

    private String accountName;

    private String businessType;

    private String carrier;

    private String infoType;

    private String extendCode;

    private Integer randomExtendCodeLength;

    private String accountStatus;

    private String chargeType;

    private String payType;

    private String protocol;


}
