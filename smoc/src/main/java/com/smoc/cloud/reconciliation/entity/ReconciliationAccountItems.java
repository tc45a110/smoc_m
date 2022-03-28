package com.smoc.cloud.reconciliation.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "reconciliation_account_items")
public class ReconciliationAccountItems {
    private String id;
    private String accountPeriodId;
    private String accountPeriod;
    private String accountId;
    private String carrier;
    private int totalSendQuantity;
    private int totalSubmitQuantity;
    private BigDecimal totalAmount;
    private int totalNoReportQuantity;
    private String chargeType;
    private String accountPeriodStatus;
    private String status;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ACCOUNT_PERIOD_ID")
    public String getAccountPeriodId() {
        return accountPeriodId;
    }

    public void setAccountPeriodId(String accountPeriodId) {
        this.accountPeriodId = accountPeriodId;
    }

    @Basic
    @Column(name = "ACCOUNT_PERIOD")
    public String getAccountPeriod() {
        return accountPeriod;
    }

    public void setAccountPeriod(String accountPeriod) {
        this.accountPeriod = accountPeriod;
    }

    @Basic
    @Column(name = "ACCOUNT_ID")
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Basic
    @Column(name = "CARRIER")
    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    @Basic
    @Column(name = "TOTAL_SEND_QUANTITY")
    public int getTotalSendQuantity() {
        return totalSendQuantity;
    }

    public void setTotalSendQuantity(int totalSendQuantity) {
        this.totalSendQuantity = totalSendQuantity;
    }

    @Basic
    @Column(name = "TOTAL_SUBMIT_QUANTITY")
    public int getTotalSubmitQuantity() {
        return totalSubmitQuantity;
    }

    public void setTotalSubmitQuantity(int totalSubmitQuantity) {
        this.totalSubmitQuantity = totalSubmitQuantity;
    }

    @Basic
    @Column(name = "TOTAL_AMOUNT")
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Basic
    @Column(name = "TOTAL_NO_REPORT_QUANTITY")
    public int getTotalNoReportQuantity() {
        return totalNoReportQuantity;
    }

    public void setTotalNoReportQuantity(int totalNoReportQuantity) {
        this.totalNoReportQuantity = totalNoReportQuantity;
    }

    @Basic
    @Column(name = "CHARGE_TYPE")
    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    @Basic
    @Column(name = "ACCOUNT_PERIOD_STATUS")
    public String getAccountPeriodStatus() {
        return accountPeriodStatus;
    }

    public void setAccountPeriodStatus(String accountPeriodStatus) {
        this.accountPeriodStatus = accountPeriodStatus;
    }

    @Basic
    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "CREATED_BY")
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Basic
    @Column(name = "CREATED_TIME")
    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @Basic
    @Column(name = "UPDATED_BY")
    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Basic
    @Column(name = "UPDATED_TIME")
    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReconciliationAccountItems that = (ReconciliationAccountItems) o;
        return totalSendQuantity == that.totalSendQuantity &&
                totalSubmitQuantity == that.totalSubmitQuantity &&
                totalNoReportQuantity == that.totalNoReportQuantity &&
                Objects.equals(id, that.id) &&
                Objects.equals(accountPeriodId, that.accountPeriodId) &&
                Objects.equals(accountPeriod, that.accountPeriod) &&
                Objects.equals(accountId, that.accountId) &&
                Objects.equals(carrier, that.carrier) &&
                Objects.equals(totalAmount, that.totalAmount) &&
                Objects.equals(chargeType, that.chargeType) &&
                Objects.equals(accountPeriodStatus, that.accountPeriodStatus) &&
                Objects.equals(status, that.status) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountPeriodId, accountPeriod, accountId, carrier, totalSendQuantity, totalSubmitQuantity, totalAmount, totalNoReportQuantity, chargeType, accountPeriodStatus, status, createdBy, createdTime, updatedBy, updatedTime);
    }
}
