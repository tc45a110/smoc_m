package com.smoc.cloud.common.smoc.configuate.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ChannelFilterValidator {
    private String id;
    private String channelId;
    private String regularExpression;
    private String blackList;
    private String sendTimeLimit;
    private Integer maxPhoneSendMinute;
    private Integer maxPhoneSendHour;
    private Integer maxPhoneSendDaily;
    private Integer maxSendDaily;
    private Integer maxSendMonth;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

}
