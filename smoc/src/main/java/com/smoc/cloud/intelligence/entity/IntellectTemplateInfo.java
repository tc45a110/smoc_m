package com.smoc.cloud.intelligence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "intellect_template_info")
public class IntellectTemplateInfo {
    @Id
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    @Column(name = "ENTERPRISE_ID", nullable = false, length = 32)
    private String enterpriseId;

    @Column(name = "ACCOUNT_ID", length = 32)
    private String accountId;

    @Column(name = "TEMPLATE_ID", length = 32)
    private String templateId;

    @Column(name = "CARD_ID", length = 64)
    private String cardId;

    @Column(name = "TPL_NAME", nullable = false, length = 20)
    private String tplName;

    @Column(name = "SCENE", nullable = false, length = 20)
    private String scene;

    @Column(name = "PAGES", length = 64)
    private String pages;

    @Column(name = "MESSAGE_TYPE", length = 64)
    private String messageType;

    @Column(name = "BIZ_ID", length = 50)
    private String bizId;

    @Column(name = "BIZ_FLAG", length = 32)
    private String bizFlag;

    @Column(name = "SMS_EXAMPLE", nullable = false, length = 150)
    private String smsExample;

    @Column(name = "TEMPLATE_CHECK_STATUS", nullable = false)
    private Integer templateCheckStatus;

    @Column(name = "TEMPLATE_STATUS", nullable = false)
    private Integer templateStatus;

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

    public Integer getTemplateStatus() {
        return templateStatus;
    }

    public void setTemplateStatus(Integer templateStatus) {
        this.templateStatus = templateStatus;
    }

    public Integer getTemplateCheckStatus() {
        return templateCheckStatus;
    }

    public void setTemplateCheckStatus(Integer templateCheckStatus) {
        this.templateCheckStatus = templateCheckStatus;
    }

    public String getSmsExample() {
        return smsExample;
    }

    public void setSmsExample(String smsExample) {
        this.smsExample = smsExample;
    }

    public String getBizFlag() {
        return bizFlag;
    }

    public void setBizFlag(String bizFlag) {
        this.bizFlag = bizFlag;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getTplName() {
        return tplName;
    }

    public void setTplName(String tplName) {
        this.tplName = tplName;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
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