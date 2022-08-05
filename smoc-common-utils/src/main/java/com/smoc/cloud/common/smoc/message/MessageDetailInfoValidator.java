package com.smoc.cloud.common.smoc.message;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageDetailInfoValidator {

    private String id;

    private String taskId;

    private String protocol;

    private String phoneNumber;

    private String businessAccount;

    private String accountName;

    private String sendNumber;

    private String submitStyle;

    private String sign;

    private String carrier;

    private String area;

    private String customerSubmitStatus;

    private String submitTime;

    private String sendTime;

    private String channelId;

    private String channelName;

    private String reportTime;

    private String reportStatus;

    private String customerReportTime;

    private String delayTimes;

    private String totalDelayTimes;

    private String customerStatus;

    private String messageContent;

    private String createdBy;

    private String createdTime;

    private String updatedTime;

    private String startDate;
    private String endDate;

    private String enterpriseId;

    private String enterpriseFlag;

    private String businessType;

    private int messageNumber;
    //间隔时间
    private Long timeElapsed;
}
