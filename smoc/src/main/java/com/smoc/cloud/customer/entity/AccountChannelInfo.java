package com.smoc.cloud.customer.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "account_channel_info")
public class AccountChannelInfo {
    private String id;
    private String accountId;
    private String configType;
    private String carrier;
    private String channelGroupId;
    private String channelId;
    private String channelPriority;
    private int channelWeight;
    private String channelSource;
    private String changeSource;
    private String channelStatus;
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
    @Column(name = "ACCOUNT_ID")
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Basic
    @Column(name = "CONFIG_TYPE")
    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
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
    @Column(name = "CHANNEL_GROUP_ID")
    public String getChannelGroupId() {
        return channelGroupId;
    }

    public void setChannelGroupId(String channelGroupId) {
        this.channelGroupId = channelGroupId;
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
    @Column(name = "CHANNEL_PRIORITY")
    public String getChannelPriority() {
        return channelPriority;
    }

    public void setChannelPriority(String channelPriority) {
        this.channelPriority = channelPriority;
    }

    @Basic
    @Column(name = "CHANNEL_WEIGHT")
    public int getChannelWeight() {
        return channelWeight;
    }

    public void setChannelWeight(int channelWeight) {
        this.channelWeight = channelWeight;
    }

    @Basic
    @Column(name = "CHANNEL_SOURCE")
    public String getChannelSource() {
        return channelSource;
    }

    public void setChannelSource(String channelSource) {
        this.channelSource = channelSource;
    }

    @Basic
    @Column(name = "CHANGE_SOURCE")
    public String getChangeSource() {
        return changeSource;
    }

    public void setChangeSource(String changeSource) {
        this.changeSource = changeSource;
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
        AccountChannelInfo that = (AccountChannelInfo) o;
        return channelWeight == that.channelWeight &&
                Objects.equals(id, that.id) &&
                Objects.equals(accountId, that.accountId) &&
                Objects.equals(configType, that.configType) &&
                Objects.equals(carrier, that.carrier) &&
                Objects.equals(channelGroupId, that.channelGroupId) &&
                Objects.equals(channelId, that.channelId) &&
                Objects.equals(channelPriority, that.channelPriority) &&
                Objects.equals(channelSource, that.channelSource) &&
                Objects.equals(changeSource, that.changeSource) &&
                Objects.equals(channelStatus, that.channelStatus) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountId, configType, carrier, channelGroupId, channelId, channelPriority, channelWeight, channelSource, changeSource, channelStatus, createdBy, createdTime, updatedBy, updatedTime);
    }
}
