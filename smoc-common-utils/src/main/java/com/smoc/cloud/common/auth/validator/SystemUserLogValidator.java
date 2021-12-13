package com.smoc.cloud.common.auth.validator;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;

@Setter
@Getter
public class SystemUserLogValidator {

    private String id;

    private String userId;

    private String module;

    private String moduleId;

    private String operationType;

    private String simpleIntroduce;

    private String logData;

    private Date createdTime;

}