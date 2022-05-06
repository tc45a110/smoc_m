package com.smoc.cloud.configure.channel.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "config_channel_spare_channel")
public class ConfigChannelSpareChannel {
    private String id;
    private String channelId;
    private String spareChannelId;
    private String statusFailureSupple;
    private String accountFailureSupple;
    private String contentFailureSupple;
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
    @Column(name = "SPARE_CHANNEL_ID")
    public String getSpareChannelId() {
        return spareChannelId;
    }

    public void setSpareChannelId(String spareChannelId) {
        this.spareChannelId = spareChannelId;
    }

    @Basic
    @Column(name = "STATUS_FAILURE_SUPPLE")
    public String getStatusFailureSupple() {
        return statusFailureSupple;
    }

    public void setStatusFailureSupple(String statusFailureSupple) {
        this.statusFailureSupple = statusFailureSupple;
    }

    @Basic
    @Column(name = "ACCOUNT_FAILURE_SUPPLE")
    public String getAccountFailureSupple() {
        return accountFailureSupple;
    }

    public void setAccountFailureSupple(String accountFailureSupple) {
        this.accountFailureSupple = accountFailureSupple;
    }

    @Basic
    @Column(name = "CONTENT_FAILURE_SUPPLE")
    public String getContentFailureSupple() {
        return contentFailureSupple;
    }

    public void setContentFailureSupple(String contentFailureSupple) {
        this.contentFailureSupple = contentFailureSupple;
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
        ConfigChannelSpareChannel that = (ConfigChannelSpareChannel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(channelId, that.channelId) &&
                Objects.equals(spareChannelId, that.spareChannelId) &&
                Objects.equals(statusFailureSupple, that.statusFailureSupple) &&
                Objects.equals(accountFailureSupple, that.accountFailureSupple) &&
                Objects.equals(contentFailureSupple, that.contentFailureSupple) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, channelId, spareChannelId, statusFailureSupple, accountFailureSupple, contentFailureSupple, createdBy, createdTime, updatedBy, updatedTime);
    }
}
