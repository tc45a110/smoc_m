package com.smoc.cloud.common.smoc.configuate.validator;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
public class ChannelPriceValidator {
    private String id;
    private String channelId;
    private String priceStyle;
    private String areaCode;
    private BigDecimal channelPrice;
    private String lasttimeHistory;
    private String createdBy;
    private String updatedBy;
    private Date updatedTime;

    private List<ChannelPriceValidator> prices = new ArrayList<>();
}
