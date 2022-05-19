package com.smoc.cloud.filter.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "filter_key_words_info")
public class FilterKeyWordsInfo {
    private String id;
    private String keyWordsBusinessType;
    private String businessId;
    private String keyWordsType;
    private String waskKeyWords;
    private String keyWords;
    private String keyDesc;
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
    @Column(name = "KEY_WORDS_BUSINESS_TYPE")
    public String getKeyWordsBusinessType() {
        return keyWordsBusinessType;
    }

    public void setKeyWordsBusinessType(String keyWordsBusinessType) {
        this.keyWordsBusinessType = keyWordsBusinessType;
    }

    @Basic
    @Column(name = "BUSINESS_ID")
    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    @Basic
    @Column(name = "KEY_WORDS_TYPE")
    public String getKeyWordsType() {
        return keyWordsType;
    }

    public void setKeyWordsType(String keyWordsType) {
        this.keyWordsType = keyWordsType;
    }

    @Basic
    @Column(name = "WASK_KEY_WORDS")
    public String getWaskKeyWords() {
        return waskKeyWords;
    }

    public void setWaskKeyWords(String waskKeyWords) {
        this.waskKeyWords = waskKeyWords;
    }

    @Basic
    @Column(name = "KEY_WORDS")
    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    @Basic
    @Column(name = "KEY_DESC")
    public String getKeyDesc() {
        return keyDesc;
    }

    public void setKeyDesc(String keyDesc) {
        this.keyDesc = keyDesc;
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
        FilterKeyWordsInfo that = (FilterKeyWordsInfo) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(keyWordsBusinessType, that.keyWordsBusinessType) &&
                Objects.equals(businessId, that.businessId) &&
                Objects.equals(keyWordsType, that.keyWordsType) &&
                Objects.equals(keyWords, that.keyWords) &&
                Objects.equals(keyDesc, that.keyDesc) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, keyWordsBusinessType, businessId, keyWordsType, keyWords, keyDesc, createdBy, createdTime, updatedBy, updatedTime);
    }
}
