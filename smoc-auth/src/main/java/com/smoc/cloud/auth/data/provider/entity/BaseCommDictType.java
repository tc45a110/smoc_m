package com.smoc.cloud.auth.data.provider.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * 2019/5/21 17:12
 **/
@Entity
@Table(name = "base_comm_dict_type")
public class BaseCommDictType {

    private String id;
    private String dictTypeName;
    private String dictTypeCode;
    private String dictTypeSystem;
    private String icon;
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
    @Column(name = "DICT_TYPE_NAME")
    public String getDictTypeName() {
        return dictTypeName;
    }

    public void setDictTypeName(String dictTypeName) {
        this.dictTypeName = dictTypeName;
    }

    @Basic
    @Column(name = "DICT_TYPE_SYSTEM")
    public String getDictTypeSystem() {
        return dictTypeSystem;
    }

    public void setDictTypeSystem(String dictTypeSystem) {
        this.dictTypeSystem = dictTypeSystem;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Basic
    @Column(name = "ICON")
    public String getIcon() {
        return icon;
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

    @Basic
    @Column(name = "DICT_TYPE_CODE")
    public String getDictTypeCode() {
        return dictTypeCode;
    }

    public void setDictTypeCode(String dictTypeCode) {
        this.dictTypeCode = dictTypeCode;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseCommDictType that = (BaseCommDictType) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(dictTypeName, that.dictTypeName) &&
                Objects.equals(dictTypeSystem, that.dictTypeSystem) &&
                Objects.equals(sort, that.sort) &&
                Objects.equals(active, that.active) &&
                Objects.equals(createDate, that.createDate) &&
                Objects.equals(editDate, that.editDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dictTypeName, dictTypeSystem, sort, active, createDate, editDate);
    }


}
