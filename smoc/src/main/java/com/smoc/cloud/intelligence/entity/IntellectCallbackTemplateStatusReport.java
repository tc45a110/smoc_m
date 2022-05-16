package com.smoc.cloud.intelligence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "intellect_callback_template_status_report")
public class IntellectCallbackTemplateStatusReport {
    @Id
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    @Column(name = "ORDER_NO", length = 64)
    private String orderNo;

    @Column(name = "TPL_ID", nullable = false, length = 32)
    private String tplId;

    @Column(name = "BIZ_ID", length = 32)
    private String bizId;

    @Column(name = "BIZ_FLAG", length = 64)
    private String bizFlag;

    @Column(name = "TPL_STATE", nullable = false)
    private Integer tplState;

    @Column(name = "AUDIT_STATE", nullable = false)
    private Integer auditState;

    @Column(name = "FACTORY_INFO_LIST", length = 900)
    private String factoryInfoList;

    @Column(name = "AUDIT_DESC", length = 128)
    private String auditDesc;

    @Column(name = "FACTORY_TYPE", length = 32)
    private String factoryType;

    @Column(name = "STATE", nullable = false)
    private Integer state;

    @Column(name = "CREATED_BY", length = 32)
    private String createdBy;

    @Column(name = "CREATED_TIME")
    private Date createdTime;

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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getFactoryType() {
        return factoryType;
    }

    public void setFactoryType(String factoryType) {
        this.factoryType = factoryType;
    }

    public String getAuditDesc() {
        return auditDesc;
    }

    public void setAuditDesc(String auditDesc) {
        this.auditDesc = auditDesc;
    }

    public String getFactoryInfoList() {
        return factoryInfoList;
    }

    public void setFactoryInfoList(String factoryInfoList) {
        this.factoryInfoList = factoryInfoList;
    }

    public Integer getAuditState() {
        return auditState;
    }

    public void setAuditState(Integer auditState) {
        this.auditState = auditState;
    }

    public Integer getTplState() {
        return tplState;
    }

    public void setTplState(Integer tplState) {
        this.tplState = tplState;
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

    public String getTplId() {
        return tplId;
    }

    public void setTplId(String tplId) {
        this.tplId = tplId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}