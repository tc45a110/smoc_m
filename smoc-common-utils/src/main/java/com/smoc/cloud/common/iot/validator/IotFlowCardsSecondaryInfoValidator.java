package com.smoc.cloud.common.iot.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class IotFlowCardsSecondaryInfoValidator {

    private String id;

    private String cardId;

    private String imei;

    private String isMove;

    private String stopReasen;

    private String cityCode;

    private String longitude;

    private String latitude;

    private String longitudeLatitudeDate;

    private String flowSyncMonthDate;

    private String flowSyncDayDate;

    private String cycleSettlementDate;

    private String remark;
}
