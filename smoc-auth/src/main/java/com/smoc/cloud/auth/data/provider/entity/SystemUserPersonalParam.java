package com.smoc.cloud.auth.data.provider.entity;

import javax.persistence.*;
import java.time.Instant;

@Table(name = "system_user_personal_params", indexes = {
        @Index(name = "index_system_user_personal_params", columnList = "USER_ID, PARAM_KEY", unique = true)
})
@Entity
public class SystemUserPersonalParam {
    @Id
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    @Column(name = "USER_ID", nullable = false, length = 32)
    private String userId;

    @Column(name = "PARAM_KEY", nullable = false, length = 32)
    private String paramKey;

    @Column(name = "PARAM_VALUE", nullable = false, length = 64)
    private String paramValue;

    @Column(name = "CREATED_BY", nullable = false, length = 32)
    private String createdBy;

    @Column(name = "CREATED_TIME", nullable = false)
    private Instant createdTime;

    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}