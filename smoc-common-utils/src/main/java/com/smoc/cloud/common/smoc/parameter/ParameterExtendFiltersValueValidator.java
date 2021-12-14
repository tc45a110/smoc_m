package com.smoc.cloud.common.smoc.parameter;
import lombok.Data;


import java.util.Date;

@Data
public class ParameterExtendFiltersValueValidator {

    private String id;

    private String businessType;

    private String businessId;

    private String paramName;

    private String paramKey;

    private String paramValue;

    private String createdBy;

    private Date createdTime;
}
