package com.smoc.cloud.customer.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "enterprise_express_info")
public class EnterpriseExpressInfo {
    private String id;
    private String enterpriseId;
    private String postRemark;
    private String postContacts;
    private String postPhone;
    private String postAddress;
    private String postStatus;
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
    @Column(name = "ENTERPRISE_ID")
    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    @Basic
    @Column(name = "POST_REMARK")
    public String getPostRemark() {
        return postRemark;
    }

    public void setPostRemark(String postRemark) {
        this.postRemark = postRemark;
    }

    @Basic
    @Column(name = "POST_CONTACTS")
    public String getPostContacts() {
        return postContacts;
    }

    public void setPostContacts(String postContacts) {
        this.postContacts = postContacts;
    }

    @Basic
    @Column(name = "POST_PHONE")
    public String getPostPhone() {
        return postPhone;
    }

    public void setPostPhone(String postPhone) {
        this.postPhone = postPhone;
    }

    @Basic
    @Column(name = "POST_ADDRESS")
    public String getPostAddress() {
        return postAddress;
    }

    public void setPostAddress(String postAddress) {
        this.postAddress = postAddress;
    }

    @Basic
    @Column(name = "POST_STATUS")
    public String getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(String postStatus) {
        this.postStatus = postStatus;
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
        EnterpriseExpressInfo that = (EnterpriseExpressInfo) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(enterpriseId, that.enterpriseId) &&
                Objects.equals(postRemark, that.postRemark) &&
                Objects.equals(postContacts, that.postContacts) &&
                Objects.equals(postPhone, that.postPhone) &&
                Objects.equals(postAddress, that.postAddress) &&
                Objects.equals(postStatus, that.postStatus) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, enterpriseId, postRemark, postContacts, postPhone, postAddress, postStatus, createdBy, createdTime, updatedBy, updatedTime);
    }
}
