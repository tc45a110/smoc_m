package com.smoc.cloud.common.smoc.reconciliation;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
public class ReconciliationCarrierItemsValidator {
    private String id;
    private String channelPeriod;
    private String channelProvder;
    private String channelId;
    private String srcId;
    private String businessType;
    private int totalSendQuantity;
    private int totalSubmitQuantity;
    private BigDecimal totalAmount;
    private int totalNoReportQuantity;
    private BigDecimal price;
    private BigDecimal carrierTotalAmount;
    private int carrierTotalSendQuantity;
    private Integer carrierTotalSubmitQuantity;
    private Integer carrierTotalNoReportQuantity;
    private String channelPeriodStatus;
    private String status;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

}
