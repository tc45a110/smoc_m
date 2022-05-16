package com.smoc.cloud.intelligence.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "intellect_short_url")
public class IntellectShortUrl {
    @Id
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    @Column(name = "TPL_ID", nullable = false, length = 32)
    private String tplId;

    @Column(name = "CUST_FLAG", nullable = false, length = 64)
    private String custFlag;

    @Column(name = "CUST_ID", length = 32)
    private String custId;

    @Lob
    @Column(name = "DYNC_PARAMS")
    private String dyncParams;

    @Column(name = "CUSTOM_URL", length = 155)
    private String customUrl;

    @Column(name = "EXT_DATA")
    private String extData;

    @Column(name = "AIM_URL", length = 64)
    private String aimUrl;

    @Column(name = "AIM_CODE", length = 64)
    private String aimCode;

    @Column(name = "SHOW_TIMES", nullable = false)
    private Integer showTimes;

    @Column(name = "EXPIRE_TIMES", nullable = false)
    private Integer expireTimes;

    @Column(name = "CURRENT_PRICE", nullable = false, precision = 24, scale = 4)
    private BigDecimal currentPrice;

    @Column(name = "COST_PRICE", nullable = false, precision = 24, scale = 4)
    private BigDecimal costPrice;

    @Column(name = "FACTORIES", length = 255)
    private String factories;

    @Column(name = "RESULT_CODE", length = 32)
    private String resultCode;

    @Column(name = "ERROR_MESSAGE", length = 900)
    private String errorMessage;

    @Column(name = "IS_GIVE_BACK", length = 32)
    private String isGiveBack;

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

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getAimCode() {
        return aimCode;
    }

    public void setAimCode(String aimCode) {
        this.aimCode = aimCode;
    }

    public String getAimUrl() {
        return aimUrl;
    }

    public void setAimUrl(String aimUrl) {
        this.aimUrl = aimUrl;
    }

    public String getExtData() {
        return extData;
    }

    public void setExtData(String extData) {
        this.extData = extData;
    }

    public String getCustomUrl() {
        return customUrl;
    }

    public void setCustomUrl(String customUrl) {
        this.customUrl = customUrl;
    }

    public String getDyncParams() {
        return dyncParams;
    }

    public void setDyncParams(String dyncParams) {
        this.dyncParams = dyncParams;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustFlag() {
        return custFlag;
    }

    public void setCustFlag(String custFlag) {
        this.custFlag = custFlag;
    }

    public String getTplId() {
        return tplId;
    }

    public void setTplId(String tplId) {
        this.tplId = tplId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getShowTimes() {
        return showTimes;
    }

    public void setShowTimes(Integer showTimes) {
        this.showTimes = showTimes;
    }

    public Integer getExpireTimes() {
        return expireTimes;
    }

    public void setExpireTimes(Integer expireTimes) {
        this.expireTimes = expireTimes;
    }

    public String getFactories() {
        return factories;
    }

    public void setFactories(String factories) {
        this.factories = factories;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public String getIsGiveBack() {
        return isGiveBack;
    }

    public void setIsGiveBack(String isGiveBack) {
        this.isGiveBack = isGiveBack;
    }
}