package com.smoc.cloud.configure.channel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "config_channel_change_items")
public class ConfigChannelChangeItem {
    @Id
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    @Column(name = "CHANGE_ID", nullable = false, length = 32)
    private String changeId;

    @Column(name = "BUSINESS_ACCOUNT", nullable = false, length = 32)
    private String businessAccount;

    @Column(name = "CHANGE_TYPE", nullable = false, length = 32)
    private String changeType;

    @Column(name = "CHANGE_BEFORE_PRIORITY", nullable = false, length = 32)
    private String changeBeforePriority;

    @Column(name = "CHANGE_AFTER_PRIORITY", nullable = false, length = 32)
    private String changeAfterPriority;

    @Column(name = "CREATED_BY", nullable = false, length = 32)
    private String createdBy;

    @Column(name = "CREATED_TIME", nullable = false)
    private Instant createdTime;

    @Column(name = "UPDATED_BY", length = 32)
    private String updatedBy;

    @Column(name = "UPDATED_TIME")
    private Instant updatedTime;

    public Instant getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Instant updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getChangeAfterPriority() {
        return changeAfterPriority;
    }

    public void setChangeAfterPriority(String changeAfterPriority) {
        this.changeAfterPriority = changeAfterPriority;
    }

    public String getChangeBeforePriority() {
        return changeBeforePriority;
    }

    public void setChangeBeforePriority(String changeBeforePriority) {
        this.changeBeforePriority = changeBeforePriority;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getBusinessAccount() {
        return businessAccount;
    }

    public void setBusinessAccount(String businessAccount) {
        this.businessAccount = businessAccount;
    }

    public String getChangeId() {
        return changeId;
    }

    public void setChangeId(String changeId) {
        this.changeId = changeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}