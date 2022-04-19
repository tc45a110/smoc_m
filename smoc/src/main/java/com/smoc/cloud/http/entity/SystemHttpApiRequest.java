package com.smoc.cloud.http.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "system_http_api_request", schema = "smoc", catalog = "")
public class SystemHttpApiRequest {
    private String id;
    private String account;
    private String orderNo;
    private String orderType;
    private String requestData;
    private Timestamp createdTime;

    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ACCOUNT")
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Basic
    @Column(name = "ORDER_NO")
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Basic
    @Column(name = "ORDER_TYPE")
    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    @Basic
    @Column(name = "REQUEST_DATA")
    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    @Basic
    @Column(name = "CREATED_TIME")
    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SystemHttpApiRequest that = (SystemHttpApiRequest) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(account, that.account) &&
                Objects.equals(orderNo, that.orderNo) &&
                Objects.equals(orderType, that.orderType) &&
                Objects.equals(requestData, that.requestData) &&
                Objects.equals(createdTime, that.createdTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, account, orderNo, orderType, requestData, createdTime);
    }
}
