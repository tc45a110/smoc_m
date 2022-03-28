package com.smoc.cloud.reconciliation.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "reconciliation_enterprise_period")
public class ReconciliationEnterprisePeriod {
    private String id;
    private String accountPeriodId;
    private String enterpriseId;
    private String enterpriseName;
    private String accountPeriod;
    private String accountBillStatus;
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
    @Column(name = "ENTERPRISE_ID")
    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    @Basic
    @Column(name = "ENTERPRISE_NAME")
    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
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
    @Column(name = "ACCOUNT_BILL_STATUS")
    public String getAccountBillStatus() {
        return accountBillStatus;
    }

    public void setAccountBillStatus(String accountBillStatus) {
        this.accountBillStatus = accountBillStatus;
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
        ReconciliationEnterprisePeriod that = (ReconciliationEnterprisePeriod) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(accountPeriodId, that.accountPeriodId) &&
                Objects.equals(enterpriseId, that.enterpriseId) &&
                Objects.equals(enterpriseName, that.enterpriseName) &&
                Objects.equals(accountPeriod, that.accountPeriod) &&
                Objects.equals(accountBillStatus, that.accountBillStatus) &&
                Objects.equals(status, that.status) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountPeriodId, enterpriseId, enterpriseName, accountPeriod, accountBillStatus, status, createdBy, createdTime, updatedBy, updatedTime);
    }
}
