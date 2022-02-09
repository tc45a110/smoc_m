package com.smoc.cloud.finance.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "finance_account")
public class FinanceAccount {
    private String accountId;
    private String accountType;
    private String accountName;
    private BigDecimal accountTotalSum;
    private BigDecimal accountUsableSum;
    private BigDecimal accountFrozenSum;
    private BigDecimal accountConsumeSum;
    private BigDecimal accountRechargeSum;
    private BigDecimal accountCreditSum;
    private String accountStatus;
    private String isShare;
    private String shareId;
    private String enterpriseId;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

    @Id
    @Column(name = "ACCOUNT_ID")
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Basic
    @Column(name = "ACCOUNT_TYPE")
    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Basic
    @Column(name = "ACCOUNT_NAME")
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    @Basic
    @Column(name = "ACCOUNT_TOTAL_SUM")
    public BigDecimal getAccountTotalSum() {
        return accountTotalSum;
    }

    public void setAccountTotalSum(BigDecimal accountTotalSum) {
        this.accountTotalSum = accountTotalSum;
    }

    @Basic
    @Column(name = "ACCOUNT_USABLE_SUM")
    public BigDecimal getAccountUsableSum() {
        return accountUsableSum;
    }

    public void setAccountUsableSum(BigDecimal accountUsableSum) {
        this.accountUsableSum = accountUsableSum;
    }

    @Basic
    @Column(name = "ACCOUNT_FROZEN_SUM")
    public BigDecimal getAccountFrozenSum() {
        return accountFrozenSum;
    }

    public void setAccountFrozenSum(BigDecimal accountFrozenSum) {
        this.accountFrozenSum = accountFrozenSum;
    }

    @Basic
    @Column(name = "ACCOUNT_CONSUME_SUM")
    public BigDecimal getAccountConsumeSum() {
        return accountConsumeSum;
    }

    public void setAccountConsumeSum(BigDecimal accountConsumeSum) {
        this.accountConsumeSum = accountConsumeSum;
    }

    @Basic
    @Column(name = "ACCOUNT_RECHARGE_SUM")
    public BigDecimal getAccountRechargeSum() {
        return accountRechargeSum;
    }

    public void setAccountRechargeSum(BigDecimal accountRechargeSum) {
        this.accountRechargeSum = accountRechargeSum;
    }

    @Basic
    @Column(name = "ACCOUNT_CREDIT_SUM")
    public BigDecimal getAccountCreditSum() {
        return accountCreditSum;
    }

    public void setAccountCreditSum(BigDecimal accountCreditSum) {
        this.accountCreditSum = accountCreditSum;
    }

    @Basic
    @Column(name = "ACCOUNT_STATUS")
    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
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

    @Basic
    @Column(name = "IS_SHARE")
    public String getIsShare() {
        return isShare;
    }

    public void setIsShare(String isShare) {
        this.isShare = isShare;
    }

    @Basic
    @Column(name = "SHARE_ID")
    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    @Basic
    @Column(name = "ENTERPRISE_ID")
    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinanceAccount that = (FinanceAccount) o;
        return Objects.equals(accountId, that.accountId) &&
                Objects.equals(accountType, that.accountType) &&
                Objects.equals(accountTotalSum, that.accountTotalSum) &&
                Objects.equals(accountUsableSum, that.accountUsableSum) &&
                Objects.equals(accountFrozenSum, that.accountFrozenSum) &&
                Objects.equals(accountConsumeSum, that.accountConsumeSum) &&
                Objects.equals(accountRechargeSum, that.accountRechargeSum) &&
                Objects.equals(accountCreditSum, that.accountCreditSum) &&
                Objects.equals(accountStatus, that.accountStatus) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, accountType, accountTotalSum, accountUsableSum, accountFrozenSum, accountConsumeSum, accountRechargeSum, accountCreditSum, accountStatus, createdBy, createdTime, updatedBy, updatedTime);
    }
}
