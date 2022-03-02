package com.smoc.cloud.complaint.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "message_complaint_info")
public class MessageComplaintInfo {
    private String id;
    private String businessAccount;
    private String businessType;
    private String reportNumber;
    private String numberCode;
    private String channelId;
    private String carrierSource;
    private String carrier;
    private String sendDate;
    private String sendRate;
    private String reportContent;
    private String contentType;
    private String is12321;
    private String reportSource;
    private String reportDate;
    private String reportedNumber;
    private String reportedProvince;
    private String reportedCity;
    private String reportProvince;
    private String reportCity;
    private String reportUnit;
    private String reportChann;
    private String handleFlag;
    private String handleCarrierId;
    private String sendType;
    private String handleStatus;
    private String handleResult;
    private String handleRemark;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;
    private String complaintSource;

    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    @Column(name = "REPORT_NUMBER")
    public String getReportNumber() {
        return reportNumber;
    }

    public void setReportNumber(String reportNumber) {
        this.reportNumber = reportNumber;
    }

    @Basic
    @Column(name = "NUMBER_CODE")
    public String getNumberCode() {
        return numberCode;
    }

    public void setNumberCode(String numberCode) {
        this.numberCode = numberCode;
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
    @Column(name = "CARRIER_SOURCE")
    public String getCarrierSource() {
        return carrierSource;
    }

    public void setCarrierSource(String carrierSource) {
        this.carrierSource = carrierSource;
    }

    @Basic
    @Column(name = "CARRIER")
    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    @Basic
    @Column(name = "SEND_DATE")
    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    @Basic
    @Column(name = "SEND_RATE")
    public String getSendRate() {
        return sendRate;
    }

    public void setSendRate(String sendRate) {
        this.sendRate = sendRate;
    }

    @Basic
    @Column(name = "REPORT_CONTENT")
    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    @Basic
    @Column(name = "CONTENT_TYPE")
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Basic
    @Column(name = "IS_12321")
    public String getIs12321() {
        return is12321;
    }

    public void setIs12321(String is12321) {
        this.is12321 = is12321;
    }

    @Basic
    @Column(name = "REPORT_SOURCE")
    public String getReportSource() {
        return reportSource;
    }

    public void setReportSource(String reportSource) {
        this.reportSource = reportSource;
    }

    @Basic
    @Column(name = "REPORT_DATE")
    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    @Basic
    @Column(name = "REPORTED_NUMBER")
    public String getReportedNumber() {
        return reportedNumber;
    }

    public void setReportedNumber(String reportedNumber) {
        this.reportedNumber = reportedNumber;
    }

    @Basic
    @Column(name = "REPORTED_PROVINCE")
    public String getReportedProvince() {
        return reportedProvince;
    }

    public void setReportedProvince(String reportedProvince) {
        this.reportedProvince = reportedProvince;
    }

    @Basic
    @Column(name = "REPORTED_CITY")
    public String getReportedCity() {
        return reportedCity;
    }

    public void setReportedCity(String reportedCity) {
        this.reportedCity = reportedCity;
    }

    @Basic
    @Column(name = "REPORT_PROVINCE")
    public String getReportProvince() {
        return reportProvince;
    }

    public void setReportProvince(String reportProvince) {
        this.reportProvince = reportProvince;
    }

    @Basic
    @Column(name = "REPORT_CITY")
    public String getReportCity() {
        return reportCity;
    }

    public void setReportCity(String reportCity) {
        this.reportCity = reportCity;
    }

    @Basic
    @Column(name = "REPORT_UNIT")
    public String getReportUnit() {
        return reportUnit;
    }

    public void setReportUnit(String reportUnit) {
        this.reportUnit = reportUnit;
    }

    @Basic
    @Column(name = "REPORT_CHANN")
    public String getReportChann() {
        return reportChann;
    }

    public void setReportChann(String reportChann) {
        this.reportChann = reportChann;
    }

    @Basic
    @Column(name = "HANDLE_FLAG")
    public String getHandleFlag() {
        return handleFlag;
    }

    public void setHandleFlag(String handleFlag) {
        this.handleFlag = handleFlag;
    }

    @Basic
    @Column(name = "HANDLE_CARRIER_ID")
    public String getHandleCarrierId() {
        return handleCarrierId;
    }

    public void setHandleCarrierId(String handleCarrierId) {
        this.handleCarrierId = handleCarrierId;
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
    @Column(name = "HANDLE_STATUS")
    public String getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(String handleStatus) {
        this.handleStatus = handleStatus;
    }

    @Basic
    @Column(name = "HANDLE_RESULT")
    public String getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
    }

    @Basic
    @Column(name = "HANDLE_REMARK")
    public String getHandleRemark() {
        return handleRemark;
    }

    public void setHandleRemark(String handleRemark) {
        this.handleRemark = handleRemark;
    }

    @Basic
    @Column(name = "COMPLAINT_SOURCE")
    public String getComplaintSource() {
        return complaintSource;
    }

    public void setComplaintSource(String complaintSource) {
        this.complaintSource = complaintSource;
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
        MessageComplaintInfo that = (MessageComplaintInfo) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(businessAccount, that.businessAccount) &&
                Objects.equals(businessType, that.businessType) &&
                Objects.equals(reportNumber, that.reportNumber) &&
                Objects.equals(numberCode, that.numberCode) &&
                Objects.equals(channelId, that.channelId) &&
                Objects.equals(carrierSource, that.carrierSource) &&
                Objects.equals(carrier, that.carrier) &&
                Objects.equals(sendDate, that.sendDate) &&
                Objects.equals(sendRate, that.sendRate) &&
                Objects.equals(reportContent, that.reportContent) &&
                Objects.equals(contentType, that.contentType) &&
                Objects.equals(is12321, that.is12321) &&
                Objects.equals(reportSource, that.reportSource) &&
                Objects.equals(reportDate, that.reportDate) &&
                Objects.equals(reportedNumber, that.reportedNumber) &&
                Objects.equals(reportedProvince, that.reportedProvince) &&
                Objects.equals(reportedCity, that.reportedCity) &&
                Objects.equals(reportProvince, that.reportProvince) &&
                Objects.equals(reportCity, that.reportCity) &&
                Objects.equals(reportUnit, that.reportUnit) &&
                Objects.equals(reportChann, that.reportChann) &&
                Objects.equals(handleFlag, that.handleFlag) &&
                Objects.equals(handleCarrierId, that.handleCarrierId) &&
                Objects.equals(sendType, that.sendType) &&
                Objects.equals(handleStatus, that.handleStatus) &&
                Objects.equals(handleResult, that.handleResult) &&
                Objects.equals(handleRemark, that.handleRemark) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, businessAccount, businessType, reportNumber, numberCode, channelId, carrierSource, carrier, sendDate, sendRate, reportContent, contentType, is12321, reportSource, reportDate, reportedNumber, reportedProvince, reportedCity, reportProvince, reportCity, reportUnit, reportChann, handleFlag, handleCarrierId, sendType, handleStatus, handleResult, handleRemark, createdBy, createdTime, updatedBy, updatedTime);
    }
}
