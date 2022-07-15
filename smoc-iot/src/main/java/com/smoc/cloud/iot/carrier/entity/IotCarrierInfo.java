package com.smoc.cloud.iot.carrier.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Entity
@Table(name = "iot_carrier_info")
public class IotCarrierInfo {
    @Id
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    @Column(name = "CARRIER", nullable = false, length = 32)
    private String carrier;

    @Column(name = "CARRIER_NAME", nullable = false, length = 32)
    private String carrierName;

    @Column(name = "CARRIER_IDENTIFYING", nullable = false, length = 32)
    private String carrierIdentifying;

    @Column(name = "CARRIER_USERNAME", nullable = false, length = 32)
    private String carrierUsername;

    @Column(name = "CARRIER_PASSWORD", nullable = false, length = 64)
    private String carrierPassword;

    @Column(name = "CARRIER_SERVER_URL", nullable = false, length = 64)
    private String carrierServerUrl;

    @Column(name = "API_TYPE", nullable = false, length = 32)
    private String apiType;

    @Column(name = "CARRIER_STATUS", nullable = false, length = 32)
    private String carrierStatus;

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

    public String getCarrierStatus() {
        return carrierStatus;
    }

    public void setCarrierStatus(String carrierStatus) {
        this.carrierStatus = carrierStatus;
    }

    public String getApiType() {
        return apiType;
    }

    public void setApiType(String apiType) {
        this.apiType = apiType;
    }

    public String getCarrierServerUrl() {
        return carrierServerUrl;
    }

    public void setCarrierServerUrl(String carrierServerUrl) {
        this.carrierServerUrl = carrierServerUrl;
    }

    public String getCarrierPassword() {
        return carrierPassword;
    }

    public void setCarrierPassword(String carrierPassword) {
        this.carrierPassword = carrierPassword;
    }

    public String getCarrierUsername() {
        return carrierUsername;
    }

    public void setCarrierUsername(String carrierUsername) {
        this.carrierUsername = carrierUsername;
    }

    public String getCarrierIdentifying() {
        return carrierIdentifying;
    }

    public void setCarrierIdentifying(String carrierIdentifying) {
        this.carrierIdentifying = carrierIdentifying;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}