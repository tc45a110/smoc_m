package com.smoc.cloud.identification.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "identification_orders_info")
public class IdentificationOrdersInfo {
    @Id
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    @Column(name = "IDENTIFICATION_ACCOUNT", nullable = false, length = 32)
    private String identificationAccount;

    @Column(name = "ORDER_NO", nullable = false, length = 32)
    private String orderNo;

    @Column(name = "ORDER_TYPE", nullable = false)
    private String orderType;

    @Column(name = "IDENTIFICATION_PRICE", nullable = false, precision = 24, scale = 4)
    private BigDecimal identificationPrice;

    @Column(name = "IDENTIFICATION_PRICE_STATUS", nullable = false, length = 32)
    private String identificationPriceStatus;

    @Column(name = "IDENTIFICATION_ORDER_NO", length = 64)
    private String identificationOrderNo;

    @Column(name = "IDENTIFICATION_STATUS", length = 325)
    private String identificationStatus;

    @Column(name = "IDENTIFICATION_MESSAGE", length = 128)
    private String identificationMessage;

    @Column(name = "COST_PRICE", precision = 24, scale = 4)
    private BigDecimal costPrice;

    @Column(name = "CREATED_BY", length = 32)
    private String createdBy;

    @Column(name = "CREATED_TIME")
    private Date createdTime;

    @Column(name = "UPDATED_BY", length = 32)
    private String updatedBy;

    @Column(name = "UPDATED_TIME")
    private String updatedTime;

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
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

    public String getIdentificationMessage() {
        return identificationMessage;
    }

    public void setIdentificationMessage(String identificationMessage) {
        this.identificationMessage = identificationMessage;
    }

    public String getIdentificationStatus() {
        return identificationStatus;
    }

    public void setIdentificationStatus(String identificationStatus) {
        this.identificationStatus = identificationStatus;
    }

    public String getIdentificationOrderNo() {
        return identificationOrderNo;
    }

    public void setIdentificationOrderNo(String identificationOrderNo) {
        this.identificationOrderNo = identificationOrderNo;
    }

    public String getIdentificationPriceStatus() {
        return identificationPriceStatus;
    }

    public void setIdentificationPriceStatus(String identificationPriceStatus) {
        this.identificationPriceStatus = identificationPriceStatus;
    }

    public BigDecimal getIdentificationPrice() {
        return identificationPrice;
    }

    public void setIdentificationPrice(BigDecimal identificationPrice) {
        this.identificationPrice = identificationPrice;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getIdentificationAccount() {
        return identificationAccount;
    }

    public void setIdentificationAccount(String identificationAccount) {
        this.identificationAccount = identificationAccount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }
}