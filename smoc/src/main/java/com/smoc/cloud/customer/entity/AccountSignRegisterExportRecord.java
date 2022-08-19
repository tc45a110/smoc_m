package com.smoc.cloud.customer.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "account_sign_register_export_record")
public class AccountSignRegisterExportRecord {

    private String id;

    private String registerOrderNo;

    private String carrier;

    private Integer registerNumber;

    private String registerStatus;

    private String createdBy;

    private Date createdTime;

    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "REGISTER_ORDER_NO")
    public String getRegisterOrderNo() {
        return registerOrderNo;
    }

    public void setRegisterOrderNo(String registerOrderNo) {
        this.registerOrderNo = registerOrderNo;
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
    @Column(name = "REGISTER_NUMBER")
    public Integer getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(Integer registerNumber) {
        this.registerNumber = registerNumber;
    }

    @Basic
    @Column(name = "REGISTER_STATUS")
    public String getRegisterStatus() {
        return registerStatus;
    }

    public void setRegisterStatus(String registerStatus) {
        this.registerStatus = registerStatus;
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
}
