package com.smoc.cloud.customer.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "config_route_content_rule")
public class ConfigRouteContentRule {
    private String id;
    private String accountId;
    private String businessId;
    private String businessType;
    private String carrier;
    private String areaCodes;
    private String routeContent;
    private String channelId;
    private String routeCode;
    private String routeStatus;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

    private String mobileNum;
    private Integer minContent;
    private Integer maxContent;
    private String routeReverseContent;

    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ACCOUNT_ID")
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Basic
    @Column(name = "BUSINESS_ID")
    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
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
    @Column(name = "AREA_CODES")
    public String getAreaCodes() {
        return areaCodes;
    }

    public void setAreaCodes(String areaCodes) {
        this.areaCodes = areaCodes;
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
    @Column(name = "MOBILE_NUM")
    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    @Basic
    @Column(name = "MIN_CONTENT")
    public Integer getMinContent() {
        return minContent;
    }

    public void setMinContent(Integer minContent) {
        this.minContent = minContent;
    }

    @Basic
    @Column(name = "MAX_CONTENT")
    public Integer getMaxContent() {
        return maxContent;
    }

    public void setMaxContent(Integer maxContent) {
        this.maxContent = maxContent;
    }

    @Basic
    @Column(name = "ROUTE_CONTENT")
    public String getRouteContent() {
        return routeContent;
    }

    public void setRouteContent(String routeContent) {
        this.routeContent = routeContent;
    }

    @Basic
    @Column(name = "CHANNEL_ID")
    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @Basic
    @Column(name = "ROUTE_CODE")
    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    @Basic
    @Column(name = "ROUTE_STATUS")
    public String getRouteStatus() {
        return routeStatus;
    }

    public void setRouteStatus(String routeStatus) {
        this.routeStatus = routeStatus;
    }

    @Basic
    @Column(name = "ROUTE_REVERSE_CONTENT")
    public String getRouteReverseContent() {
        return routeReverseContent;
    }

    public void setRouteReverseContent(String routeReverseContent) {
        this.routeReverseContent = routeReverseContent;
    }
}
