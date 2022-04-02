package com.smoc.cloud.common.smoc.message;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
public class MessageAccountValidator {

    private String accountId;

    private String enterpriseId;

    private String businessType;

    private String infoType;

    private String carrier;

    private String protocol;

    private String payType;

    private String maskArea;

    private String dayLimit;

    private String sendLimit;

    private String accountStatus;

    private String createdTime;

    private BigDecimal accountUsableSum;

    private String startDate;

    private String endDate;
}
