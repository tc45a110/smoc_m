package com.smoc.cloud.common.smoc.query.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
public class ChannelSendStatisticModel {

    private String messageDate;

    private String channelId;

    private String channelName;

    private String startDate;

    private String endDate;

    private Integer customerSubmitNum;

    private Integer successSubmitNum;

    private Integer failureSubmitNum;

    private Integer messageSuccessNum;

    private Integer messageFailureNum;

    private Integer messageNoReportNum;
}
