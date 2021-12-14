package com.smoc.cloud.configure.channel.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "config_channel_filter")
public class ConfigChannelFilter {
    private String id;
    private String channelId;
    private String regularExpression;
    private String blackList;
    private String sendTimeLimit;
    private Integer maxPhoneSendMinute;
    private Integer maxPhoneSendHour;
    private Integer maxPhoneSendDaily;
    private Integer maxSendDaily;
    private Integer maxSendMonth;
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
    @Column(name = "REGULAR_EXPRESSION")
    public String getRegularExpression() {
        return regularExpression;
    }

    public void setRegularExpression(String regularExpression) {
        this.regularExpression = regularExpression;
    }

    @Basic
    @Column(name = "BLACK_LIST")
    public String getBlackList() {
        return blackList;
    }

    public void setBlackList(String blackList) {
        this.blackList = blackList;
    }

    @Basic
    @Column(name = "SEND_TIME_LIMIT")
    public String getSendTimeLimit() {
        return sendTimeLimit;
    }

    public void setSendTimeLimit(String sendTimeLimit) {
        this.sendTimeLimit = sendTimeLimit;
    }

    @Basic
    @Column(name = "MAX_PHONE_SEND_MINUTE")
    public Integer getMaxPhoneSendMinute() {
        return maxPhoneSendMinute;
    }

    public void setMaxPhoneSendMinute(Integer maxPhoneSendMinute) {
        this.maxPhoneSendMinute = maxPhoneSendMinute;
    }

    @Basic
    @Column(name = "MAX_PHONE_SEND_HOUR")
    public Integer getMaxPhoneSendHour() {
        return maxPhoneSendHour;
    }

    public void setMaxPhoneSendHour(Integer maxPhoneSendHour) {
        this.maxPhoneSendHour = maxPhoneSendHour;
    }

    @Basic
    @Column(name = "MAX_PHONE_SEND_DAILY")
    public Integer getMaxPhoneSendDaily() {
        return maxPhoneSendDaily;
    }

    public void setMaxPhoneSendDaily(Integer maxPhoneSendDaily) {
        this.maxPhoneSendDaily = maxPhoneSendDaily;
    }

    @Basic
    @Column(name = "MAX_SEND_DAILY")
    public Integer getMaxSendDaily() {
        return maxSendDaily;
    }

    public void setMaxSendDaily(Integer maxSendDaily) {
        this.maxSendDaily = maxSendDaily;
    }

    @Basic
    @Column(name = "MAX_SEND_MONTH")
    public Integer getMaxSendMonth() {
        return maxSendMonth;
    }

    public void setMaxSendMonth(Integer maxSendMonth) {
        this.maxSendMonth = maxSendMonth;
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
        ConfigChannelFilter that = (ConfigChannelFilter) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(channelId, that.channelId) &&
                Objects.equals(regularExpression, that.regularExpression) &&
                Objects.equals(blackList, that.blackList) &&
                Objects.equals(sendTimeLimit, that.sendTimeLimit) &&
                Objects.equals(maxPhoneSendMinute, that.maxPhoneSendMinute) &&
                Objects.equals(maxPhoneSendHour, that.maxPhoneSendHour) &&
                Objects.equals(maxPhoneSendDaily, that.maxPhoneSendDaily) &&
                Objects.equals(maxSendDaily, that.maxSendDaily) &&
                Objects.equals(maxSendMonth, that.maxSendMonth) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, channelId, regularExpression, blackList, sendTimeLimit, maxPhoneSendMinute, maxPhoneSendHour, maxPhoneSendDaily, maxSendDaily, maxSendMonth, createdBy, createdTime, updatedBy, updatedTime);
    }
}
