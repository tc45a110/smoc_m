package com.smoc.cloud.message.entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "account_template_info")
public class AccountTemplateInfo {
    @Id
    @Column(name = "TEMPLATE_ID", nullable = false, length = 32)
    private String id;

    @Column(name = "TEMPLATE_TYPE", nullable = false, length = 32)
    private String templateType;

    @Column(name = "BUSINESS_ACCOUNT", nullable = false, length = 32)
    private String businessAccount;

    @Lob
    @Column(name = "TEMPLATE_CONTENT", nullable = false)
    private String templateContent;

    @Column(name = "CHECK_DATE")
    private Instant checkDate;

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
    private Instant createdTime;

    @Column(name = "UPDATED_BY", length = 32)
    private String updatedBy;

    @Column(name = "UPDATED_TIME")
    private Instant updatedTime;

    public Instant getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Instant updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
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

    public Instant getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Instant checkDate) {
        this.checkDate = checkDate;
    }

    public String getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}