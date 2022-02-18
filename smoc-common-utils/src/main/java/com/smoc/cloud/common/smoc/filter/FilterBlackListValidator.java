package com.smoc.cloud.common.smoc.filter;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Setter
@Getter
public class FilterBlackListValidator {
    private String id;
    private String enterpriseId;
    private String groupId;
    private String name;
    private String mobile;
    private String isSync;
    private String status;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;
    private String createdTimeStr;

}
