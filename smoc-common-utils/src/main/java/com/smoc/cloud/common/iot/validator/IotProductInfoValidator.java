package com.smoc.cloud.common.iot.validator;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class IotProductInfoValidator {

    private String id;

    private String productName;

    private String productType;

    private BigDecimal cardsChanging;

    private BigDecimal productPoolSize;

    private String changingCycle;

    private BigDecimal cycleQuota;

    private BigDecimal aboveQuotaChanging;

    private Integer productCardsNum;

    private String remark;

    private String useStatus;

    private String productStatus;

    private String createdBy;

    private String createdTime;

    private String updatedBy;

    private Date updatedTime;

}
