package com.smoc.cloud.identification.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Entity
@Table(name = "identification_request_data")
public class IdentificationRequestData {
    @Id
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    @Column(name = "IDENTIFICATION_ACCOUNT", nullable = false, length = 32)
    private String identificationAccount;

    @Column(name = "ORDER_NO", nullable = false, length = 32)
    private String orderNo;

    @Column(name = "ORDER_TYPE", nullable = false, length = 32)
    private String orderType;

    @Column(name = "REQUEST_DATA", length = 2046)
    private String requestData;

    @Column(name = "RESPONCE_DATA", length = 2046)
    private String responceData;

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

    public String getResponceData() {
        return responceData;
    }

    public void setResponceData(String responceData) {
        this.responceData = responceData;
    }

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getIdentificationAccount() {
        return identificationAccount;
    }

    public void setIdentificationAccount(String identificationAccount) {
        this.identificationAccount = identificationAccount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}