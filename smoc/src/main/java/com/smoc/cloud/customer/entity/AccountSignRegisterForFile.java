package com.smoc.cloud.customer.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "account_sign_register_for_file")
public class AccountSignRegisterForFile {
    private String id;
    private String registerSignId;
    private String account;
    private String channelId;
    private String channelName;
    private String accessProvince;
    private String registerCarrier;
    private String registerCodeNumber;
    private String registerExtendNumber;
    private String registerSign;
    private String numberSegment;
    private String registerExpireDate;
    private String registerStatus;
    private String registerFiles;
    private String updatedBy;
    private Date updatedTime;
    private Date createdTime;

    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "REGISTER_SIGN_ID")
    public String getRegisterSignId() {
        return registerSignId;
    }

    public void setRegisterSignId(String registerSignId) {
        this.registerSignId = registerSignId;
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
    @Column(name = "CHANNEL_ID")
    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @Basic
    @Column(name = "CHANNEL_NAME")
    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @Basic
    @Column(name = "ACCESS_PROVINCE")
    public String getAccessProvince() {
        return accessProvince;
    }

    public void setAccessProvince(String accessProvince) {
        this.accessProvince = accessProvince;
    }

    @Basic
    @Column(name = "REGISTER_CARRIER")
    public String getRegisterCarrier() {
        return registerCarrier;
    }

    public void setRegisterCarrier(String registerCarrier) {
        this.registerCarrier = registerCarrier;
    }

    @Basic
    @Column(name = "REGISTER_CODE_NUMBER")
    public String getRegisterCodeNumber() {
        return registerCodeNumber;
    }

    public void setRegisterCodeNumber(String registerCodeNumber) {
        this.registerCodeNumber = registerCodeNumber;
    }

    @Basic
    @Column(name = "REGISTER_EXTEND_NUMBER")
    public String getRegisterExtendNumber() {
        return registerExtendNumber;
    }

    public void setRegisterExtendNumber(String registerExtendNumber) {
        this.registerExtendNumber = registerExtendNumber;
    }

    @Basic
    @Column(name = "REGISTER_SIGN")
    public String getRegisterSign() {
        return registerSign;
    }

    public void setRegisterSign(String registerSign) {
        this.registerSign = registerSign;
    }

    @Basic
    @Column(name = "NUMBER_SEGMENT")
    public String getNumberSegment() {
        return numberSegment;
    }

    public void setNumberSegment(String numberSegment) {
        this.numberSegment = numberSegment;
    }

    @Basic
    @Column(name = "REGISTER_EXPIRE_DATE")
    public String getRegisterExpireDate() {
        return registerExpireDate;
    }

    public void setRegisterExpireDate(String registerExpireDate) {
        this.registerExpireDate = registerExpireDate;
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
    @Column(name = "REGISTER_FILES")
    public String getRegisterFiles() {
        return registerFiles;
    }

    public void setRegisterFiles(String registerFiles) {
        this.registerFiles = registerFiles;
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
    @Column(name = "CREATED_TIME")
    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountSignRegisterForFile that = (AccountSignRegisterForFile) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(registerSignId, that.registerSignId) &&
                Objects.equals(account, that.account) &&
                Objects.equals(channelId, that.channelId) &&
                Objects.equals(channelName, that.channelName) &&
                Objects.equals(accessProvince, that.accessProvince) &&
                Objects.equals(registerCarrier, that.registerCarrier) &&
                Objects.equals(registerCodeNumber, that.registerCodeNumber) &&
                Objects.equals(registerExtendNumber, that.registerExtendNumber) &&
                Objects.equals(registerSign, that.registerSign) &&
                Objects.equals(numberSegment, that.numberSegment) &&
                Objects.equals(registerExpireDate, that.registerExpireDate) &&
                Objects.equals(registerStatus, that.registerStatus) &&
                Objects.equals(registerFiles, that.registerFiles) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime) &&
                Objects.equals(createdTime, that.createdTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, registerSignId, account, channelId, channelName, accessProvince, registerCarrier, registerCodeNumber, registerExtendNumber, registerSign, numberSegment, registerExpireDate, registerStatus, registerFiles, updatedBy, updatedTime, createdTime);
    }
}
