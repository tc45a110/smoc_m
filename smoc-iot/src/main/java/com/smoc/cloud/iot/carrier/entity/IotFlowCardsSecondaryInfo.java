package com.smoc.cloud.iot.carrier.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "iot_flow_cards_secondary_info")
public class IotFlowCardsSecondaryInfo {
    @Id
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    @Column(name = "CARD_ID", nullable = false, length = 32)
    private String cardId;

    @Column(name = "IMEI")
    private String imei;

    @Column(name = "IS_MOVE", length = 32)
    private String isMove;

    @Column(name = "STOP_REASEN", length = 64)
    private String stopReasen;

    @Column(name = "CITY_CODE", length = 32)
    private String cityCode;

    @Column(name = "LONGITUDE", length = 32)
    private String longitude;

    @Column(name = "LATITUDE", length = 32)
    private String latitude;

    @Column(name = "LONGITUDE_LATITUDE_DATE")
    private Date longitudeLatitudeDate;

    @Column(name = "FLOW_SYNC_MONTH_DATE")
    private Date flowSyncMonthDate;

    @Column(name = "FLOW_SYNC_DAY_DATE")
    private Date flowSyncDayDate;

    @Column(name = "CYCLE_SETTLEMENT_DATE")
    private Date cycleSettlementDate;

    @Column(name = "REMARK")
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCycleSettlementDate() {
        return cycleSettlementDate;
    }

    public void setCycleSettlementDate(Date cycleSettlementDate) {
        this.cycleSettlementDate = cycleSettlementDate;
    }

    public Date getFlowSyncDayDate() {
        return flowSyncDayDate;
    }

    public void setFlowSyncDayDate(Date flowSyncDayDate) {
        this.flowSyncDayDate = flowSyncDayDate;
    }

    public Date getFlowSyncMonthDate() {
        return flowSyncMonthDate;
    }

    public void setFlowSyncMonthDate(Date flowSyncMonthDate) {
        this.flowSyncMonthDate = flowSyncMonthDate;
    }

    public Date getLongitudeLatitudeDate() {
        return longitudeLatitudeDate;
    }

    public void setLongitudeLatitudeDate(Date longitudeLatitudeDate) {
        this.longitudeLatitudeDate = longitudeLatitudeDate;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getStopReasen() {
        return stopReasen;
    }

    public void setStopReasen(String stopReasen) {
        this.stopReasen = stopReasen;
    }

    public String getIsMove() {
        return isMove;
    }

    public void setIsMove(String isMove) {
        this.isMove = isMove;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}