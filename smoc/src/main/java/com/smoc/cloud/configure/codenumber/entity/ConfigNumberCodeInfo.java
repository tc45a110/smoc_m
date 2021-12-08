package com.smoc.cloud.configure.codenumber.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Date;

@Entity
@Table(name = "config_number_code_info")
public class ConfigNumberCodeInfo {
    private String id;
    private String srcId;
    private BigDecimal maxComplaintRate;
    private String carrier;
    private String businessType;
    private BigDecimal srcIdRpice;
    private Date priceEffectiveDate;
    private String useType;
    private String srcIdSource;
    private String accessPoint;
    private Date accessTime;
    private Integer minConsumeDemand;
    private Date minConsumeEffectiveDate;
    private Date srcIdEffectiveDate;
    private String caSrcId;
    private String province;
    private String city;
    private String srcIdRemark;
    private String srcIdStatus;
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
    @Column(name = "SRC_ID")
    public String getSrcId() {
        return srcId;
    }

    public void setSrcId(String srcId) {
        this.srcId = srcId;
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
    @Column(name = "SRC_ID_RPICE")
    public BigDecimal getSrcIdRpice() {
        return srcIdRpice;
    }

    public void setSrcIdRpice(BigDecimal srcIdRpice) {
        this.srcIdRpice = srcIdRpice;
    }

    @Basic
    @Column(name = "PRICE_EFFECTIVE_DATE")
    public Date getPriceEffectiveDate() {
        return priceEffectiveDate;
    }

    public void setPriceEffectiveDate(Date priceEffectiveDate) {
        this.priceEffectiveDate = priceEffectiveDate;
    }

    @Basic
    @Column(name = "USE_TYPE")
    public String getUseType() {
        return useType;
    }

    public void setUseType(String useType) {
        this.useType = useType;
    }

    @Basic
    @Column(name = "SRC_ID_SOURCE")
    public String getSrcIdSource() {
        return srcIdSource;
    }

    public void setSrcIdSource(String srcIdSource) {
        this.srcIdSource = srcIdSource;
    }

    @Basic
    @Column(name = "ACCESS_POINT")
    public String getAccessPoint() {
        return accessPoint;
    }

    public void setAccessPoint(String accessPoint) {
        this.accessPoint = accessPoint;
    }

    @Basic
    @Column(name = "ACCESS_TIME")
    public Date getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
    }

    @Basic
    @Column(name = "MIN_CONSUME_DEMAND")
    public Integer getMinConsumeDemand() {
        return minConsumeDemand;
    }

    public void setMinConsumeDemand(Integer minConsumeDemand) {
        this.minConsumeDemand = minConsumeDemand;
    }

    @Basic
    @Column(name = "MIN_CONSUME_EFFECTIVE_DATE")
    public Date getMinConsumeEffectiveDate() {
        return minConsumeEffectiveDate;
    }

    public void setMinConsumeEffectiveDate(Date minConsumeEffectiveDate) {
        this.minConsumeEffectiveDate = minConsumeEffectiveDate;
    }

    @Basic
    @Column(name = "SRC_ID_EFFECTIVE_DATE")
    public Date getSrcIdEffectiveDate() {
        return srcIdEffectiveDate;
    }

    public void setSrcIdEffectiveDate(Date srcIdEffectiveDate) {
        this.srcIdEffectiveDate = srcIdEffectiveDate;
    }

    @Basic
    @Column(name = "CA_SRC_ID")
    public String getCaSrcId() {
        return caSrcId;
    }

    public void setCaSrcId(String caSrcId) {
        this.caSrcId = caSrcId;
    }

    @Basic
    @Column(name = "PROVINCE")
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Basic
    @Column(name = "CITY")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Basic
    @Column(name = "SRC_ID_REMARK")
    public String getSrcIdRemark() {
        return srcIdRemark;
    }

    public void setSrcIdRemark(String srcIdRemark) {
        this.srcIdRemark = srcIdRemark;
    }

    @Basic
    @Column(name = "SRC_ID_STATUS")
    public String getSrcIdStatus() {
        return srcIdStatus;
    }

    public void setSrcIdStatus(String srcIdStatus) {
        this.srcIdStatus = srcIdStatus;
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
        ConfigNumberCodeInfo that = (ConfigNumberCodeInfo) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(srcId, that.srcId) &&
                Objects.equals(maxComplaintRate, that.maxComplaintRate) &&
                Objects.equals(carrier, that.carrier) &&
                Objects.equals(businessType, that.businessType) &&
                Objects.equals(srcIdRpice, that.srcIdRpice) &&
                Objects.equals(priceEffectiveDate, that.priceEffectiveDate) &&
                Objects.equals(useType, that.useType) &&
                Objects.equals(srcIdSource, that.srcIdSource) &&
                Objects.equals(accessPoint, that.accessPoint) &&
                Objects.equals(accessTime, that.accessTime) &&
                Objects.equals(minConsumeDemand, that.minConsumeDemand) &&
                Objects.equals(minConsumeEffectiveDate, that.minConsumeEffectiveDate) &&
                Objects.equals(srcIdEffectiveDate, that.srcIdEffectiveDate) &&
                Objects.equals(caSrcId, that.caSrcId) &&
                Objects.equals(province, that.province) &&
                Objects.equals(city, that.city) &&
                Objects.equals(srcIdRemark, that.srcIdRemark) &&
                Objects.equals(srcIdStatus, that.srcIdStatus) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, srcId, maxComplaintRate, carrier, businessType, srcIdRpice, priceEffectiveDate, useType, srcIdSource, accessPoint, accessTime, minConsumeDemand, minConsumeEffectiveDate, srcIdEffectiveDate, caSrcId, province, city, srcIdRemark, srcIdStatus, createdBy, createdTime, updatedBy, updatedTime);
    }
}
