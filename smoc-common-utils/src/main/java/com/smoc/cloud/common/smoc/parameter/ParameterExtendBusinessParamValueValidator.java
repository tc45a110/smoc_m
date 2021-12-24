package com.smoc.cloud.common.smoc.parameter;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ParameterExtendBusinessParamValueValidator {

    private String id;

    private String businessType;

    private String businessId;

    private String paramName;

    private String paramKey;

    private String paramValue;

    private String createdBy;

    private Date createdTime;
}
