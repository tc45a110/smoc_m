package com.smoc.cloud.customer.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "enterprise_chain_info")
public class EnterpriseChainInfo {
    private String id;
    private String documentId;
    private String signChain;
    private String signDate;
    private String signExpireDate;
    private String signChainStatus;
    private String createdBy;
    private Integer sort;
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
    @Column(name = "DOCUMENT_ID")
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    @Basic
    @Column(name = "SIGN_CHAIN")
    public String getSignChain() {
        return signChain;
    }

    public void setSignChain(String signChain) {
        this.signChain = signChain;
    }

    @Basic
    @Column(name = "SIGN_DATE")
    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    @Basic
    @Column(name = "SIGN_EXPIRE_DATE")
    public String getSignExpireDate() {
        return signExpireDate;
    }

    public void setSignExpireDate(String signExpireDate) {
        this.signExpireDate = signExpireDate;
    }

    @Basic
    @Column(name = "SIGN_CHAIN_STATUS")
    public String getSignChainStatus() {
        return signChainStatus;
    }

    public void setSignChainStatus(String signChainStatus) {
        this.signChainStatus = signChainStatus;
    }

    @Basic
    @Column(name = "SORT")
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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
        EnterpriseChainInfo that = (EnterpriseChainInfo) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(documentId, that.documentId) &&
                Objects.equals(signChain, that.signChain) &&
                Objects.equals(signDate, that.signDate) &&
                Objects.equals(signExpireDate, that.signExpireDate) &&
                Objects.equals(signChainStatus, that.signChainStatus) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, documentId, signChain, signDate, signExpireDate, signChainStatus, createdBy, createdTime, updatedBy, updatedTime);
    }
}
