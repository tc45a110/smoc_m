package com.smoc.cloud.configure.channel.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "config_channel_repair_rule")
public class ConfigChannelRepairRule {
    private String id;
    private String channelId;
    private String businessId;
    private String businessType;
    private String channelRepairId;
    private String repairCode;
    private String repairStatus;
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
    @Column(name = "CHANNEL_ID")
    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
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
    @Column(name = "BUSINESS_TYPE")
    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    @Basic
    @Column(name = "CHANNEL_REPAIR_ID")
    public String getChannelRepairId() {
        return channelRepairId;
    }

    public void setChannelRepairId(String channelRepairId) {
        this.channelRepairId = channelRepairId;
    }

    @Basic
    @Column(name = "REPAIR_CODE")
    public String getRepairCode() {
        return repairCode;
    }

    public void setRepairCode(String repairCode) {
        this.repairCode = repairCode;
    }

    @Basic
    @Column(name = "REPAIR_STATUS")
    public String getRepairStatus() {
        return repairStatus;
    }

    public void setRepairStatus(String repairStatus) {
        this.repairStatus = repairStatus;
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
        ConfigChannelRepairRule that = (ConfigChannelRepairRule) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(channelId, that.channelId) &&
                Objects.equals(businessId, that.businessId) &&
                Objects.equals(businessType, that.businessType) &&
                Objects.equals(channelRepairId, that.channelRepairId) &&
                Objects.equals(repairCode, that.repairCode) &&
                Objects.equals(repairStatus, that.repairStatus) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, channelId, businessId, businessType, channelRepairId, repairCode, repairStatus, createdBy, createdTime, updatedBy, updatedTime);
    }
}
