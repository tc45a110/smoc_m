package com.smoc.cloud.iot.product.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "iot_product_info")
public class IotProductInfo {
    @Id
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    @Column(name = "PRODUCT_NAME", nullable = false, length = 32)
    private String productName;

    @Column(name = "PRODUCT_TYPE", nullable = false, length = 32)
    private String productType;

    @Column(name = "CARDS_CHANGING", precision = 24, scale = 6)
    private BigDecimal cardsChanging;

    @Column(name = "PRODUCT_POOL_SIZE", nullable = false, precision = 24, scale = 6)
    private BigDecimal productPoolSize;

    @Column(name = "CHANGING_CYCLE", nullable = false, length = 32)
    private String changingCycle;

    @Column(name = "CYCLE_QUOTA", nullable = false, precision = 24, scale = 6)
    private BigDecimal cycleQuota;

    @Column(name = "ABOVE_QUOTA_CHANGING", nullable = false, precision = 24, scale = 4)
    private BigDecimal aboveQuotaChanging;

    @Column(name = "PRODUCT_CARDS_NUM")
    private Integer productCardsNum;

    @Column(name = "REMARK", length = 900)
    private String remark;

    @Column(name = "USE_STATUS", nullable = false, length = 32)
    private String useStatus;

    @Column(name = "PRODUCT_STATUS", nullable = false, length = 32)
    private String productStatus;

    @Column(name = "CREATED_BY", nullable = false, length = 32)
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

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public String getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(String useStatus) {
        this.useStatus = useStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getProductCardsNum() {
        return productCardsNum;
    }

    public void setProductCardsNum(Integer productCardsNum) {
        this.productCardsNum = productCardsNum;
    }

    public BigDecimal getAboveQuotaChanging() {
        return aboveQuotaChanging;
    }

    public void setAboveQuotaChanging(BigDecimal aboveQuotaChanging) {
        this.aboveQuotaChanging = aboveQuotaChanging;
    }

    public BigDecimal getCycleQuota() {
        return cycleQuota;
    }

    public void setCycleQuota(BigDecimal cycleQuota) {
        this.cycleQuota = cycleQuota;
    }

    public String getChangingCycle() {
        return changingCycle;
    }

    public void setChangingCycle(String changingCycle) {
        this.changingCycle = changingCycle;
    }

    public BigDecimal getProductPoolSize() {
        return productPoolSize;
    }

    public void setProductPoolSize(BigDecimal productPoolSize) {
        this.productPoolSize = productPoolSize;
    }

    public BigDecimal getCardsChanging() {
        return cardsChanging;
    }

    public void setCardsChanging(BigDecimal cardsChanging) {
        this.cardsChanging = cardsChanging;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}