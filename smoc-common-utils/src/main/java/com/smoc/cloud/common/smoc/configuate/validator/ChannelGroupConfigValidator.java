package com.smoc.cloud.common.smoc.configuate.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ChannelGroupConfigValidator {
    private String id;
    private String channelGroupId;
    private String channelId;
    private Integer channelPriority;
    private Integer channelWeight;
    private String createdBy;
    private Date createdTime;

}
