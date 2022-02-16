package com.smoc.cloud.message.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "message_detail_info")
public class MessageDetailInfo {
    @Id
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    @Column(name = "PHONE_NUMBER", nullable = false, length = 32)
    private String phoneNumber;

    @Column(name = "BUSINESS_ACCOUNT", nullable = false, length = 32)
    private String businessAccount;

    @Column(name = "SEND_NUMBER", length = 32)
    private String sendNumber;

    @Column(name = "SUBMIT_STYLE", nullable = false, length = 32)
    private String submitStyle;

    @Column(name = "SIGN", length = 64)
    private String sign;

    @Column(name = "CARRIER", nullable = false, length = 32)
    private String carrier;

    @Column(name = "AREA", length = 32)
    private String area;

    @Column(name = "CUSTOMER_SUBMIT_STATUS", length = 32)
    private String customerSubmitStatus;

    @Column(name = "SEND_TIME", length = 32)
    private String sendTime;

    @Column(name = "CHANNEL_ID", length = 32)
    private String channelId;

    @Column(name = "REPORT_TIME", length = 32)
    private String reportTime;

    @Column(name = "REPORT_STATUS", length = 32)
    private String reportStatus;

    @Column(name = "CUSTOMER_REPORT_TIME", length = 32)
    private String customerReportTime;

    @Column(name = "DELAY_TIMES", length = 32)
    private String delayTimes;

    @Column(name = "TOTAL_DELAY_TIMES", length = 23)
    private String totalDelayTimes;

    @Column(name = "CUSTOMER_STATUS", length = 32)
    private String customerStatus;

    @Column(name = "MESSAGE_CONTENT", length = 511)
    private String messageContent;

    @Column(name = "CREATED_BY", length = 32)
    private String createdBy;

    @Column(name = "CREATED_TIME")
    private Date createdTime;

    @Column(name = "UPDATED_TIME")
    private Date updatedTime;

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(String customerStatus) {
        this.customerStatus = customerStatus;
    }

    public String getTotalDelayTimes() {
        return totalDelayTimes;
    }

    public void setTotalDelayTimes(String totalDelayTimes) {
        this.totalDelayTimes = totalDelayTimes;
    }

    public String getDelayTimes() {
        return delayTimes;
    }

    public void setDelayTimes(String delayTimes) {
        this.delayTimes = delayTimes;
    }

    public String getCustomerReportTime() {
        return customerReportTime;
    }

    public void setCustomerReportTime(String customerReportTime) {
        this.customerReportTime = customerReportTime;
    }

    public String getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getCustomerSubmitStatus() {
        return customerSubmitStatus;
    }

    public void setCustomerSubmitStatus(String customerSubmitStatus) {
        this.customerSubmitStatus = customerSubmitStatus;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSubmitStyle() {
        return submitStyle;
    }

    public void setSubmitStyle(String submitStyle) {
        this.submitStyle = submitStyle;
    }

    public String getSendNumber() {
        return sendNumber;
    }

    public void setSendNumber(String sendNumber) {
        this.sendNumber = sendNumber;
    }

    public String getBusinessAccount() {
        return businessAccount;
    }

    public void setBusinessAccount(String businessAccount) {
        this.businessAccount = businessAccount;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}