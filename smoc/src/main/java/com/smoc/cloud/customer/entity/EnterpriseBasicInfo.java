package com.smoc.cloud.customer.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "enterprise_basic_info")
public class EnterpriseBasicInfo {
    private String enterpriseId;
    private String enterpriseParentId;
    private String enterpriseFlag;
    private String enterpriseName;
    private String enterpriseType;
    private String accessCorporation;
    private String enterpriseContacts;
    private String enterpriseContactsPhone;
    private String saler;
    private String enterpriseProcess;
    private String enterpriseStatus;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

    @Id
    @Column(name = "ENTERPRISE_ID")
    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    @Basic
    @Column(name = "ENTERPRISE_PARENT_ID")
    public String getEnterpriseParentId() {
        return enterpriseParentId;
    }

    public void setEnterpriseParentId(String enterpriseParentId) {
        this.enterpriseParentId = enterpriseParentId;
    }

    @Basic
    @Column(name = "ENTERPRISE_FLAG")
    public String getEnterpriseFlag() {
        return enterpriseFlag;
    }

    public void setEnterpriseFlag(String enterpriseFlag) {
        this.enterpriseFlag = enterpriseFlag;
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
    @Column(name = "ENTERPRISE_TYPE")
    public String getEnterpriseType() {
        return enterpriseType;
    }

    public void setEnterpriseType(String enterpriseType) {
        this.enterpriseType = enterpriseType;
    }

    @Basic
    @Column(name = "ACCESS_CORPORATION")
    public String getAccessCorporation() {
        return accessCorporation;
    }

    public void setAccessCorporation(String accessCorporation) {
        this.accessCorporation = accessCorporation;
    }

    @Basic
    @Column(name = "ENTERPRISE_CONTACTS")
    public String getEnterpriseContacts() {
        return enterpriseContacts;
    }

    public void setEnterpriseContacts(String enterpriseContacts) {
        this.enterpriseContacts = enterpriseContacts;
    }

    @Basic
    @Column(name = "ENTERPRISE_CONTACTS_PHONE")
    public String getEnterpriseContactsPhone() {
        return enterpriseContactsPhone;
    }

    public void setEnterpriseContactsPhone(String enterpriseContactsPhone) {
        this.enterpriseContactsPhone = enterpriseContactsPhone;
    }

    @Basic
    @Column(name = "SALER")
    public String getSaler() {
        return saler;
    }

    public void setSaler(String saler) {
        this.saler = saler;
    }

    @Basic
    @Column(name = "ENTERPRISE_PROCESS")
    public String getEnterpriseProcess() {
        return enterpriseProcess;
    }

    public void setEnterpriseProcess(String enterpriseProcess) {
        this.enterpriseProcess = enterpriseProcess;
    }

    @Basic
    @Column(name = "ENTERPRISE_STATUS")
    public String getEnterpriseStatus() {
        return enterpriseStatus;
    }

    public void setEnterpriseStatus(String enterpriseStatus) {
        this.enterpriseStatus = enterpriseStatus;
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
        EnterpriseBasicInfo that = (EnterpriseBasicInfo) o;
        return Objects.equals(enterpriseId, that.enterpriseId) &&
                Objects.equals(enterpriseParentId, that.enterpriseParentId) &&
                Objects.equals(enterpriseName, that.enterpriseName) &&
                Objects.equals(enterpriseType, that.enterpriseType) &&
                Objects.equals(accessCorporation, that.accessCorporation) &&
                Objects.equals(enterpriseContacts, that.enterpriseContacts) &&
                Objects.equals(enterpriseContactsPhone, that.enterpriseContactsPhone) &&
                Objects.equals(saler, that.saler) &&
                Objects.equals(enterpriseProcess, that.enterpriseProcess) &&
                Objects.equals(enterpriseStatus, that.enterpriseStatus) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enterpriseId, enterpriseParentId, enterpriseName, enterpriseType, accessCorporation, enterpriseContacts, enterpriseContactsPhone, saler, enterpriseProcess, enterpriseStatus, createdBy, createdTime, updatedBy, updatedTime);
    }
}
