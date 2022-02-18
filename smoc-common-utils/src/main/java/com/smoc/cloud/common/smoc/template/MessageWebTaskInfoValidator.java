package com.smoc.cloud.common.smoc.template;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class MessageWebTaskInfoValidator {

    private String id;

    private String subject;

    private String templateId;

    private String businessAccount;

    private String businessType;

    private String sendType;

    private String timingTime;

    private String expandNumber;

    private Integer submitNumber;

    private Integer successNumber;

    private Integer successSendNumber;

    private Integer failureNumber;

    private Integer noReportNumber;

    private String appleSendTime;

    private String sendTime;

    private String sendStatus;

    private String inputNumber;

    private String numberFiles;

    private String messageContent;

    private String createdBy;

    private String createdTime;

    private String updatedBy;

    private Date updatedTime;

    private String enterpriseName;
    private String startDate;
    private String endDate;
}
