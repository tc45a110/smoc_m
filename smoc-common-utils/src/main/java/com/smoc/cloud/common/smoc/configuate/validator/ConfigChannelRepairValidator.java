package com.smoc.cloud.common.smoc.configuate.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
public class ConfigChannelRepairValidator {

    private String channelId;

    private String channelName;

    private String carrier;

    private String businessType;

    private String infoType;

    private String repairStatus;

    private String rowspan;

    private String flag;

    private String repairCode;
    private String repairDate;

    private List<ConfigChannelRepairRuleValidator> repairList = new ArrayList<>();
}
