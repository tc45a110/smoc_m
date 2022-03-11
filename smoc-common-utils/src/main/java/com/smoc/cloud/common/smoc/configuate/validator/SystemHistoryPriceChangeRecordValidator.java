package com.smoc.cloud.common.smoc.configuate.validator;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
public class SystemHistoryPriceChangeRecordValidator {

    private String id;
    private String changeType;
    private String businessId;
    private String priceArea;
    private String startDate;
    private BigDecimal changePrice;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private Date updatedTime;
}
