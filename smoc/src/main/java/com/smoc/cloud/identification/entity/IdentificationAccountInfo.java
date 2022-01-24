package com.smoc.cloud.identification.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "identification_account_info")
public class IdentificationAccountInfo {
    @Id
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    @Column(name = "ENTERPRISE_ID", nullable = false, length = 32)
    private String enterpriseId;

    @Column(name = "IDENTIFICATION_ACCOUNT", nullable = false, length = 32)
    private String identificationAccount;

    @Column(name = "MD5_HMAC_KEY", nullable = false)
    private String md5HmacKey;

    @Column(name = "AES_KEY", nullable = false, length = 32)
    private String aesKey;

    @Column(name = "AES_IV", nullable = false, length = 32)
    private String aesIv;

    @Column(name = "IDENTIFICATION_PRICE", nullable = false, precision = 24, scale = 4)
    private BigDecimal identificationPrice;

    @Column(name = "IDENTIFICATION_FACE_PRICE", nullable = false, precision = 24, scale = 4)
    private BigDecimal identificationFacePrice;

    @Column(name = "ACCOUNT_TYPE", nullable = false)
    private String accountType;

    @Column(name = "ACCOUNT_STATUS", nullable = false, length = 32)
    private String accountStatus;

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

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getIdentificationFacePrice() {
        return identificationFacePrice;
    }

    public void setIdentificationFacePrice(BigDecimal identificationFacePrice) {
        this.identificationFacePrice = identificationFacePrice;
    }

    public BigDecimal getIdentificationPrice() {
        return identificationPrice;
    }

    public void setIdentificationPrice(BigDecimal identificationPrice) {
        this.identificationPrice = identificationPrice;
    }

    public String getAesIv() {
        return aesIv;
    }

    public void setAesIv(String aesIv) {
        this.aesIv = aesIv;
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public String getMd5HmacKey() {
        return md5HmacKey;
    }

    public void setMd5HmacKey(String md5HmacKey) {
        this.md5HmacKey = md5HmacKey;
    }

    public String getIdentificationAccount() {
        return identificationAccount;
    }

    public void setIdentificationAccount(String identificationAccount) {
        this.identificationAccount = identificationAccount;
    }

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}