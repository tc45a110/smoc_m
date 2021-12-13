package com.smoc.cloud.auth.data.provider.entity;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Table(name = "system_user_logs", indexes = {
        @Index(name = "index_system_user_logs_query", columnList = "MODULE_ID, MODULE")
})
@Entity
public class SystemUserLog {
    @Id
    @Column(name = "ID", nullable = false, length = 32)
    private String id;

    @Column(name = "USER_ID", nullable = false, length = 32)
    private String userId;

    @Column(name = "MODULE", nullable = false, length = 32)
    private String module;

    @Column(name = "MODULE_ID", nullable = false, length = 32)
    private String moduleId;

    @Column(name = "OPERATION_TYPE", nullable = false, length = 32)
    private String operationType;

    @Column(name = "SIMPLE_INTRODUCE")
    private String simpleIntroduce;

    @Lob
    @Column(name = "LOG_DATA")
    private String logData;

    @Column(name = "CREATED_TIME", nullable = false)
    private Date createdTime;

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getLogData() {
        return logData;
    }

    public void setLogData(String logData) {
        this.logData = logData;
    }

    public String getSimpleIntroduce() {
        return simpleIntroduce;
    }

    public void setSimpleIntroduce(String simpleIntroduce) {
        this.simpleIntroduce = simpleIntroduce;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}