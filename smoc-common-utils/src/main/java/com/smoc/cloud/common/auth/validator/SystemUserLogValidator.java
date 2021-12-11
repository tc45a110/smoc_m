package com.smoc.cloud.common.auth.validator;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class SystemUserLogValidator {

    private String id;

    private String userId;

    private String moudle;

    private String operationType;

    private String simpleIntroduce;

    private String logData;

    private Instant createdTime;

}