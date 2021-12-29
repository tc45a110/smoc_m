package com.smoc.cloud.configure.channel.group.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "config_channel_group")
public class ConfigChannelGroup {
    private String id;
    private String channelGroupId;
    private String channelId;
    private int channelPriority;
    private int channelWeight;
    private String createdBy;
    private Date createdTime;

    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    public int getChannelPriority() {
        return channelPriority;
    }

    public void setChannelPriority(int channelPriority) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigChannelGroup that = (ConfigChannelGroup) o;
        return channelPriority == that.channelPriority &&
                channelWeight == that.channelWeight &&
                Objects.equals(id, that.id) &&
                Objects.equals(channelGroupId, that.channelGroupId) &&
                Objects.equals(channelId, that.channelId) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, channelGroupId, channelId, channelPriority, channelWeight, createdBy, createdTime);
    }
}
