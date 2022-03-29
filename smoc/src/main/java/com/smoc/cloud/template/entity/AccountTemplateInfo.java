package com.smoc.cloud.template.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "account_template_info")
public class AccountTemplateInfo {
    @Id
    @Column(name = "TEMPLATE_ID", nullable = false, length = 32)
    private String templateId;

    @Column(name = "ENTERPRISE_ID", nullable = false, length = 32)
    private String enterpriseId;

    @Column(name = "TEMPLATE_TYPE", nullable = false, length = 32)
    private String templateType;

    @Column(name = "BUSINESS_ACCOUNT", nullable = false, length = 32)
    private String businessAccount;

    @Column(name = "SIGN_NAME", nullable = false, length = 64)
    private String signName;

    @Lob
    @Column(name = "TEMPLATE_CONTENT", nullable = false)
    private String templateContent;

    @Column(name = "CHECK_DATE")
    private Date checkDate;

    @Column(name = "INFO_TYPE")
    private String infoType;

    @Column(name = "CHECK_BY", length = 32)
    private String checkBy;

    @Column(name = "CHECK_OPINIONS", length = 900)
    private String checkOpinions;

    @Column(name = "CHECK_STATUS", length = 1)
    private String checkStatus;

    @Column(name = "TEMPLATE_STATUS", nullable = false, length = 1)
    private String templateStatus;

    @Column(name = "TEMPLATE_AGREEMENT_TYPE", length = 32)
    private String templateAgreementType;

    @Column(name = "CREATED_BY", nullable = false, length = 32)
    private String createdBy;

    @Column(name = "CREATED_TIME", nullable = false)
    private Date createdTime;

    @Column(name = "UPDATED_BY", length = 32)
    private String updatedBy;

    @Column(name = "UPDATED_TIME")
    private Date updatedTime;

    @Column(name = "TEMPLATE_FLAG")
    private String templateFlag;

    @Column(name = "MM_ATTCHMENT")
    private String mmAttchment;

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getMmAttchment() {
        return mmAttchment;
    }

    public void setMmAttchment(String mmAttchment) {
        this.mmAttchment = mmAttchment;
    }

    public String getTemplateFlag() {
        return templateFlag;
    }

    public void setTemplateFlag(String templateFlag) {
        this.templateFlag = templateFlag;
    }

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

    public String getTemplateAgreementType() {
        return templateAgreementType;
    }

    public void setTemplateAgreementType(String templateAgreementType) {
        this.templateAgreementType = templateAgreementType;
    }

    public String getTemplateStatus() {
        return templateStatus;
    }

    public void setTemplateStatus(String templateStatus) {
        this.templateStatus = templateStatus;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getCheckOpinions() {
        return checkOpinions;
    }

    public void setCheckOpinions(String checkOpinions) {
        this.checkOpinions = checkOpinions;
    }

    public String getCheckBy() {
        return checkBy;
    }

    public void setCheckBy(String checkBy) {
        this.checkBy = checkBy;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public String getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getBusinessAccount() {
        return businessAccount;
    }

    public void setBusinessAccount(String businessAccount) {
        this.businessAccount = businessAccount;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
}