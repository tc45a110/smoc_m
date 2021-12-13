package com.smoc.cloud.auth.data.provider.entity;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Table(name = "system_extend_business_param", indexes = {
        @Index(name = "index_system_extend_business_param", columnList = "BUSINESS_TYPE", unique = true)
})
@Entity
public class SystemExtendBusinessParam {
    @Id
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    @Column(name = "BUSINESS_TYPE", nullable = false, length = 32)
    private String businessType;

    @Column(name = "PARAM_TITLE", nullable = false, length = 90)
    private String paramTitle;

    @Column(name = "PARAM_KEY", nullable = false, length = 32)
    private String paramKey;

    @Column(name = "DATA_TYPE", nullable = false, length = 64)
    private String dataType;

    @Column(name = "IS_NULL", nullable = false, length = 32)
    private String isNull;

    @Column(name = "DICT_ENABLE", nullable = false, length = 32)
    private String dictEnable;

    @Column(name = "PARAM_MAX_LENGTH")
    private Integer paramMaxLength;

    @Column(name = "SHOW_TYPE", nullable = false, length = 32)
    private String showType;

    @Column(name = "SHOW_STYLE", length = 64)
    private String showStyle;

    @Column(name = "IS_READONLY", nullable = false, length = 32)
    private String isReadonly;

    @Column(name = "PARAM_STATUS", nullable = false, length = 32)
    private String paramStatus;

    @Column(name = "CREATED_BY", nullable = false, length = 32)
    private String createdBy;

    @Column(name = "CREATED_TIME", nullable = false)
    private Date createdTime;

    @Column(name = "UPDATED_BY", nullable = false, length = 32)
    private String updatedBy;

    @Column(name = "UPDATED_TIME", nullable = false)
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

    public String getParamStatus() {
        return paramStatus;
    }

    public void setParamStatus(String paramStatus) {
        this.paramStatus = paramStatus;
    }

    public String getIsReadonly() {
        return isReadonly;
    }

    public void setIsReadonly(String isReadonly) {
        this.isReadonly = isReadonly;
    }

    public String getShowStyle() {
        return showStyle;
    }

    public void setShowStyle(String showStyle) {
        this.showStyle = showStyle;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public Integer getParamMaxLength() {
        return paramMaxLength;
    }

    public void setParamMaxLength(Integer paramMaxLength) {
        this.paramMaxLength = paramMaxLength;
    }

    public String getDictEnable() {
        return dictEnable;
    }

    public void setDictEnable(String dictEnable) {
        this.dictEnable = dictEnable;
    }

    public String getIsNull() {
        return isNull;
    }

    public void setIsNull(String isNull) {
        this.isNull = isNull;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getParamTitle() {
        return paramTitle;
    }

    public void setParamTitle(String paramTitle) {
        this.paramTitle = paramTitle;
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