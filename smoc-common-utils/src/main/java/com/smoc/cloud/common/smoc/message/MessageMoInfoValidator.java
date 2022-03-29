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
    private String mobile;
    private String taskId;
    private String webTemplateId;
    private String messageContent;
    private String moDate;
    private String status;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

    private String accountName;
    private String startDate;
    private String endDate;
}
