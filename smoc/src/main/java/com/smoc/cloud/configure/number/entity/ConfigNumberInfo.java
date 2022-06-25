package com.smoc.cloud.configure.number.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "config_number_info")
public class ConfigNumberInfo {
    private String id;
    private String numberCode;
    private String carrier;
    private String province;
    private String numberCodeType;
    private String isSync;
    private String status;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "NUMBER_CODE")
    public String getNumberCode() {
        return numberCode;
    }

    public void setNumberCode(String numberCode) {
        this.numberCode = numberCode;
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
    @Column(name = "PROVINCE")
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Basic
    @Column(name = "NUMBER_CODE_TYPE")
    public String getNumberCodeType() {
        return numberCodeType;
    }

    public void setNumberCodeType(String numberCodeType) {
        this.numberCodeType = numberCodeType;
    }

    @Basic
    @Column(name = "IS_SYNC")
    public String getIsSync() {
        return isSync;
    }

    public void setIsSync(String isSync) {
        this.isSync = isSync;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigNumberInfo that = (ConfigNumberInfo) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(numberCode, that.numberCode) &&
                Objects.equals(carrier, that.carrier) &&
                Objects.equals(province, that.province) &&
                Objects.equals(numberCodeType, that.numberCodeType) &&
                Objects.equals(status, that.status) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numberCode, carrier, province, numberCodeType, status, createdBy, createdTime);
    }
}
