package com.smoc.cloud.message.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "message_https_task_info")
public class MessageHttpsTaskInfo {
    private String id;
    private String subject;
    private String templateId;
    private String enterpriseId;
    private String businessAccount;
    private String businessType;
    private String infoType;
    private String messageType;
    private String sendType;
    private String expandNumber;
    private Integer splitNumber;
    private Integer submitNumber;
    private Integer successNumber;
    private Integer successSendNumber;
    private Integer failureNumber;
    private Integer noReportNumber;
    private String sendTime;
    private String messageContent;
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
    @Column(name = "SUBJECT")
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Basic
    @Column(name = "TEMPLATE_ID")
    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
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
    @Column(name = "BUSINESS_ACCOUNT")
    public String getBusinessAccount() {
        return businessAccount;
    }

    public void setBusinessAccount(String businessAccount) {
        this.businessAccount = businessAccount;
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

    @Basic
    @Column(name = "MESSAGE_TYPE")
    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    @Basic
    @Column(name = "SEND_TYPE")
    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    @Basic
    @Column(name = "EXPAND_NUMBER")
    public String getExpandNumber() {
        return expandNumber;
    }

    public void setExpandNumber(String expandNumber) {
        this.expandNumber = expandNumber;
    }

    @Basic
    @Column(name = "SPLIT_NUMBER")
    public Integer getSplitNumber() {
        return splitNumber;
    }

    public void setSplitNumber(Integer splitNumber) {
        this.splitNumber = splitNumber;
    }

    @Basic
    @Column(name = "SUBMIT_NUMBER")
    public Integer getSubmitNumber() {
        return submitNumber;
    }

    public void setSubmitNumber(Integer submitNumber) {
        this.submitNumber = submitNumber;
    }

    @Basic
    @Column(name = "SUCCESS_NUMBER")
    public Integer getSuccessNumber() {
        return successNumber;
    }

    public void setSuccessNumber(Integer successNumber) {
        this.successNumber = successNumber;
    }

    @Basic
    @Column(name = "SUCCESS_SEND_NUMBER")
    public Integer getSuccessSendNumber() {
        return successSendNumber;
    }

    public void setSuccessSendNumber(Integer successSendNumber) {
        this.successSendNumber = successSendNumber;
    }

    @Basic
    @Column(name = "FAILURE_NUMBER")
    public Integer getFailureNumber() {
        return failureNumber;
    }

    public void setFailureNumber(Integer failureNumber) {
        this.failureNumber = failureNumber;
    }

    @Basic
    @Column(name = "NO_REPORT_NUMBER")
    public Integer getNoReportNumber() {
        return noReportNumber;
    }

    public void setNoReportNumber(Integer noReportNumber) {
        this.noReportNumber = noReportNumber;
    }

    @Basic
    @Column(name = "SEND_TIME")
    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
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
        MessageHttpsTaskInfo that = (MessageHttpsTaskInfo) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(subject, that.subject) &&
                Objects.equals(templateId, that.templateId) &&
                Objects.equals(enterpriseId, that.enterpriseId) &&
                Objects.equals(businessAccount, that.businessAccount) &&
                Objects.equals(businessType, that.businessType) &&
                Objects.equals(infoType, that.infoType) &&
                Objects.equals(messageType, that.messageType) &&
                Objects.equals(sendType, that.sendType) &&
                Objects.equals(expandNumber, that.expandNumber) &&
                Objects.equals(splitNumber, that.splitNumber) &&
                Objects.equals(submitNumber, that.submitNumber) &&
                Objects.equals(successNumber, that.successNumber) &&
                Objects.equals(successSendNumber, that.successSendNumber) &&
                Objects.equals(failureNumber, that.failureNumber) &&
                Objects.equals(noReportNumber, that.noReportNumber) &&
                Objects.equals(sendTime, that.sendTime) &&
                Objects.equals(messageContent, that.messageContent) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subject, templateId, enterpriseId, businessAccount, businessType, infoType, messageType, sendType, expandNumber, splitNumber, submitNumber, successNumber, successSendNumber, failureNumber, noReportNumber, sendTime, messageContent, createdBy, createdTime, updatedBy, updatedTime);
    }
}
