package com.smoc.cloud.reconciliation.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "reconciliation_carrier_items")
public class ReconciliationCarrierItems {
    private String id;
    private String channelPeriod;
    private String channelProvder;
    private String channelId;
    private String srcId;
    private String businessType;
    private int totalSendQuantity;
    private int totalSubmitQuantity;
    private BigDecimal totalAmount;
    private int totalNoReportQuantity;
    private BigDecimal price;
    private BigDecimal carrierTotalAmount;
    private int carrierTotalSendQuantity;
    private Integer carrierTotalSubmitQuantity;
    private Integer carrierTotalNoReportQuantity;
    private String channelPeriodStatus;
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
    @Column(name = "CHANNEL_PERIOD")
    public String getChannelPeriod() {
        return channelPeriod;
    }

    public void setChannelPeriod(String channelPeriod) {
        this.channelPeriod = channelPeriod;
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
    @Column(name = "CHANNEL_ID")
    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
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
    @Column(name = "BUSINESS_TYPE")
    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    @Basic
    @Column(name = "TOTAL_SEND_QUANTITY")
    public int getTotalSendQuantity() {
        return totalSendQuantity;
    }

    public void setTotalSendQuantity(int totalSendQuantity) {
        this.totalSendQuantity = totalSendQuantity;
    }

    @Basic
    @Column(name = "TOTAL_SUBMIT_QUANTITY")
    public int getTotalSubmitQuantity() {
        return totalSubmitQuantity;
    }

    public void setTotalSubmitQuantity(int totalSubmitQuantity) {
        this.totalSubmitQuantity = totalSubmitQuantity;
    }

    @Basic
    @Column(name = "TOTAL_AMOUNT")
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Basic
    @Column(name = "TOTAL_NO_REPORT_QUANTITY")
    public int getTotalNoReportQuantity() {
        return totalNoReportQuantity;
    }

    public void setTotalNoReportQuantity(int totalNoReportQuantity) {
        this.totalNoReportQuantity = totalNoReportQuantity;
    }

    @Basic
    @Column(name = "PRICE")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Basic
    @Column(name = "CARRIER_TOTAL_AMOUNT")
    public BigDecimal getCarrierTotalAmount() {
        return carrierTotalAmount;
    }

    public void setCarrierTotalAmount(BigDecimal carrierTotalAmount) {
        this.carrierTotalAmount = carrierTotalAmount;
    }

    @Basic
    @Column(name = "CARRIER_TOTAL_SEND_QUANTITY")
    public int getCarrierTotalSendQuantity() {
        return carrierTotalSendQuantity;
    }

    public void setCarrierTotalSendQuantity(int carrierTotalSendQuantity) {
        this.carrierTotalSendQuantity = carrierTotalSendQuantity;
    }

    @Basic
    @Column(name = "CARRIER_TOTAL_SUBMIT_QUANTITY")
    public Integer getCarrierTotalSubmitQuantity() {
        return carrierTotalSubmitQuantity;
    }

    public void setCarrierTotalSubmitQuantity(Integer carrierTotalSubmitQuantity) {
        this.carrierTotalSubmitQuantity = carrierTotalSubmitQuantity;
    }

    @Basic
    @Column(name = "CARRIER_TOTAL_NO_REPORT_QUANTITY")
    public Integer getCarrierTotalNoReportQuantity() {
        return carrierTotalNoReportQuantity;
    }

    public void setCarrierTotalNoReportQuantity(Integer carrierTotalNoReportQuantity) {
        this.carrierTotalNoReportQuantity = carrierTotalNoReportQuantity;
    }

    @Basic
    @Column(name = "CHANNEL_PERIOD_STATUS")
    public String getChannelPeriodStatus() {
        return channelPeriodStatus;
    }

    public void setChannelPeriodStatus(String channelPeriodStatus) {
        this.channelPeriodStatus = channelPeriodStatus;
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
        ReconciliationCarrierItems that = (ReconciliationCarrierItems) o;
        return totalSendQuantity == that.totalSendQuantity &&
                totalSubmitQuantity == that.totalSubmitQuantity &&
                totalNoReportQuantity == that.totalNoReportQuantity &&
                carrierTotalSendQuantity == that.carrierTotalSendQuantity &&
                Objects.equals(id, that.id) &&
                Objects.equals(channelPeriod, that.channelPeriod) &&
                Objects.equals(channelProvder, that.channelProvder) &&
                Objects.equals(channelId, that.channelId) &&
                Objects.equals(businessType, that.businessType) &&
                Objects.equals(totalAmount, that.totalAmount) &&
                Objects.equals(price, that.price) &&
                Objects.equals(carrierTotalAmount, that.carrierTotalAmount) &&
                Objects.equals(carrierTotalSubmitQuantity, that.carrierTotalSubmitQuantity) &&
                Objects.equals(carrierTotalNoReportQuantity, that.carrierTotalNoReportQuantity) &&
                Objects.equals(channelPeriodStatus, that.channelPeriodStatus) &&
                Objects.equals(status, that.status) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, channelPeriod, channelProvder, channelId, businessType, totalSendQuantity, totalSubmitQuantity, totalAmount, totalNoReportQuantity, price, carrierTotalAmount, carrierTotalSendQuantity, carrierTotalSubmitQuantity, carrierTotalNoReportQuantity, channelPeriodStatus, status, createdBy, createdTime, updatedBy, updatedTime);
    }
}
