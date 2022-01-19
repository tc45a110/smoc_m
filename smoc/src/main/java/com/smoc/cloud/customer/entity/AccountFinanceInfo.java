package com.smoc.cloud.customer.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "account_finance_info")
public class AccountFinanceInfo {
    private String id;
    private String accountId;
    private String payType;
    private String chargeType;
    private String frozenReturnDate;
    private BigDecimal accountCreditSum;
    private String carrierType;
    private String carrier;
    private BigDecimal carrierPrice;
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
    @Column(name = "PAY_TYPE")
    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    @Basic
    @Column(name = "CHARGE_TYPE")
    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    @Basic
    @Column(name = "FROZEN_RETURN_DATE")
    public String getFrozenReturnDate() {
        return frozenReturnDate;
    }

    public void setFrozenReturnDate(String frozenReturnDate) {
        this.frozenReturnDate = frozenReturnDate;
    }

    @Basic
    @Column(name = "ACCOUNT_CREDIT_SUM")
    public BigDecimal getAccountCreditSum() {
        return accountCreditSum;
    }

    public void setAccountCreditSum(BigDecimal accountCreditSum) {
        this.accountCreditSum = accountCreditSum;
    }

    @Basic
    @Column(name = "CARRIER_TYPE")
    public String getCarrierType() {
        return carrierType;
    }

    public void setCarrierType(String carrierType) {
        this.carrierType = carrierType;
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
    @Column(name = "CARRIER_PRICE")
    public BigDecimal getCarrierPrice() {
        return carrierPrice;
    }

    public void setCarrierPrice(BigDecimal carrierPrice) {
        this.carrierPrice = carrierPrice;
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
        AccountFinanceInfo that = (AccountFinanceInfo) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(accountId, that.accountId) &&
                Objects.equals(payType, that.payType) &&
                Objects.equals(chargeType, that.chargeType) &&
                Objects.equals(frozenReturnDate, that.frozenReturnDate) &&
                Objects.equals(accountCreditSum, that.accountCreditSum) &&
                Objects.equals(carrierType, that.carrierType) &&
                Objects.equals(carrier, that.carrier) &&
                Objects.equals(carrierPrice, that.carrierPrice) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountId, payType, chargeType, frozenReturnDate, accountCreditSum, carrierType, carrier, carrierPrice, createdBy, createdTime, updatedBy, updatedTime);
    }
}
