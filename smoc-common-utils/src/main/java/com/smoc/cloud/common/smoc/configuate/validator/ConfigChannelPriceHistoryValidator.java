package com.smoc.cloud.common.smoc.configuate.validator;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
public class ConfigChannelPriceHistoryValidator {

    private String id;
    private String sourceId;
    private String channelId;
    private String priceStyle;
    private String areaCode;
    private BigDecimal channelPrice;
    private String priceDate;
    private String createTime;
    private String updatedBy;
    private String updatedTime;

    private String startDate;
    private String endDate;
}
