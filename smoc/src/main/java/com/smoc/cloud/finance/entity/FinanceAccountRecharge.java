package com.smoc.cloud.finance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "finance_account_recharge")
public class FinanceAccountRecharge {
    @Id
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    @Column(name = "ACCOUNT_ID", nullable = false, length = 32)
    private String accountId;

    @Column(name = "RECHARGE_FLOW_NO", nullable = false, length = 64)
    private String rechargeFlowNo;

    @Column(name = "RECHARGE_SOURCE", length = 32)
    private String rechargeSource;

    @Column(name = "RECHARGE_SUM", nullable = false, precision = 24, scale = 6)
    private BigDecimal rechargeSum;

    @Column(name = "RECHARGE_COST", precision = 24, scale = 6)
    private BigDecimal rechargeCost;

    @Column(name = "RECHARGE_ACCOUNT_USABLE", precision = 24, scale = 6)
    private BigDecimal rechargeAccountUsable;

    @Column(name = "CREATED_BY", nullable = false, length = 32)
    private String createdBy;

    @Column(name = "CREATED_TIME", nullable = false)
    private Date createdTime;

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public BigDecimal getRechargeAccountUsable() {
        return rechargeAccountUsable;
    }

    public void setRechargeAccountUsable(BigDecimal rechargeAccountUsable) {
        this.rechargeAccountUsable = rechargeAccountUsable;
    }

    public BigDecimal getRechargeCost() {
        return rechargeCost;
    }

    public void setRechargeCost(BigDecimal rechargeCost) {
        this.rechargeCost = rechargeCost;
    }

    public BigDecimal getRechargeSum() {
        return rechargeSum;
    }

    public void setRechargeSum(BigDecimal rechargeSum) {
        this.rechargeSum = rechargeSum;
    }

    public String getRechargeSource() {
        return rechargeSource;
    }

    public void setRechargeSource(String rechargeSource) {
        this.rechargeSource = rechargeSource;
    }

    public String getRechargeFlowNo() {
        return rechargeFlowNo;
    }

    public void setRechargeFlowNo(String rechargeFlowNo) {
        this.rechargeFlowNo = rechargeFlowNo;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}