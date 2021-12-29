package com.smoc.cloud.configure.channel.group.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "config_channel_group_info")
public class ConfigChannelGroupInfo {
    private String channelGroupId;
    private String channelGroupName;
    private String carrier;
    private String businessType;
    private String infoType;
    private String maskProvince;
    private String channelGroupIntroduce;
    private String channelGroupProcess;
    private String channelGroupStatus;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

    @Id
    @Column(name = "CHANNEL_GROUP_ID")
    public String getChannelGroupId() {
        return channelGroupId;
    }

    public void setChannelGroupId(String channelGroupId) {
        this.channelGroupId = channelGroupId;
    }

    @Basic
    @Column(name = "CHANNEL_GROUP_NAME")
    public String getChannelGroupName() {
        return channelGroupName;
    }

    public void setChannelGroupName(String channelGroupName) {
        this.channelGroupName = channelGroupName;
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
    @Column(name = "INFO_TYPE")
    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
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
    @Column(name = "CHANNEL_GROUP_INTRODUCE")
    public String getChannelGroupIntroduce() {
        return channelGroupIntroduce;
    }

    public void setChannelGroupIntroduce(String channelGroupIntroduce) {
        this.channelGroupIntroduce = channelGroupIntroduce;
    }

    @Basic
    @Column(name = "CHANNEL_GROUP_PROCESS")
    public String getChannelGroupProcess() {
        return channelGroupProcess;
    }

    public void setChannelGroupProcess(String channelGroupProcess) {
        this.channelGroupProcess = channelGroupProcess;
    }

    @Basic
    @Column(name = "CHANNEL_GROUP_STATUS")
    public String getChannelGroupStatus() {
        return channelGroupStatus;
    }

    public void setChannelGroupStatus(String channelGroupStatus) {
        this.channelGroupStatus = channelGroupStatus;
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
        ConfigChannelGroupInfo that = (ConfigChannelGroupInfo) o;
        return Objects.equals(channelGroupId, that.channelGroupId) &&
                Objects.equals(channelGroupName, that.channelGroupName) &&
                Objects.equals(carrier, that.carrier) &&
                Objects.equals(businessType, that.businessType) &&
                Objects.equals(infoType, that.infoType) &&
                Objects.equals(maskProvince, that.maskProvince) &&
                Objects.equals(channelGroupIntroduce, that.channelGroupIntroduce) &&
                Objects.equals(channelGroupProcess, that.channelGroupProcess) &&
                Objects.equals(channelGroupStatus, that.channelGroupStatus) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channelGroupId, channelGroupName, carrier, businessType, infoType, maskProvince, channelGroupIntroduce, channelGroupProcess, channelGroupStatus, createdBy, createdTime, updatedBy, updatedTime);
    }
}
