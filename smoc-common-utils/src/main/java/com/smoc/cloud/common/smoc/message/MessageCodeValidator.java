package com.smoc.cloud.common.smoc.message;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class MessageCodeValidator {

    private String businessAccount;

    private String accountName;

    private String sign;

    private String carrier;

    private String codeStatus;

    private int codeNumber;

    private BigDecimal ratio;

    private String startDate;

    private String endDate;

    private String enterpriseId;

    private String enterpriseFlag;

}
