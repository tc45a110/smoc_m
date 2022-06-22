package com.smoc.cloud.finance.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "finance_account_refund")
public class FinanceAccountRefund {
    private String id;
    private String accountId;
    private String refundFlowNo;
    private String refundSource;
    private BigDecimal refundSum;
    private BigDecimal refundCost;
    private BigDecimal refundAccountUsable;
    private String remark;
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
    @Column(name = "ACCOUNT_ID")
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Basic
    @Column(name = "REFUND_FLOW_NO")
    public String getRefundFlowNo() {
        return refundFlowNo;
    }

    public void setRefundFlowNo(String refundFlowNo) {
        this.refundFlowNo = refundFlowNo;
    }

    @Basic
    @Column(name = "REFUND_SOURCE")
    public String getRefundSource() {
        return refundSource;
    }

    public void setRefundSource(String refundSource) {
        this.refundSource = refundSource;
    }

    @Basic
    @Column(name = "REFUND_SUM")
    public BigDecimal getRefundSum() {
        return refundSum;
    }

    public void setRefundSum(BigDecimal refundSum) {
        this.refundSum = refundSum;
    }

    @Basic
    @Column(name = "REFUND_COST")
    public BigDecimal getRefundCost() {
        return refundCost;
    }

    public void setRefundCost(BigDecimal refundCost) {
        this.refundCost = refundCost;
    }

    @Basic
    @Column(name = "REFUND_ACCOUNT_USABLE")
    public BigDecimal getRefundAccountUsable() {
        return refundAccountUsable;
    }

    public void setRefundAccountUsable(BigDecimal refundAccountUsable) {
        this.refundAccountUsable = refundAccountUsable;
    }

    @Basic
    @Column(name = "REMARK")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        FinanceAccountRefund that = (FinanceAccountRefund) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(accountId, that.accountId) &&
                Objects.equals(refundFlowNo, that.refundFlowNo) &&
                Objects.equals(refundSource, that.refundSource) &&
                Objects.equals(refundSum, that.refundSum) &&
                Objects.equals(refundCost, that.refundCost) &&
                Objects.equals(refundAccountUsable, that.refundAccountUsable) &&
                Objects.equals(remark, that.remark) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountId, refundFlowNo, refundSource, refundSum, refundCost, refundAccountUsable, remark, createdBy, createdTime, updatedBy, updatedTime);
    }
}
