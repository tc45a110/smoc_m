package com.smoc.cloud.common.smoc.configuate.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ConfigChannelChangeItemValidator {

    private String id;

    private String changeId;

    private String businessAccount;

    private String changeType;

    private String changeBeforePriority;

    private String changeAfterPriority;

    private String accountChannelId;

    private String status;

    private String createdBy;

    private String createdTime;

    private String updatedBy;

    private Date updatedTime;
}
