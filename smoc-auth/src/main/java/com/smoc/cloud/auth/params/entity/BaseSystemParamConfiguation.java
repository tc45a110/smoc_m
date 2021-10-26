package com.smoc.cloud.auth.params.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * 2020/5/30 14:24
 **/
@Entity
@Table(name = "base_system_param_configuation")
public class BaseSystemParamConfiguation {
    private String id;
    private String userId;
    private String paramCode;
    private String paramValue;
    private String paramValueDesc;
    private String status;
    private Date dataDate;

    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    @Column(name = "PARAM_CODE")
    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    @Basic
    @Column(name = "PARAM_VALUE")
    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    @Basic
    @Column(name = "PARAM_VALUE_DESC")
    public String getParamValueDesc() {
        return paramValueDesc;
    }

    public void setParamValueDesc(String paramValueDesc) {
        this.paramValueDesc = paramValueDesc;
    }

    @Basic
    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "DATA_DATE")
    public Date getDataDate() {
        return dataDate;
    }

    public void setDataDate(Date dataDate) {
        this.dataDate = dataDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseSystemParamConfiguation that = (BaseSystemParamConfiguation) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(paramCode, that.paramCode) &&
                Objects.equals(paramValue, that.paramValue) &&
                Objects.equals(paramValueDesc, that.paramValueDesc) &&
                Objects.equals(status, that.status) &&
                Objects.equals(dataDate, that.dataDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, paramCode, paramValue, paramValueDesc, status, dataDate);
    }
}
