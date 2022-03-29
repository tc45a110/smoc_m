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
    private String channelSrc;
    private String srcId;
    private String mobile;
    private String taskId;
    private String webTemplateId;
    private String messageContent;
    private Date moDate;
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
    @Column(name = "CHANNEL_SRC")
    public String getChannelSrc() {
        return channelSrc;
    }

    public void setChannelSrc(String channelSrc) {
        this.channelSrc = channelSrc;
    }

    @Basic
    @Column(name = "SRC_ID")
    public String getSrcId() {
        return srcId;
    }

    public void setSrcId(String srcId) {
        this.srcId = srcId;
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
    @Column(name = "MESSAGE_CONTENT")
    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageMoInfo that = (MessageMoInfo) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(enterpriseId, that.enterpriseId) &&
                Objects.equals(accountId, that.accountId) &&
                Objects.equals(channelId, that.channelId) &&
                Objects.equals(channelSrc, that.channelSrc) &&
                Objects.equals(srcId, that.srcId) &&
                Objects.equals(mobile, that.mobile) &&
                Objects.equals(taskId, that.taskId) &&
                Objects.equals(webTemplateId, that.webTemplateId) &&
                Objects.equals(messageContent, that.messageContent) &&
                Objects.equals(moDate, that.moDate) &&
                Objects.equals(status, that.status) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, enterpriseId, accountId, channelId, channelSrc, srcId, mobile, taskId, webTemplateId, messageContent, moDate, status, createdBy, createdTime, updatedBy, updatedTime);
    }
}
