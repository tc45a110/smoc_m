package com.smoc.cloud.configure.channel.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "config_channel_interface")
public class ConfigChannelInterface implements Cloneable{
    private String id;
    private String channelId;
    private String channelAccessAccount;
    private String channelAccessPassword;
    private String channelServiceUrl;
    private String spId;
    private String srcId;
    private String businessCode;
    private Integer connectNumber;
    private Integer maxSendSecond;
    private Integer heartbeatInterval;
    private String protocol;
    private String version;
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
    @Column(name = "CHANNEL_ACCESS_ACCOUNT")
    public String getChannelAccessAccount() {
        return channelAccessAccount;
    }

    public void setChannelAccessAccount(String channelAccessAccount) {
        this.channelAccessAccount = channelAccessAccount;
    }

    @Basic
    @Column(name = "CHANNEL_ACCESS_PASSWORD")
    public String getChannelAccessPassword() {
        return channelAccessPassword;
    }

    public void setChannelAccessPassword(String channelAccessPassword) {
        this.channelAccessPassword = channelAccessPassword;
    }

    @Basic
    @Column(name = "CHANNEL_SERVICE_URL")
    public String getChannelServiceUrl() {
        return channelServiceUrl;
    }

    public void setChannelServiceUrl(String channelServiceUrl) {
        this.channelServiceUrl = channelServiceUrl;
    }

    @Basic
    @Column(name = "SP_ID")
    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
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
    @Column(name = "BUSINESS_CODE")
    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    @Basic
    @Column(name = "CONNECT_NUMBER")
    public Integer getConnectNumber() {
        return connectNumber;
    }

    public void setConnectNumber(Integer connectNumber) {
        this.connectNumber = connectNumber;
    }

    @Basic
    @Column(name = "MAX_SEND_SECOND")
    public Integer getMaxSendSecond() {
        return maxSendSecond;
    }

    public void setMaxSendSecond(Integer maxSendSecond) {
        this.maxSendSecond = maxSendSecond;
    }

    @Basic
    @Column(name = "HEARTBEAT_INTERVAL")
    public Integer getHeartbeatInterval() {
        return heartbeatInterval;
    }

    public void setHeartbeatInterval(Integer heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }

    @Basic
    @Column(name = "PROTOCOL")
    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Basic
    @Column(name = "VERSION")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
        ConfigChannelInterface that = (ConfigChannelInterface) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(channelId, that.channelId) &&
                Objects.equals(channelAccessAccount, that.channelAccessAccount) &&
                Objects.equals(channelAccessPassword, that.channelAccessPassword) &&
                Objects.equals(channelServiceUrl, that.channelServiceUrl) &&
                Objects.equals(spId, that.spId) &&
                Objects.equals(srcId, that.srcId) &&
                Objects.equals(businessCode, that.businessCode) &&
                Objects.equals(connectNumber, that.connectNumber) &&
                Objects.equals(maxSendSecond, that.maxSendSecond) &&
                Objects.equals(heartbeatInterval, that.heartbeatInterval) &&
                Objects.equals(protocol, that.protocol) &&
                Objects.equals(version, that.version) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, channelId, channelAccessAccount, channelAccessPassword, channelServiceUrl, spId, srcId, businessCode, connectNumber, maxSendSecond, heartbeatInterval, protocol, version, createdBy, createdTime, updatedBy, updatedTime);
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
}
