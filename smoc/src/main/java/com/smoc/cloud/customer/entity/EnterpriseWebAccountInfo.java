package com.smoc.cloud.customer.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "enterprise_web_account_info")
public class EnterpriseWebAccountInfo {
    private String id;
    private String enterpriseId;
    private String accountType;
    private String webLoginName;
    private String webLoginPassword;
    private String aesPassword;
    private String accountStatus;
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
    @Column(name = "ENTERPRISE_ID")
    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
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
    @Column(name = "WEB_LOGIN_NAME")
    public String getWebLoginName() {
        return webLoginName;
    }

    public void setWebLoginName(String webLoginName) {
        this.webLoginName = webLoginName;
    }

    @Basic
    @Column(name = "WEB_LOGIN_PASSWORD")
    public String getWebLoginPassword() {
        return webLoginPassword;
    }

    public void setWebLoginPassword(String webLoginPassword) {
        this.webLoginPassword = webLoginPassword;
    }

    @Basic
    @Column(name = "AES_PASSWORD")
    public String getAesPassword() {
        return aesPassword;
    }

    public void setAesPassword(String aesPassword) {
        this.aesPassword = aesPassword;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnterpriseWebAccountInfo that = (EnterpriseWebAccountInfo) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(enterpriseId, that.enterpriseId) &&
                Objects.equals(accountType, that.accountType) &&
                Objects.equals(webLoginName, that.webLoginName) &&
                Objects.equals(webLoginPassword, that.webLoginPassword) &&
                Objects.equals(accountStatus, that.accountStatus) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, enterpriseId, accountType, webLoginName, webLoginPassword, accountStatus, createdBy, createdTime, updatedBy, updatedTime);
    }
}
