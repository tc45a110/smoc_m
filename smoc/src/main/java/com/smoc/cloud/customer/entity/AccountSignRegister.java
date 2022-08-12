package com.smoc.cloud.customer.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "account_sign_register")
public class AccountSignRegister {
    private String id;
    private String account;
    private String sign;
    private String signExtendNumber;
    private String extendType;
    private String extendData;
    private String enterpriseId;
    private String appName;
    private String serviceType;
    private String mainApplication;
    private String registerStatus;
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
    @Column(name = "ACCOUNT")
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Basic
    @Column(name = "SIGN")
    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Basic
    @Column(name = "SIGN_EXTEND_NUMBER")
    public String getSignExtendNumber() {
        return signExtendNumber;
    }

    public void setSignExtendNumber(String signExtendNumber) {
        this.signExtendNumber = signExtendNumber;
    }

    @Basic
    @Column(name = "EXTEND_TYPE")
    public String getExtendType() {
        return extendType;
    }

    public void setExtendType(String extendType) {
        this.extendType = extendType;
    }

    @Basic
    @Column(name = "EXTEND_DATA")
    public String getExtendData() {
        return extendData;
    }

    public void setExtendData(String extendData) {
        this.extendData = extendData;
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
    @Column(name = "APP_NAME")
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Basic
    @Column(name = "SERVICE_TYPE")
    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    @Basic
    @Column(name = "MAIN_APPLICATION")
    public String getMainApplication() {
        return mainApplication;
    }

    public void setMainApplication(String mainApplication) {
        this.mainApplication = mainApplication;
    }

    @Basic
    @Column(name = "REGISTER_STATUS")
    public String getRegisterStatus() {
        return registerStatus;
    }

    public void setRegisterStatus(String registerStatus) {
        this.registerStatus = registerStatus;
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
        AccountSignRegister that = (AccountSignRegister) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(account, that.account) &&
                Objects.equals(sign, that.sign) &&
                Objects.equals(signExtendNumber, that.signExtendNumber) &&
                Objects.equals(extendType, that.extendType) &&
                Objects.equals(extendData, that.extendData) &&
                Objects.equals(enterpriseId, that.enterpriseId) &&
                Objects.equals(appName, that.appName) &&
                Objects.equals(serviceType, that.serviceType) &&
                Objects.equals(mainApplication, that.mainApplication) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, account, sign, signExtendNumber, extendType, extendData, enterpriseId, appName, serviceType, mainApplication, createdBy, createdTime, updatedBy, updatedTime);
    }
}
