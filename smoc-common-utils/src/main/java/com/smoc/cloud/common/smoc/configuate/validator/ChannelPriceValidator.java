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
    private String createdTime;
    private String createdBy;
    private String updatedBy;
    private Date updatedTime;
    private String lasttimeHistory;

    //数据库批量执行标识1:添加 2：更新
    private String flag;

    private List<ChannelPriceValidator> prices = new ArrayList<>();
}
