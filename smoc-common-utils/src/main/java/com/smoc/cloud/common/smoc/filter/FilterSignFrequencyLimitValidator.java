package com.smoc.cloud.common.smoc.filter;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class FilterSignFrequencyLimitValidator {

    private String id;
    private String limitType;
    private String infoType;
    private String signs;
    private String accounts;
    private String frequency;
    private String status;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private Date updatedTime;
}
