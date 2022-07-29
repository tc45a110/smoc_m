package com.smoc.cloud.iot.carrier.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "iot_flow_cards_primary_info")
public class IotFlowCardsPrimaryInfo {
    @Id
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    @Column(name = "CARRIER", nullable = false, length = 32)
    private String carrier;

    @Column(name = "CARD_TYPE", nullable = false, length = 32)
    private String cardType;

    @Column(name = "ORDER_NUM", length = 32)
    private String orderNum;

    @Column(name = "ICCID", nullable = false, length = 32)
    private String iccid;
    @Column(name = "MSISDN", length = 32)
    private String msisdn;

    @Column(name = "IMSI", length = 32)
    private String imsi;

    @Column(name = "IMEI", length = 32)
    private String imei;

    @Column(name = "CHARGE_CYCLE", nullable = false, length = 32)
    private String chargeCycle;

    @Column(name = "CHARGE_TYPE", nullable = false, length = 32)
    private String chargeType;

    @Column(name = "FLOW_POOL_ID", length = 32)
    private String flowPoolId;

    @Column(name = "CYCLE_QUOTA", nullable = false, precision = 24, scale = 6)
    private BigDecimal cycleQuota;

    @Column(name = "OPEN_CARD_FEE", nullable = false, precision = 24, scale = 6)
    private BigDecimal openCardFee;

    @Column(name = "CYCLE_FUNCTION_FEE", nullable = false, precision = 24, scale = 6)
    private BigDecimal cycleFunctionFee;

    @Column(name = "ACTIVE_DATE", length = 32)
    private String activeDate;

    @Column(name = "OPEN_DATE", length = 32)
    private String openDate;

    @Column(name = "USE_STATUS", length = 32)
    private String useStatus;

    @Column(name = "CARD_STATUS", length = 32)
    private String cardStatus;

    @Column(name = "CREATED_BY", nullable = false, length = 32)
    private String createdBy;

    @Column(name = "CREATED_TIME", nullable = false)
    private Date createdTime;

    @Column(name = "UPDATED_BY", length = 32)
    private String updatedBy;

    @Column(name = "UPDATED_TIME")
    private Date updatedTime;

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(String useStatus) {
        this.useStatus = useStatus;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public String getActiveDate() {
        return activeDate;
    }

    public void setActiveDate(String activeDate) {
        this.activeDate = activeDate;
    }

    public BigDecimal getCycleQuota() {
        return cycleQuota;
    }

    public void setCycleQuota(BigDecimal cycleQuota) {
        this.cycleQuota = cycleQuota;
    }

    public BigDecimal getOpenCardFee() {
        return openCardFee;
    }

    public void setOpenCardFee(BigDecimal openCardFee) {
        this.openCardFee = openCardFee;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getChargeCycle() {
        return chargeCycle;
    }

    public void setChargeCycle(String chargeCycle) {
        this.chargeCycle = chargeCycle;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public BigDecimal getCycleFunctionFee() {
        return cycleFunctionFee;
    }

    public void setCycleFunctionFee(BigDecimal cycleFunctionFee) {
        this.cycleFunctionFee = cycleFunctionFee;
    }

    public String getFlowPoolId() {
        return flowPoolId;
    }

    public void setFlowPoolId(String flowPoolId) {
        this.flowPoolId = flowPoolId;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}