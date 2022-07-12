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
    private long totalSendQuantity;
    private long totalSubmitQuantity;
    private BigDecimal totalAmount;
    private long totalNoReportQuantity;
    private BigDecimal price;
    private BigDecimal carrierTotalAmount;
    private long carrierTotalSendQuantity;
    private Integer carrierTotalSubmitQuantity;
    private Integer carrierTotalNoReportQuantity;
    private String channelPeriodStatus;
    private String status;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

    //条数差额
    private long quantityDifference;
    //条数差额比列
    private String quantityDifferenceRate;
    //金额差额
    private BigDecimal amountDifference;
}
