package com.smoc.cloud.common.smoc.configuate.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ConfigChannelChangeValidator {

    private String id;

    private String channelId;

    private Integer accountNum;

    private String changeType;

    private String changeReason;

    private String changeStatus;

    private String createdBy;

    private String createdTime;

    private String updatedBy;

    private Date updatedTime;

    private String channelName;
    private String accountIds;
}
