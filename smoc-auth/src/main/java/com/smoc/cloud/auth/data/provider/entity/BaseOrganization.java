package com.smoc.cloud.auth.data.provider.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "base_organization")
public class BaseOrganization {

    private String id;
    private String orgName;
    private String orgCode;
    private int orgLevel;
    private int orgType;
    private int isLeaf;
    private String parentId;
    private Integer sort;
    private int active;
    private Date createDate;
    private Date editDate;

    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ORG_NAME")
    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    @Basic
    @Column(name = "ORG_CODE")
    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    @Basic
    @Column(name = "ORG_LEVEL")
    public int getOrgLevel() {
        return orgLevel;
    }

    public void setOrgLevel(int orgLevel) {
        this.orgLevel = orgLevel;
    }

    @Basic
    @Column(name = "ORG_TYPE")
    public int getOrgType() {
        return orgType;
    }

    public void setOrgType(int orgType) {
        this.orgType = orgType;
    }

    @Basic
    @Column(name = "IS_LEAF")
    public int getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(int isLeaf) {
        this.isLeaf = isLeaf;
    }

    @Basic
    @Column(name = "PARENT_ID")
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Basic
    @Column(name = "ACTIVE")
    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
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
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Basic
    @Column(name = "EDIT_DATE")
    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseOrganization that = (BaseOrganization) o;
        return orgLevel == that.orgLevel &&
                orgType == that.orgType &&
                isLeaf == that.isLeaf &&
                active == that.active &&
                Objects.equals(id, that.id) &&
                Objects.equals(orgName, that.orgName) &&
                Objects.equals(orgCode, that.orgCode) &&
                Objects.equals(parentId, that.parentId) &&
                Objects.equals(createDate, that.createDate) &&
                Objects.equals(editDate, that.editDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orgName, orgCode, orgLevel, orgType, isLeaf, parentId, active, createDate, editDate);
    }
}
