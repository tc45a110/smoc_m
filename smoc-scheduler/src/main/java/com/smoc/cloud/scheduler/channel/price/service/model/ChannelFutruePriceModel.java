package com.smoc.cloud.scheduler.channel.price.service.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ChannelFutruePriceModel {

    private String id;

    private String sourceId;

    //业务账号
    private String accountId;

    //运营商
    private String carrier;

    //运营商价格
    private BigDecimal carrierPrice;

    //价格日期
    private String priceDate;

    private String priceStyle;
}
