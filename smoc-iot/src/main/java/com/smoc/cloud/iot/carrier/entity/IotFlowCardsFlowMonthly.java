package com.smoc.cloud.iot.carrier.entity;

import javax.persistence.*;
import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "iot_flow_cards_flow_monthly")
public class IotFlowCardsFlowMonthly {
    private String id;
    private String cardId;
    private String iccid;
    private String msisdn;
    private String imsi;
    private String account;
    private String packageId;
    private String chargingType;
    private BigDecimal openCardFee;
    private BigDecimal cycleFunctionFee;
    private BigDecimal cycleQuota;
    private BigDecimal usedAmount;
    private BigDecimal totalAmount;
    private BigDecimal remainAmount;
    private String settlementFee;
    private String usedMonth;
    private String settlementStatus;
    private String createdBy;
    private Data createdTime;

    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "CARD_ID")
    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    @Basic
    @Column(name = "ICCID")
    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    @Basic
    @Column(name = "MSISDN")
    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    @Basic
    @Column(name = "IMSI")
    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    @Basic
    @Column(name = "ACCOUNT")
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Basic
    @Column(name = "PACKAGE_ID")
    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    @Basic
    @Column(name = "CHARGING_TYPE")
    public String getChargingType() {
        return chargingType;
    }

    public void setChargingType(String chargingType) {
        this.chargingType = chargingType;
    }

    @Basic
    @Column(name = "OPEN_CARD_FEE")
    public BigDecimal getOpenCardFee() {
        return openCardFee;
    }

    public void setOpenCardFee(BigDecimal openCardFee) {
        this.openCardFee = openCardFee;
    }

    @Basic
    @Column(name = "CYCLE_FUNCTION_FEE")
    public BigDecimal getCycleFunctionFee() {
        return cycleFunctionFee;
    }

    public void setCycleFunctionFee(BigDecimal cycleFunctionFee) {
        this.cycleFunctionFee = cycleFunctionFee;
    }

    @Basic
    @Column(name = "CYCLE_QUOTA")
    public BigDecimal getCycleQuota() {
        return cycleQuota;
    }

    public void setCycleQuota(BigDecimal cycleQuota) {
        this.cycleQuota = cycleQuota;
    }

    @Basic
    @Column(name = "USED_AMOUNT")
    public BigDecimal getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(BigDecimal usedAmount) {
        this.usedAmount = usedAmount;
    }

    @Basic
    @Column(name = "TOTAL_AMOUNT")
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Basic
    @Column(name = "REMAIN_AMOUNT")
    public BigDecimal getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(BigDecimal remainAmount) {
        this.remainAmount = remainAmount;
    }

    @Basic
    @Column(name = "SETTLEMENT_FEE")
    public String getSettlementFee() {
        return settlementFee;
    }

    public void setSettlementFee(String settlementFee) {
        this.settlementFee = settlementFee;
    }

    @Basic
    @Column(name = "USED_MONTH")
    public String getUsedMonth() {
        return usedMonth;
    }

    public void setUsedMonth(String usedMonth) {
        this.usedMonth = usedMonth;
    }

    @Basic
    @Column(name = "SETTLEMENT_STATUS")
    public String getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(String settlementStatus) {
        this.settlementStatus = settlementStatus;
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
    public Data getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Data createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IotFlowCardsFlowMonthly that = (IotFlowCardsFlowMonthly) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(cardId, that.cardId) &&
                Objects.equals(iccid, that.iccid) &&
                Objects.equals(msisdn, that.msisdn) &&
                Objects.equals(imsi, that.imsi) &&
                Objects.equals(account, that.account) &&
                Objects.equals(packageId, that.packageId) &&
                Objects.equals(chargingType, that.chargingType) &&
                Objects.equals(openCardFee, that.openCardFee) &&
                Objects.equals(cycleFunctionFee, that.cycleFunctionFee) &&
                Objects.equals(cycleQuota, that.cycleQuota) &&
                Objects.equals(usedAmount, that.usedAmount) &&
                Objects.equals(totalAmount, that.totalAmount) &&
                Objects.equals(remainAmount, that.remainAmount) &&
                Objects.equals(settlementFee, that.settlementFee) &&
                Objects.equals(usedMonth, that.usedMonth) &&
                Objects.equals(settlementStatus, that.settlementStatus) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cardId, iccid, msisdn, imsi, account, packageId, chargingType, openCardFee, cycleFunctionFee, cycleQuota, usedAmount, totalAmount, remainAmount, settlementFee, usedMonth, settlementStatus, createdBy, createdTime);
    }
}
