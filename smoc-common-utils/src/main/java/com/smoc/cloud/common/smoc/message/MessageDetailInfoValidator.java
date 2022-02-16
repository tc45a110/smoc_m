package com.smoc.cloud.common.smoc.message;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class MessageDetailInfoValidator {

    private String id;

    private String phoneNumber;

    private String businessAccount;

    private String sendNumber;

    private String submitStyle;

    private String sign;

    private String carrier;

    private String area;

    private String customerSubmitStatus;

    private String sendTime;

    private String channelId;

    private String reportTime;

    private String reportStatus;

    private String customerReportTime;

    private String delayTimes;

    private String totalDelayTimes;

    private String customerStatus;

    private String messageContent;

    private String createdBy;

    private Date createdTime;

    private Date updatedTime;
}
