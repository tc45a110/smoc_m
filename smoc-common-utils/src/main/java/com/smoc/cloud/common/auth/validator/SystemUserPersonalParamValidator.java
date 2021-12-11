package com.smoc.cloud.common.auth.validator;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class SystemUserPersonalParamValidator {

    private String id;

    private String userId;

    private String paramKey;

    private String paramValue;

    private String createdBy;

    private Instant createdTime;

}