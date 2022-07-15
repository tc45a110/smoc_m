package com.smoc.cloud.iot.carrier.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Entity
@Table(name = "iot_flow_cards_change_history")
public class IotFlowCardsChangeHistory {
    @Id
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    @Column(name = "ICCID")
    private String iccid;

    @Column(name = "IMSI")
    private String imsi;

    @Column(name = "MSISDN")
    private String msisdn;

    @Column(name = "ORIGINAL_STATUS", length = 32)
    private String originalStatus;

    @Column(name = "TARGET_STATUS", length = 32)
    private String targetStatus;

    @Column(name = "CHANGE_TIME")
    private Date changeTime;

    public Date getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    public String getTargetStatus() {
        return targetStatus;
    }

    public void setTargetStatus(String targetStatus) {
        this.targetStatus = targetStatus;
    }

    public String getOriginalStatus() {
        return originalStatus;
    }

    public void setOriginalStatus(String originalStatus) {
        this.originalStatus = originalStatus;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}