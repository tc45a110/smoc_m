package com.smoc.cloud.iot.packages.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "iot_package_used_monthly")
public class IotPackageUsedMonthly {
    @Id
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    @Column(name = "PACKAGE_ID", nullable = false, length = 32)
    private String packageId;

    @Column(name = "CHANGING_TYPE", nullable = false, length = 32)
    private String changingType;

    @Column(name = "CHANGING_CYCLE", nullable = false, length = 32)
    private String changingCycle;

    @Column(name = "PACKAGE_CHANGING", nullable = false, length = 32)
    private String packageChanging;

    @Column(name = "ABOVE_QUOTA_CHANGING", nullable = false, length = 32)
    private String aboveQuotaChanging;

    @Column(name = "PACKAGE_TEMP_AMOUNT", nullable = false, precision = 24, scale = 6)
    private BigDecimal packageTempAmount;

    @Column(name = "PACKAGE_TEMP_AMOUNT_FEE", nullable = false, precision = 24, scale = 6)
    private BigDecimal packageTempAmountFee;

    @Column(name = "CYCLE_FUNCTION_FEE", nullable = false, precision = 24, scale = 6)
    private BigDecimal cycleFunctionFee;

    @Column(name = "PACKAGE_CARDS_NUM", nullable = false)
    private Integer packageCardsNum;

    @Column(name = "SYNC_CARDS_NUM", nullable = false)
    private Integer syncCardsNum;

    @Column(name = "PACKAGE_POOL_SIZE", nullable = false, precision = 24, scale = 6)
    private BigDecimal packagePoolSize;

    @Column(name = "USED_AMOUNT", nullable = false, precision = 24, scale = 6)
    private BigDecimal usedAmount;

    @Column(name = "SURPLUS_AMOUNT", nullable = false, precision = 24, scale = 6)
    private BigDecimal surplusAmount;

    @Column(name = "PACKAGE_MONTH", nullable = false, length = 32)
    private String packageMonth;

    @Column(name = "SETTLEMENT_STATUS", nullable = false, length = 32)
    private String settlementStatus;

    @Column(name = "DATA_STATUS", nullable = false, length = 32)
    private String dataStatus;

    @Column(name = "CREATED_BY", length = 32)
    private String createdBy;

    @Column(name = "CREATED_TIME")
    private Instant createdTime;

    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(String settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public String getPackageMonth() {
        return packageMonth;
    }

    public void setPackageMonth(String packageMonth) {
        this.packageMonth = packageMonth;
    }

    public BigDecimal getSurplusAmount() {
        return surplusAmount;
    }

    public void setSurplusAmount(BigDecimal surplusAmount) {
        this.surplusAmount = surplusAmount;
    }

    public BigDecimal getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(BigDecimal usedAmount) {
        this.usedAmount = usedAmount;
    }

    public BigDecimal getPackagePoolSize() {
        return packagePoolSize;
    }

    public void setPackagePoolSize(BigDecimal packagePoolSize) {
        this.packagePoolSize = packagePoolSize;
    }

    public Integer getSyncCardsNum() {
        return syncCardsNum;
    }

    public void setSyncCardsNum(Integer syncCardsNum) {
        this.syncCardsNum = syncCardsNum;
    }

    public Integer getPackageCardsNum() {
        return packageCardsNum;
    }

    public void setPackageCardsNum(Integer packageCardsNum) {
        this.packageCardsNum = packageCardsNum;
    }

    public BigDecimal getCycleFunctionFee() {
        return cycleFunctionFee;
    }

    public void setCycleFunctionFee(BigDecimal cycleFunctionFee) {
        this.cycleFunctionFee = cycleFunctionFee;
    }

    public BigDecimal getPackageTempAmountFee() {
        return packageTempAmountFee;
    }

    public void setPackageTempAmountFee(BigDecimal packageTempAmountFee) {
        this.packageTempAmountFee = packageTempAmountFee;
    }

    public BigDecimal getPackageTempAmount() {
        return packageTempAmount;
    }

    public void setPackageTempAmount(BigDecimal packageTempAmount) {
        this.packageTempAmount = packageTempAmount;
    }

    public String getAboveQuotaChanging() {
        return aboveQuotaChanging;
    }

    public void setAboveQuotaChanging(String aboveQuotaChanging) {
        this.aboveQuotaChanging = aboveQuotaChanging;
    }

    public String getPackageChanging() {
        return packageChanging;
    }

    public void setPackageChanging(String packageChanging) {
        this.packageChanging = packageChanging;
    }

    public String getChangingCycle() {
        return changingCycle;
    }

    public void setChangingCycle(String changingCycle) {
        this.changingCycle = changingCycle;
    }

    public String getChangingType() {
        return changingType;
    }

    public void setChangingType(String changingType) {
        this.changingType = changingType;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}