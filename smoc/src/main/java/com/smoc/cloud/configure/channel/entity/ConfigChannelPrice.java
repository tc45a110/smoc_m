package com.smoc.cloud.configure.channel.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "config_channel_price")
public class ConfigChannelPrice {
    private String id;
    private String channelId;
    private String priceStyle;
    private String areaCode;
    private BigDecimal channelPrice;
    private Date lasttimeHistory;
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
    @Column(name = "PRICE_STYLE")
    public String getPriceStyle() {
        return priceStyle;
    }

    public void setPriceStyle(String priceStyle) {
        this.priceStyle = priceStyle;
    }

    @Basic
    @Column(name = "AREA_CODE")
    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    @Basic
    @Column(name = "CHANNEL_PRICE")
    public BigDecimal getChannelPrice() {
        return channelPrice;
    }

    public void setChannelPrice(BigDecimal channelPrice) {
        this.channelPrice = channelPrice;
    }

    @Basic
    @Column(name = "LASTTIME_HISTORY")
    public Date getLasttimeHistory() {
        return lasttimeHistory;
    }

    public void setLasttimeHistory(Date lasttimeHistory) {
        this.lasttimeHistory = lasttimeHistory;
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
        ConfigChannelPrice that = (ConfigChannelPrice) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(channelId, that.channelId) &&
                Objects.equals(priceStyle, that.priceStyle) &&
                Objects.equals(areaCode, that.areaCode) &&
                Objects.equals(channelPrice, that.channelPrice) &&
                Objects.equals(lasttimeHistory, that.lasttimeHistory) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, channelId, priceStyle, areaCode, channelPrice, lasttimeHistory, updatedBy, updatedTime);
    }
}
