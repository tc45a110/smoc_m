package com.smoc.cloud.common.iot.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class IotFlowCardsChangeHistoryValidator {

    private String id;

    private String iccid;

    private String imsi;

    private String msisdn;

    private String originalStatus;

    private String targetStatus;

    private String changeTime;
}
