package com.smoc.cloud.auth.data.provider.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "base_flow_approve")
public class BaseFlowApprove {

    private String id;
    private String approveType;
    private String approveId;
    private Date submitTime;
    private String userId;
    private String approveAdvice;
    private Date approveTime;
    private Integer approveStatus;
    private String userApproveId;
    private Integer flowStatus;
    private String busiUrl;

    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "APPROVE_TYPE")
    public String getApproveType() {
        return approveType;
    }

    public void setApproveType(String approveType) {
        this.approveType = approveType;
    }

    @Basic
    @Column(name = "APPROVE_ID")
    public String getApproveId() {
        return approveId;
    }

    public void setApproveId(String approveId) {
        this.approveId = approveId;
    }

    @Basic
    @Column(name = "SUBMIT_TIME")
    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    @Basic
    @Column(name = "USER_ID")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "APPROVE_ADVICE")
    public String getApproveAdvice() {
        return approveAdvice;
    }

    public void setApproveAdvice(String approveAdvice) {
        this.approveAdvice = approveAdvice;
    }

    @Basic
    @Column(name = "APPROVE_TIME")
    public Date getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(Date approveTime) {
        this.approveTime = approveTime;
    }

    @Basic
    @Column(name = "APPROVE_STATUS")
    public Integer getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(Integer approveStatus) {
        this.approveStatus = approveStatus;
    }

    @Basic
    @Column(name = "USER_APPROVE_ID")
    public String getUserApproveId() {
        return userApproveId;
    }

    public void setUserApproveId(String userApproveId) {
        this.userApproveId = userApproveId;
    }

    @Basic
    @Column(name = "FLOW_STATUS")
    public Integer getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(Integer flowStatus) {
        this.flowStatus = flowStatus;
    }

    @Basic
    @Column(name = "BUSI_URL")
    public String getBusiUrl() {
        return busiUrl;
    }

    public void setBusiUrl(String busiUrl) {
        this.busiUrl = busiUrl;
    }
}
