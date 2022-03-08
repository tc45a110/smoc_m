package com.smoc.cloud.scheduler.channel.price.service.model;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ChannelPriceModel {

    private String id;

    //通道id
    private String channelId;

    //价格区域编码
    private String areaCode;

    //价格日期
    private String priceData;

    //通道价格
    private BigDecimal channelPrice;

    //上次更新时间
    private String batchDate;

    //距离上次批处理 相隔天数
    private Integer days;

    //创建日期
    private String createTime;
}
