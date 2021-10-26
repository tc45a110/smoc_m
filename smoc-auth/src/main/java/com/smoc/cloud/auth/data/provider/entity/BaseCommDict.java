package com.smoc.cloud.auth.data.provider.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * 2019/5/21 17:13
 **/
@Entity
@Table(name = "base_comm_dict")
public class BaseCommDict {

    private String id;
    private String dictName;
    private String dictCode;
    private String dictType;
    private String typeId;
    private Integer sort;
    private Integer active;
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
    @Column(name = "DICT_NAME")
    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    @Basic
    @Column(name = "DICT_CODE")
    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    @Basic
    @Column(name = "TYPE_ID")
    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    @Basic
    @Column(name = "DICT_TYPE")
    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
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
    @Column(name = "ACTIVE")
    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
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
        BaseCommDict that = (BaseCommDict) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(dictName, that.dictName) &&
                Objects.equals(dictCode, that.dictCode) &&
                Objects.equals(dictType, that.dictType) &&
                Objects.equals(sort, that.sort) &&
                Objects.equals(active, that.active) &&
                Objects.equals(createDate, that.createDate) &&
                Objects.equals(editDate, that.editDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dictName, dictCode, dictType, sort, active, createDate, editDate);
    }
}
