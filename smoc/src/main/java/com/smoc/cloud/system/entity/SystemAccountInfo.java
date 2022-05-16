package com.smoc.cloud.system.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "system_account_info")
public class SystemAccountInfo {
    @Id
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    @Column(name = "ENTERPRISE_ID", nullable = false, length = 32)
    private String enterpriseId;

    @Column(name = "ACCOUNT", nullable = false, length = 32)
    private String account;

    @Column(name = "MD5_HMAC_KEY", nullable = false)
    private String md5HmacKey;

    @Column(name = "AES_KEY", nullable = false, length = 32)
    private String aesKey;

    @Column(name = "AES_IV", nullable = false, length = 32)
    private String aesIv;

    @Column(name = "SUBMIT_LIMITER", nullable = false)
    private Integer submitLimiter;

    @Column(name = "IDENTIFY_IP")
    private String identifyIp;

    @Column(name = "PRICE", nullable = false, precision = 24, scale = 4)
    private BigDecimal price;

    @Column(name = "SECOND_PRICE", nullable = false, precision = 24, scale = 4)
    private BigDecimal secondPrice;

    @Column(name = "THIRD_PRICE", nullable = false, precision = 24, scale = 4)
    private BigDecimal thirdPrice;

    @Column(name = "GRANTING_CREDIT", nullable = false, precision = 24, scale = 4)
    private BigDecimal grantingCredit;

    @Column(name = "ACCOUNT_TYPE", nullable = false)
    private String accountType;

    @Column(name = "BUSINESS_TYPE" )
    private String businessType;

    @Column(name = "ACCOUNT_STATUS", nullable = false, length = 32)
    private String accountStatus;

    @Column(name = "IS_GATEWAY", nullable = false, length = 32)
    private String isGateway;

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

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public BigDecimal getGrantingCredit() {
        return grantingCredit;
    }

    public void setGrantingCredit(BigDecimal grantingCredit) {
        this.grantingCredit = grantingCredit;
    }

    public BigDecimal getThirdPrice() {
        return thirdPrice;
    }

    public void setThirdPrice(BigDecimal thirdPrice) {
        this.thirdPrice = thirdPrice;
    }

    public BigDecimal getSecondPrice() {
        return secondPrice;
    }

    public void setSecondPrice(BigDecimal secondPrice) {
        this.secondPrice = secondPrice;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getIdentifyIp() {
        return identifyIp;
    }

    public void setIdentifyIp(String identifyIp) {
        this.identifyIp = identifyIp;
    }

    public Integer getSubmitLimiter() {
        return submitLimiter;
    }

    public void setSubmitLimiter(Integer submitLimiter) {
        this.submitLimiter = submitLimiter;
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getIsGateway() {
        return isGateway;
    }

    public void setIsGateway(String isGateway) {
        this.isGateway = isGateway;
    }
}