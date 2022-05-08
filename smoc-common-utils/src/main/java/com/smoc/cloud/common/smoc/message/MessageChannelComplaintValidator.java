package com.smoc.cloud.common.smoc.message;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageChannelComplaintValidator {

    private String carrier;
    private String channelId;
    private String channelName;
    private String complaintNum;
    private String messageNum;
    private String complaintRate;
    private String maxComplaintRate;

    private String month;
}
