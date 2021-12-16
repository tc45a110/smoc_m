package com.smoc.cloud.parameter.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "parameter_extend_filters_value", indexes = {
        @Index(name = "index_parameter_extend_filters_value", columnList = "BUSINESS_ID")
})
@Entity
public class ParameterExtendFiltersValue {
    @Id
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    @Column(name = "BUSINESS_TYPE", nullable = false, length = 32)
    private String businessType;

    @Column(name = "BUSINESS_ID", nullable = false, length = 32)
    private String businessId;

    @Column(name = "PARAM_NAME", nullable = false, length = 90)
    private String paramName;

    @Column(name = "PARAM_KEY", nullable = false, length = 32)
    private String paramKey;

    @Column(name = "PARAM_VALUE", nullable = false, length = 512)
    private String paramValue;

    @Column(name = "CREATED_BY", nullable = false, length = 32)
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

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}