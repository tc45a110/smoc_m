package com.smoc.cloud.configure.advance.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "system_history_price_change_record")
public class SystemHistoryPriceChangeRecord {
    private String id;
    private String changeType;
    private String businessId;
    private String priceArea;
    private String areaType;
    private String startDate;
    private BigDecimal changePrice;
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
    @Column(name = "CHANGE_TYPE")
    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    @Basic
    @Column(name = "BUSINESS_ID")
    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    @Basic
    @Column(name = "PRICE_AREA")
    public String getPriceArea() {
        return priceArea;
    }

    public void setPriceArea(String priceArea) {
        this.priceArea = priceArea;
    }

    @Basic
    @Column(name = "AREA_TYPE")
    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    @Basic
    @Column(name = "START_DATE")
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @Basic
    @Column(name = "CHANGE_PRICE")
    public BigDecimal getChangePrice() {
        return changePrice;
    }

    public void setChangePrice(BigDecimal changePrice) {
        this.changePrice = changePrice;
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
        SystemHistoryPriceChangeRecord that = (SystemHistoryPriceChangeRecord) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(changeType, that.changeType) &&
                Objects.equals(businessId, that.businessId) &&
                Objects.equals(priceArea, that.priceArea) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(changePrice, that.changePrice) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, changeType, businessId, priceArea, startDate, changePrice, createdBy, createdTime, updatedBy, updatedTime);
    }
}
