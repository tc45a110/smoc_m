package com.smoc.cloud.template.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "account_resource_info")
public class AccountResourceInfo {
    private String id;
    private String resourceType;
    private String enterpriseId;
    private String businessType;
    private String resourceTitle;
    private String resourceAttchment;
    private Integer resourceAttchmentSize;
    private String resourceAttchmentType;
    private Integer resourceHeight;
    private Integer resourceWidth;
    private String resourceStatus;
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
    @Column(name = "RESOURCE_TYPE")
    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
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
    @Column(name = "BUSINESS_TYPE")
    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    @Basic
    @Column(name = "RESOURCE_TITLE")
    public String getResourceTitle() {
        return resourceTitle;
    }

    public void setResourceTitle(String resourceTitle) {
        this.resourceTitle = resourceTitle;
    }

    @Basic
    @Column(name = "RESOURCE_ATTCHMENT")
    public String getResourceAttchment() {
        return resourceAttchment;
    }

    public void setResourceAttchment(String resourceAttchment) {
        this.resourceAttchment = resourceAttchment;
    }

    @Basic
    @Column(name = "RESOURCE_ATTCHMENT_SIZE")
    public Integer getResourceAttchmentSize() {
        return resourceAttchmentSize;
    }

    public void setResourceAttchmentSize(Integer resourceAttchmentSize) {
        this.resourceAttchmentSize = resourceAttchmentSize;
    }

    @Basic
    @Column(name = "RESOURCE_ATTCHMENT_TYPE")
    public String getResourceAttchmentType() {
        return resourceAttchmentType;
    }

    public void setResourceAttchmentType(String resourceAttchmentType) {
        this.resourceAttchmentType = resourceAttchmentType;
    }

    @Basic
    @Column(name = "RESOURCE_HEIGHT")
    public Integer getResourceHeight() {
        return resourceHeight;
    }

    public void setResourceHeight(Integer resourceHeight) {
        this.resourceHeight = resourceHeight;
    }

    @Basic
    @Column(name = "RESOURCE_WIDTH")
    public Integer getResourceWidth() {
        return resourceWidth;
    }

    public void setResourceWidth(Integer resourceWidth) {
        this.resourceWidth = resourceWidth;
    }

    @Basic
    @Column(name = "RESOURCE_STATUS")
    public String getResourceStatus() {
        return resourceStatus;
    }

    public void setResourceStatus(String resourceStatus) {
        this.resourceStatus = resourceStatus;
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
        AccountResourceInfo that = (AccountResourceInfo) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(resourceType, that.resourceType) &&
                Objects.equals(businessType, that.businessType) &&
                Objects.equals(resourceTitle, that.resourceTitle) &&
                Objects.equals(resourceAttchment, that.resourceAttchment) &&
                Objects.equals(resourceAttchmentSize, that.resourceAttchmentSize) &&
                Objects.equals(resourceAttchmentType, that.resourceAttchmentType) &&
                Objects.equals(resourceHeight, that.resourceHeight) &&
                Objects.equals(resourceWidth, that.resourceWidth) &&
                Objects.equals(resourceStatus, that.resourceStatus) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, resourceType, businessType, resourceTitle, resourceAttchment, resourceAttchmentSize, resourceAttchmentType, resourceHeight, resourceWidth, resourceStatus, createdBy, createdTime, updatedBy, updatedTime);
    }
}
