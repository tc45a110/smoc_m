package com.smoc.cloud.configure.channel.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "config_channel_basic_info")
public class ConfigChannelBasicInfo {
    private String channelId;
    private String channelName;
    private String carrier;
    private String businessType;
    private BigDecimal maxComplaintRate;
    private String accessProvince;
    private String accessCity;
    private String channelProvder;
    private String infoType;
    private String businessAreaType;
    private String supportAreaCodes;
    private String maskProvince;
    private String reportEnable;
    private String priceStyle;
    private String signType;
    private String upMessageEnable;
    private String transferEnable;
    private String transferType;
    private String channelIntroduce;
    private String channelProcess;
    private String channelRunStatus;
    private String channelStatus;
    private String isRegister;
    private String registerEnterprise;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;
    private String channelAccessSales;
    private String channelRestrictContent;
    private String specificProvder;
    private String channelBill;

    @Id
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
    @Column(name = "CARRIER")
    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
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
    @Column(name = "MAX_COMPLAINT_RATE")
    public BigDecimal getMaxComplaintRate() {
        return maxComplaintRate;
    }

    public void setMaxComplaintRate(BigDecimal maxComplaintRate) {
        this.maxComplaintRate = maxComplaintRate;
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
    @Column(name = "CHANNEL_PROVDER")
    public String getChannelProvder() {
        return channelProvder;
    }

    public void setChannelProvder(String channelProvder) {
        this.channelProvder = channelProvder;
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
    @Column(name = "BUSINESS_AREA_TYPE")
    public String getBusinessAreaType() {
        return businessAreaType;
    }

    public void setBusinessAreaType(String businessAreaType) {
        this.businessAreaType = businessAreaType;
    }

    @Basic
    @Column(name = "SUPPORT_AREA_CODES")
    public String getSupportAreaCodes() {
        return supportAreaCodes;
    }

    public void setSupportAreaCodes(String supportAreaCodes) {
        this.supportAreaCodes = supportAreaCodes;
    }

    @Basic
    @Column(name = "MASK_PROVINCE")
    public String getMaskProvince() {
        return maskProvince;
    }

    public void setMaskProvince(String maskProvince) {
        this.maskProvince = maskProvince;
    }

    @Basic
    @Column(name = "REPORT_ENABLE")
    public String getReportEnable() {
        return reportEnable;
    }

    public void setReportEnable(String reportEnable) {
        this.reportEnable = reportEnable;
    }

    @Basic
    @Column(name = "PRICE_STYLE")
    public String getPriceStyle() {
        return priceStyle;
    }

    public void setPriceStyle(String priceStyle) {
        this.priceStyle = priceStyle;
    }

    @Basic
    @Column(name = "SIGN_TYPE")
    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    @Basic
    @Column(name = "UP_MESSAGE_ENABLE")
    public String getUpMessageEnable() {
        return upMessageEnable;
    }

    public void setUpMessageEnable(String upMessageEnable) {
        this.upMessageEnable = upMessageEnable;
    }

    @Basic
    @Column(name = "TRANSFER_ENABLE")
    public String getTransferEnable() {
        return transferEnable;
    }

    public void setTransferEnable(String transferEnable) {
        this.transferEnable = transferEnable;
    }

    @Basic
    @Column(name = "TRANSFER_TYPE")
    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    @Basic
    @Column(name = "CHANNEL_INTRODUCE")
    public String getChannelIntroduce() {
        return channelIntroduce;
    }

    public void setChannelIntroduce(String channelIntroduce) {
        this.channelIntroduce = channelIntroduce;
    }

    @Basic
    @Column(name = "CHANNEL_PROCESS")
    public String getChannelProcess() {
        return channelProcess;
    }

    public void setChannelProcess(String channelProcess) {
        this.channelProcess = channelProcess;
    }

    @Basic
    @Column(name = "CHANNEL_RUN_STATUS")
    public String getChannelRunStatus() {
        return channelRunStatus;
    }

    public void setChannelRunStatus(String channelRunStatus) {
        this.channelRunStatus = channelRunStatus;
    }

    @Basic
    @Column(name = "CHANNEL_STATUS")
    public String getChannelStatus() {
        return channelStatus;
    }

    public void setChannelStatus(String channelStatus) {
        this.channelStatus = channelStatus;
    }

    @Basic
    @Column(name = "IS_REGISTER")
    public String getIsRegister() {
        return isRegister;
    }

    public void setIsRegister(String isRegister) {
        this.isRegister = isRegister;
    }

    @Basic
    @Column(name = "REGISTER_ENTERPRISE")
    public String getRegisterEnterprise() {
        return registerEnterprise;
    }

    public void setRegisterEnterprise(String registerEnterprise) {
        this.registerEnterprise = registerEnterprise;
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
    @Column(name = "CHANNEL_ACCESS_SALES")
    public String getChannelAccessSales() {
        return channelAccessSales;
    }

    public void setChannelAccessSales(String channelAccessSales) {
        this.channelAccessSales = channelAccessSales;
    }

    @Basic
    @Column(name = "CHANNEL_RESTRICT_CONTENT")
    public String getChannelRestrictContent() {
        return channelRestrictContent;
    }

    public void setChannelRestrictContent(String channelRestrictContent) {
        this.channelRestrictContent = channelRestrictContent;
    }

    @Basic
    @Column(name = "ACCESS_CITY")
    public String getAccessCity() {
        return accessCity;
    }

    public void setAccessCity(String accessCity) {
        this.accessCity = accessCity;
    }

    @Basic
    @Column(name = "SPECIFIC_PROVDER")
    public String getSpecificProvder() {
        return specificProvder;
    }

    public void setSpecificProvder(String specificProvder) {
        this.specificProvder = specificProvder;
    }

    @Basic
    @Column(name = "CHANNEL_BILL")
    public String getChannelBill() {
        return channelBill;
    }

    public void setChannelBill(String channelBill) {
        this.channelBill = channelBill;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigChannelBasicInfo that = (ConfigChannelBasicInfo) o;
        return Objects.equals(channelId, that.channelId) &&
                Objects.equals(channelName, that.channelName) &&
                Objects.equals(carrier, that.carrier) &&
                Objects.equals(maxComplaintRate, that.maxComplaintRate) &&
                Objects.equals(accessProvince, that.accessProvince) &&
                Objects.equals(channelProvder, that.channelProvder) &&
                Objects.equals(infoType, that.infoType) &&
                Objects.equals(businessAreaType, that.businessAreaType) &&
                Objects.equals(supportAreaCodes, that.supportAreaCodes) &&
                Objects.equals(maskProvince, that.maskProvince) &&
                Objects.equals(reportEnable, that.reportEnable) &&
                Objects.equals(priceStyle, that.priceStyle) &&
                Objects.equals(signType, that.signType) &&
                Objects.equals(upMessageEnable, that.upMessageEnable) &&
                Objects.equals(transferEnable, that.transferEnable) &&
                Objects.equals(transferType, that.transferType) &&
                Objects.equals(channelIntroduce, that.channelIntroduce) &&
                Objects.equals(channelProcess, that.channelProcess) &&
                Objects.equals(channelRunStatus, that.channelRunStatus) &&
                Objects.equals(channelStatus, that.channelStatus) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channelId, channelName, carrier, maxComplaintRate, accessProvince, channelProvder, infoType, businessAreaType, supportAreaCodes, maskProvince, reportEnable, priceStyle, signType, upMessageEnable, transferEnable, transferType, channelIntroduce, channelProcess, channelRunStatus, channelStatus, createdBy, createdTime, updatedBy, updatedTime);
    }
}
