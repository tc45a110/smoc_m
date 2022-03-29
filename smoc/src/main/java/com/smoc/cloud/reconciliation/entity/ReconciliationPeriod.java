package com.smoc.cloud.reconciliation.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "reconciliation_period")
public class ReconciliationPeriod {
    private String id;
    private String accountPeriod;
    private String accountPeriodType;
    private Date accountPeriodStartDate;
    private Date accountPeriodEndDate;
    private String accountPeriodStatus;
    private String businessType;
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
    @Column(name = "ACCOUNT_PERIOD")
    public String getAccountPeriod() {
        return accountPeriod;
    }

    public void setAccountPeriod(String accountPeriod) {
        this.accountPeriod = accountPeriod;
    }

    @Basic
    @Column(name = "ACCOUNT_PERIOD_TYPE")
    public String getAccountPeriodType() {
        return accountPeriodType;
    }

    public void setAccountPeriodType(String accountPeriodType) {
        this.accountPeriodType = accountPeriodType;
    }

    @Basic
    @Column(name = "ACCOUNT_PERIOD_START_DATE")
    public Date getAccountPeriodStartDate() {
        return accountPeriodStartDate;
    }

    public void setAccountPeriodStartDate(Date accountPeriodStartDate) {
        this.accountPeriodStartDate = accountPeriodStartDate;
    }

    @Basic
    @Column(name = "ACCOUNT_PERIOD_END_DATE")
    public Date getAccountPeriodEndDate() {
        return accountPeriodEndDate;
    }

    public void setAccountPeriodEndDate(Date accountPeriodEndDate) {
        this.accountPeriodEndDate = accountPeriodEndDate;
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
    @Column(name = "BUSINESS_TYPE")
    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
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
        ReconciliationPeriod that = (ReconciliationPeriod) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(accountPeriod, that.accountPeriod) &&
                Objects.equals(accountPeriodType, that.accountPeriodType) &&
                Objects.equals(accountPeriodStartDate, that.accountPeriodStartDate) &&
                Objects.equals(accountPeriodEndDate, that.accountPeriodEndDate) &&
                Objects.equals(accountPeriodStatus, that.accountPeriodStatus) &&
                Objects.equals(businessType, that.businessType) &&
                Objects.equals(status, that.status) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountPeriod, accountPeriodType, accountPeriodStartDate, accountPeriodEndDate, accountPeriodStatus, businessType, status, createdBy, createdTime, updatedBy, updatedTime);
    }
}
