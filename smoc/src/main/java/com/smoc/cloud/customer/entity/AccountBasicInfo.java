package com.smoc.cloud.customer.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "account_base_info")
public class AccountBasicInfo {
    private String accountId;
    private String enterpriseId;
    private String accountName;
    private String extendNumber;
    private String businessType;
    private String carrier;
    private String infoType;
    private String industryType;
    private String extendCode;
    private Integer randomExtendCodeLength;
    private String accountChannelType;
    private String poistCarrier;
    private String transferType;
    private String accountProcess;
    private String accountStatus;
    private String accountPriority;
    private String consumeType;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;
    private String countryCode;
    private String repairStatus;

    @Id
    @Column(name = "ACCOUNT_ID")
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Basic
    @Column(name = "ENTERPRISE_ID")
    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    @Basic
    @Column(name = "ACCOUNT_NAME")
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }


    @Basic
    @Column(name = "EXTEND_NUMBER")
    public String getExtendNumber() {
        return extendNumber;
    }

    public void setExtendNumber(String extendNumber) {
        this.extendNumber = extendNumber;
    }

    @Basic
    @Column(name = "BUSINESS_TYPE")
    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
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
    @Column(name = "INFO_TYPE")
    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    @Basic
    @Column(name = "EXTEND_CODE")
    public String getExtendCode() {
        return extendCode;
    }

    public void setExtendCode(String extendCode) {
        this.extendCode = extendCode;
    }

    @Basic
    @Column(name = "RANDOM_EXTEND_CODE_LENGTH")
    public Integer getRandomExtendCodeLength() {
        return randomExtendCodeLength;
    }

    public void setRandomExtendCodeLength(Integer randomExtendCodeLength) {
        this.randomExtendCodeLength = randomExtendCodeLength;
    }

    @Basic
    @Column(name = "ACCOUNT_CHANNEL_TYPE")
    public String getAccountChannelType() {
        return accountChannelType;
    }

    public void setAccountChannelType(String accountChannelType) {
        this.accountChannelType = accountChannelType;
    }

    @Basic
    @Column(name = "POIST_CARRIER")
    public String getPoistCarrier() {
        return poistCarrier;
    }

    public void setPoistCarrier(String poistCarrier) {
        this.poistCarrier = poistCarrier;
    }

    @Basic
    @Column(name = "TRANSFER_TYPE")
    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    @Basic
    @Column(name = "ACCOUNT_PROCESS")
    public String getAccountProcess() {
        return accountProcess;
    }

    public void setAccountProcess(String accountProcess) {
        this.accountProcess = accountProcess;
    }

    @Basic
    @Column(name = "ACCOUNT_STATUS")
    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
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

    @Basic
    @Column(name = "COUNTRY_CODE")
    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Basic
    @Column(name = "ACCOUNT_PRIORITY")
    public String getAccountPriority() {
        return accountPriority;
    }

    public void setAccountPriority(String accountPriority) {
        this.accountPriority = accountPriority;
    }

    @Basic
    @Column(name = "CONSUME_TYPE")
    public String getConsumeType() {
        return consumeType;
    }

    public void setConsumeType(String consumeType) {
        this.consumeType = consumeType;
    }

    @Basic
    @Column(name = "INDUSTRY_TYPE")
    public String getIndustryType() {
        return industryType;
    }

    public void setIndustryType(String industryType) {
        this.industryType = industryType;
    }

    @Basic
    @Column(name = "REPAIR_STATUS")
    public String getRepairStatus() {
        return repairStatus;
    }

    public void setRepairStatus(String repairStatus) {
        this.repairStatus = repairStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountBasicInfo that = (AccountBasicInfo) o;
        return Objects.equals(accountId, that.accountId) &&
                Objects.equals(enterpriseId, that.enterpriseId) &&
                Objects.equals(accountName, that.accountName) &&
                Objects.equals(businessType, that.businessType) &&
                Objects.equals(carrier, that.carrier) &&
                Objects.equals(infoType, that.infoType) &&
                Objects.equals(extendCode, that.extendCode) &&
                Objects.equals(randomExtendCodeLength, that.randomExtendCodeLength) &&
                Objects.equals(accountChannelType, that.accountChannelType) &&
                Objects.equals(poistCarrier, that.poistCarrier) &&
                Objects.equals(transferType, that.transferType) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, enterpriseId, accountName, businessType, carrier, infoType, extendCode, randomExtendCodeLength, accountChannelType, poistCarrier, transferType, createdBy, createdTime, updatedBy, updatedTime);
    }
}
