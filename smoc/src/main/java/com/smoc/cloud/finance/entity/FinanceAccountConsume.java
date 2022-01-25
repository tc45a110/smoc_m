package com.smoc.cloud.finance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "finance_account_consume")
public class FinanceAccountConsume {
    @Id
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    @Column(name = "CHANNEL_ID", nullable = false, length = 32)
    private String channelId;

    @Column(name = "ACCOUNT_ID", nullable = false, length = 32)
    private String accountId;

    @Column(name = "CONSUME_FLOW_NO", nullable = false, length = 64)
    private String consumeFlowNo;

    @Column(name = "CONSUME_SOURCE", length = 32)
    private String consumeSource;

    @Column(name = "CONSUME_NUM")
    private Integer consumeNum;

    @Column(name = "CONSUME_SUM", precision = 24, scale = 6)
    private BigDecimal consumeSum;

    @Column(name = "SUCESS_NUM")
    private Integer sucessNum;

    @Column(name = "SUCESS_SUM", precision = 24, scale = 6)
    private BigDecimal sucessSum;

    @Column(name = "UNFREEZE_NUM")
    private Integer unfreezeNum;

    @Column(name = "UNFREEZE_SUM", precision = 24, scale = 6)
    private BigDecimal unfreezeSum;

    @Column(name = "FAILURE_NUM")
    private Integer failureNum;

    @Column(name = "FAILURE_SUM", precision = 24, scale = 6)
    private BigDecimal failureSum;

    @Column(name = "SETTLE_PRICE", precision = 24, scale = 6)
    private BigDecimal settlePrice;

    @Column(name = "CONSUME_STATUS", nullable = false, length = 32)
    private String consumeStatus;

    @Column(name = "CREATED_BY", nullable = false, length = 32)
    private String createdBy;

    @Column(name = "CREATED_TIME", nullable = false)
    private Date createdTime;

    @Column(name = "UPDATED_BY", nullable = false, length = 32)
    private String updatedBy;

    @Column(name = "UPDATED_TIME", nullable = false)
    private Instant updatedTime;

    public Instant getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Instant updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

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

    public String getConsumeStatus() {
        return consumeStatus;
    }

    public void setConsumeStatus(String consumeStatus) {
        this.consumeStatus = consumeStatus;
    }

    public BigDecimal getSettlePrice() {
        return settlePrice;
    }

    public void setSettlePrice(BigDecimal settlePrice) {
        this.settlePrice = settlePrice;
    }

    public BigDecimal getFailureSum() {
        return failureSum;
    }

    public void setFailureSum(BigDecimal failureSum) {
        this.failureSum = failureSum;
    }

    public Integer getFailureNum() {
        return failureNum;
    }

    public void setFailureNum(Integer failureNum) {
        this.failureNum = failureNum;
    }

    public BigDecimal getUnfreezeSum() {
        return unfreezeSum;
    }

    public void setUnfreezeSum(BigDecimal unfreezeSum) {
        this.unfreezeSum = unfreezeSum;
    }

    public Integer getUnfreezeNum() {
        return unfreezeNum;
    }

    public void setUnfreezeNum(Integer unfreezeNum) {
        this.unfreezeNum = unfreezeNum;
    }

    public BigDecimal getSucessSum() {
        return sucessSum;
    }

    public void setSucessSum(BigDecimal sucessSum) {
        this.sucessSum = sucessSum;
    }

    public Integer getSucessNum() {
        return sucessNum;
    }

    public void setSucessNum(Integer sucessNum) {
        this.sucessNum = sucessNum;
    }

    public BigDecimal getConsumeSum() {
        return consumeSum;
    }

    public void setConsumeSum(BigDecimal consumeSum) {
        this.consumeSum = consumeSum;
    }

    public Integer getConsumeNum() {
        return consumeNum;
    }

    public void setConsumeNum(Integer consumeNum) {
        this.consumeNum = consumeNum;
    }

    public String getConsumeSource() {
        return consumeSource;
    }

    public void setConsumeSource(String consumeSource) {
        this.consumeSource = consumeSource;
    }

    public String getConsumeFlowNo() {
        return consumeFlowNo;
    }

    public void setConsumeFlowNo(String consumeFlowNo) {
        this.consumeFlowNo = consumeFlowNo;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}