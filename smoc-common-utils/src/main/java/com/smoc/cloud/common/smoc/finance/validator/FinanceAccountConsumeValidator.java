package com.smoc.cloud.common.smoc.finance.validator;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@Setter
@Getter
public class FinanceAccountConsumeValidator {

    private String id;
    private String channelId;
    private String accountId;
    private String consumeFlowNo;
    private String consumeSource;
    private Integer consumeNum;
    private BigDecimal consumeSum;
    private Integer sucessNum;
    private BigDecimal sucessSum;
    private Integer unfreezeNum;
    private BigDecimal unfreezeSum;
    private Integer failureNum;
    private BigDecimal failureSum;
    private BigDecimal settlePrice;
    private String consumeStatus;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private Date updatedTime;
}
