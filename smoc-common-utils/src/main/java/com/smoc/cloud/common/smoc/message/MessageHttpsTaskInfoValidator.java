package com.smoc.cloud.common.smoc.message;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Setter
@Getter
public class MessageHttpsTaskInfoValidator {
    private String id;
    private String subject;
    private String templateId;
    private String enterpriseId;
    private String businessAccount;
    private String businessType;
    private String infoType;
    private String messageType;
    private String sendType;
    private String expandNumber;
    private Integer splitNumber;
    private Integer submitNumber;
    private Integer successNumber;
    private Integer successSendNumber;
    private Integer failureNumber;
    private Integer noReportNumber;
    private String sendTime;
    private String messageContent;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private Date updatedTime;

    private String startDate;
    private String endDate;
}
