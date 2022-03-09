package com.smoc.cloud.message.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "message_daily_statistics")
public class MessageDailyStatistic {
    @Id
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    @Column(name = "BUSINESS_ACCOUNT", nullable = false, length = 32)
    private String businessAccount;

    @Column(name = "CHANNEL_ID", nullable = false, length = 32)
    private String channelId;

    @Column(name = "AREA_CODE", nullable = false, length = 32)
    private String areaCode;

    @Column(name = "PRICE_AREA_CODE", nullable = false, length = 32)
    private String priceAreaCode;

    @Column(name = "CARRIER", nullable = false, length = 32)
    private String carrier;

    @Column(name = "BUSINESS_TYPE", nullable = false, length = 32)
    private String businessType;

    @Column(name = "CUSTOMER_SUBMIT_NUM", nullable = false)
    private Integer customerSubmitNum;

    @Column(name = "SUCCESS_SUBMIT_NUM", nullable = false)
    private Integer successSubmitNum;

    @Column(name = "FAILURE_SUBMIT_NUM", nullable = false)
    private Integer failureSubmitNum;

    @Column(name = "MESSAGE_SUCCESS_NUM", nullable = false)
    private Integer messageSuccessNum;

    @Column(name = "MESSAGE_FAILURE_NUM", nullable = false)
    private Integer messageFailureNum;

    @Column(name = "MESSAGE_NO_REPORT_NUM", nullable = false)
    private Integer messageNoReportNum;

    @Column(name = "MESSAGE_SIGN", nullable = false)
    private String messageSign;

    @Column(name = "MESSAGE_DATE", nullable = false)
    private Date messageDate;

    @Column(name = "CREATED_BY", length = 32)
    private String createdBy;

    @Column(name = "CREATED_TIME", nullable = false)
    private Date createdTime;

    @Column(name = "UPDATED_BY", length = 32)
    private String updatedBy;

    @Column(name = "UPDATED_TIME")
    private Date updatedTime;

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
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

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }

    public Integer getMessageNoReportNum() {
        return messageNoReportNum;
    }

    public void setMessageNoReportNum(Integer messageNoReportNum) {
        this.messageNoReportNum = messageNoReportNum;
    }

    public Integer getMessageFailureNum() {
        return messageFailureNum;
    }

    public void setMessageFailureNum(Integer messageFailureNum) {
        this.messageFailureNum = messageFailureNum;
    }

    public Integer getMessageSuccessNum() {
        return messageSuccessNum;
    }

    public void setMessageSuccessNum(Integer messageSuccessNum) {
        this.messageSuccessNum = messageSuccessNum;
    }

    public Integer getFailureSubmitNum() {
        return failureSubmitNum;
    }

    public void setFailureSubmitNum(Integer failureSubmitNum) {
        this.failureSubmitNum = failureSubmitNum;
    }

    public Integer getSuccessSubmitNum() {
        return successSubmitNum;
    }

    public void setSuccessSubmitNum(Integer successSubmitNum) {
        this.successSubmitNum = successSubmitNum;
    }

    public Integer getCustomerSubmitNum() {
        return customerSubmitNum;
    }

    public void setCustomerSubmitNum(Integer customerSubmitNum) {
        this.customerSubmitNum = customerSubmitNum;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getPriceAreaCode() {
        return priceAreaCode;
    }

    public void setPriceAreaCode(String priceAreaCode) {
        this.priceAreaCode = priceAreaCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getBusinessAccount() {
        return businessAccount;
    }

    public void setBusinessAccount(String businessAccount) {
        this.businessAccount = businessAccount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessageSign() {
        return messageSign;
    }

    public void setMessageSign(String messageSign) {
        this.messageSign = messageSign;
    }
}