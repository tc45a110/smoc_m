package com.smoc.cloud.finance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "finance_account_share_detail")
public class FinanceAccountShareDetail {
    @Id
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    @Column(name = "SHARE_ACCOUNT_ID", nullable = false, length = 32)
    private String shareAccountId;

    @Column(name = "ACCOUNT_ID", nullable = false)
    private String accountId;

    @Column(name = "IS_USABLE_SUM_POOL", nullable = false, length = 1)
    private String isUsableSumPool;

    @Column(name = "IS_FREEZE_SUM_POOL", nullable = false, length = 1)
    private String isFreezeSumPool;

    @Column(name = "USABLE_SUM_POOL", nullable = false, precision = 24, scale = 6)
    private BigDecimal usableSumPool;

    @Column(name = "FREEZE_SUM_POOL", nullable = false, precision = 24, scale = 6)
    private BigDecimal freezeSumPool;

    @Column(name = "SHARE_STATUS", length = 1)
    private String shareStatus;

    @Column(name = "CREATED_BY", length = 32)
    private String createdBy;

    @Column(name = "CREATED_TIME")
    private Instant createdTime;

    @Column(name = "UPDATED_BY", length = 32)
    private String updatedBy;

    @Column(name = "UPDATED_TIME")
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

    public String getShareStatus() {
        return shareStatus;
    }

    public void setShareStatus(String shareStatus) {
        this.shareStatus = shareStatus;
    }

    public BigDecimal getFreezeSumPool() {
        return freezeSumPool;
    }

    public void setFreezeSumPool(BigDecimal freezeSumPool) {
        this.freezeSumPool = freezeSumPool;
    }

    public BigDecimal getUsableSumPool() {
        return usableSumPool;
    }

    public void setUsableSumPool(BigDecimal usableSumPool) {
        this.usableSumPool = usableSumPool;
    }

    public String getIsFreezeSumPool() {
        return isFreezeSumPool;
    }

    public void setIsFreezeSumPool(String isFreezeSumPool) {
        this.isFreezeSumPool = isFreezeSumPool;
    }

    public String getIsUsableSumPool() {
        return isUsableSumPool;
    }

    public void setIsUsableSumPool(String isUsableSumPool) {
        this.isUsableSumPool = isUsableSumPool;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getShareAccountId() {
        return shareAccountId;
    }

    public void setShareAccountId(String shareAccountId) {
        this.shareAccountId = shareAccountId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}