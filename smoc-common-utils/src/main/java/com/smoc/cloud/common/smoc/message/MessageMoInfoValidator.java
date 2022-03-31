package com.smoc.cloud.common.smoc.message;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Setter
@Getter
public class MessageMoInfoValidator {
    private String id;
    private String enterpriseId;
    private String accountId;
    private String channelId;
    private String channelSrc;
    private String srcId;
    private String businessType;
    private String infoType;
    private String mobile;
    private String taskId;
    private String webTemplateId;
    private String moMessageContent;
    private String moDate;
    private String mtMessageContent;
    private String mtDate;
    private String status;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

    private String accountName;
    private String startDate;
    private String endDate;
}
