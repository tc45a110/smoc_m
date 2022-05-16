package com.smoc.cloud.intelligence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "intellect_callback_show_report")
public class IntellectCallbackShowReport {
    @Id
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    @Column(name = "ORDER_NO", length = 64)
    private String orderNo;

    @Column(name = "CUST_FLAG", nullable = false, length = 64)
    private String custFlag;

    @Column(name = "CUST_ID", length = 64)
    private String custId;

    @Column(name = "TPL_ID", nullable = false, length = 64)
    private String tplId;

    @Column(name = "AIM_URL", nullable = false, length = 64)
    private String aimUrl;

    @Column(name = "AIM_CODE", nullable = false, length = 64)
    private String aimCode;

    @Column(name = "EXT_DATA")
    private String extData;

    @Column(name = "STATUS", nullable = false)
    private Integer status;

    @Column(name = "`DESCRIBE`", length = 128)
    private String describe;

    @Column(name = "CREATED_BY", length = 32)
    private String createdBy;

    @Column(name = "CREATED_TIME", nullable = false)
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

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getExtData() {
        return extData;
    }

    public void setExtData(String extData) {
        this.extData = extData;
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

    public String getTplId() {
        return tplId;
    }

    public void setTplId(String tplId) {
        this.tplId = tplId;
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