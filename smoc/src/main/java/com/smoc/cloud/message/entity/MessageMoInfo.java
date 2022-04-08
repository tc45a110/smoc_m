package com.smoc.cloud.message.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "message_mo_info")
public class MessageMoInfo {
    private String id;
    private String enterpriseId;
    private String accountId;
    private String channelId;
    private String channelSrcId;
    private String accountSrcId;
    private String moSrcId;
    private String businessCode;
    private String mobile;
    private String taskId;
    private String webTemplateId;
    private String moMessageContent;
    private Date moDate;
    private String mtMessageContent;
    private Date mtDate;
    private String status;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

    private String businessType;
    private String infoType;
    private String carrier;
    private String area;

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
    @Column(name = "ACCOUNT_ID")
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
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
    @Column(name = "CHANNEL_SRC_ID")
    public String getChannelSrcId() {
        return channelSrcId;
    }

    public void setChannelSrcId(String channelSrcId) {
        this.channelSrcId = channelSrcId;
    }

    @Basic
    @Column(name = "ACCOUNT_SRC_ID")
    public String getAccountSrcId() {
        return accountSrcId;
    }

    public void setAccountSrcId(String accountSrcId) {
        this.accountSrcId = accountSrcId;
    }

    @Basic
    @Column(name = "MO_SRC_ID")
    public String getMoSrcId() {
        return moSrcId;
    }

    public void setMoSrcId(String moSrcId) {
        this.moSrcId = moSrcId;
    }

    @Basic
    @Column(name = "BUSINESS_CODE")
    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    @Basic
    @Column(name = "MOBILE")
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Basic
    @Column(name = "TASK_ID")
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Basic
    @Column(name = "WEB_TEMPLATE_ID")
    public String getWebTemplateId() {
        return webTemplateId;
    }

    public void setWebTemplateId(String webTemplateId) {
        this.webTemplateId = webTemplateId;
    }

    @Basic
    @Column(name = "MT_MESSAGE_CONTENT")
    public String getMtMessageContent() {
        return mtMessageContent;
    }

    public void setMtMessageContent(String mtMessageContent) {
        this.mtMessageContent = mtMessageContent;
    }

    @Basic
    @Column(name = "MT_DATE")
    public Date getMtDate() {
        return mtDate;
    }

    public void setMtDate(Date mtDate) {
        this.mtDate = mtDate;
    }

    @Basic
    @Column(name = "MO_MESSAGE_CONTENT")
    public String getMoMessageContent() {
        return moMessageContent;
    }

    public void setMoMessageContent(String moMessageContent) {
        this.moMessageContent = moMessageContent;
    }

    @Basic
    @Column(name = "MO_DATE")
    public Date getMoDate() {
        return moDate;
    }

    public void setMoDate(Date moDate) {
        this.moDate = moDate;
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

    @Basic
    @Column(name = "BUSINESS_TYPE")
    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    @Basic
    @Column(name = "INFO_TYPE")
    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageMoInfo that = (MessageMoInfo) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(enterpriseId, that.enterpriseId) &&
                Objects.equals(accountId, that.accountId) &&
                Objects.equals(channelId, that.channelId) &&

                Objects.equals(mobile, that.mobile) &&
                Objects.equals(taskId, that.taskId) &&
                Objects.equals(webTemplateId, that.webTemplateId) &&
                Objects.equals(moMessageContent, that.moMessageContent) &&
                Objects.equals(moDate, that.moDate) &&
                Objects.equals(status, that.status) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, enterpriseId, accountId, channelId,  mobile, taskId, webTemplateId, moMessageContent, moDate, status, createdBy, createdTime, updatedBy, updatedTime);
    }
}
