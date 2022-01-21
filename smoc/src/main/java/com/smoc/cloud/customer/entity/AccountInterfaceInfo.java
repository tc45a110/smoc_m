package com.smoc.cloud.customer.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "account_interface_info")
public class AccountInterfaceInfo {
    private String accountId;
    private String protocol;
    private String accountPassword;
    private Integer maxSubmitSecond;
    private int maxSendSecond;
    private String serviceCode;
    private String identifyIp;
    private Integer maxConnect;
    private String executeCheck;
    private String moUrl;
    private String statusReportUrl;
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
    @Column(name = "PROTOCOL")
    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Basic
    @Column(name = "ACCOUNT_PASSWORD")
    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    @Basic
    @Column(name = "MAX_SUBMIT_SECOND")
    public Integer getMaxSubmitSecond() {
        return maxSubmitSecond;
    }

    public void setMaxSubmitSecond(Integer maxSubmitSecond) {
        this.maxSubmitSecond = maxSubmitSecond;
    }

    @Basic
    @Column(name = "MAX_SEND_SECOND")
    public int getMaxSendSecond() {
        return maxSendSecond;
    }

    public void setMaxSendSecond(int maxSendSecond) {
        this.maxSendSecond = maxSendSecond;
    }

    @Basic
    @Column(name = "SERVICE_CODE")
    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    @Basic
    @Column(name = "IDENTIFY_IP")
    public String getIdentifyIp() {
        return identifyIp;
    }

    public void setIdentifyIp(String identifyIp) {
        this.identifyIp = identifyIp;
    }

    @Basic
    @Column(name = "MAX_CONNECT")
    public Integer getMaxConnect() {
        return maxConnect;
    }

    public void setMaxConnect(Integer maxConnect) {
        this.maxConnect = maxConnect;
    }

    @Basic
    @Column(name = "EXECUTE_CHECK")
    public String getExecuteCheck() {
        return executeCheck;
    }

    public void setExecuteCheck(String executeCheck) {
        this.executeCheck = executeCheck;
    }

    @Basic
    @Column(name = "MO_URL")
    public String getMoUrl() {
        return moUrl;
    }

    public void setMoUrl(String moUrl) {
        this.moUrl = moUrl;
    }

    @Basic
    @Column(name = "STATUS_REPORT_URL")
    public String getStatusReportUrl() {
        return statusReportUrl;
    }

    public void setStatusReportUrl(String statusReportUrl) {
        this.statusReportUrl = statusReportUrl;
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
        AccountInterfaceInfo that = (AccountInterfaceInfo) o;
        return maxSendSecond == that.maxSendSecond &&
                Objects.equals(accountId, that.accountId) &&
                Objects.equals(protocol, that.protocol) &&
                Objects.equals(accountPassword, that.accountPassword) &&
                Objects.equals(maxSubmitSecond, that.maxSubmitSecond) &&
                Objects.equals(serviceCode, that.serviceCode) &&
                Objects.equals(identifyIp, that.identifyIp) &&
                Objects.equals(maxConnect, that.maxConnect) &&
                Objects.equals(executeCheck, that.executeCheck) &&
                Objects.equals(moUrl, that.moUrl) &&
                Objects.equals(statusReportUrl, that.statusReportUrl) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, protocol, accountPassword, maxSubmitSecond, maxSendSecond, serviceCode, identifyIp, maxConnect, executeCheck, moUrl, statusReportUrl, createdBy, createdTime, updatedBy, updatedTime);
    }
}
