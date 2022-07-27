package com.smoc.cloud.iot.carrier.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "iot_carrier_flow_pool")
public class IotCarrierFlowPool {
    @Id
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    @Column(name = "CARRIER_ID", nullable = false, length = 32)
    private String carrierId;

    @Column(name = "POOL_NAME", nullable = false, length = 128)
    private String poolName;

    @Column(name = "POOL_TYPE", nullable = false, length = 32)
    private String poolType;

    @Column(name = "POOL_CARD_NUMBER")
    private Integer poolCardNumber;

    @Column(name = "POOL_SIZE", nullable = false, precision = 24, scale = 6)
    private BigDecimal poolSize;

    @Column(name = "USED_AMOUNT", nullable = false, precision = 24, scale = 6)
    private BigDecimal usedAmount;

    @Column(name = "SYNC_DATE", length = 32)
    private String syncDate;

    @Column(name = "WARNING_LEVEL")
    private Integer warningLevel;

    @Column(name = "CONTINUE_TYPE", length = 32)
    private String continueType;

    @Column(name = "POOL_STATUS", nullable = false, length = 32)
    private String poolStatus;

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

    public String getPoolStatus() {
        return poolStatus;
    }

    public void setPoolStatus(String poolStatus) {
        this.poolStatus = poolStatus;
    }

    public String getContinueType() {
        return continueType;
    }

    public void setContinueType(String continueType) {
        this.continueType = continueType;
    }

    public String getSyncDate() {
        return syncDate;
    }

    public void setSyncDate(String syncDate) {
        this.syncDate = syncDate;
    }

    public BigDecimal getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(BigDecimal poolSize) {
        this.poolSize = poolSize;
    }

    public BigDecimal getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(BigDecimal usedAmount) {
        this.usedAmount = usedAmount;
    }

    public Integer getPoolCardNumber() {
        return poolCardNumber;
    }

    public void setPoolCardNumber(Integer poolCardNumber) {
        this.poolCardNumber = poolCardNumber;
    }

    public Integer getWarningLevel() {
        return warningLevel;
    }

    public void setWarningLevel(Integer warningLevel) {
        this.warningLevel = warningLevel;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getPoolType() {
        return poolType;
    }

    public void setPoolType(String poolType) {
        this.poolType = poolType;
    }

    public String getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(String carrierId) {
        this.carrierId = carrierId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}