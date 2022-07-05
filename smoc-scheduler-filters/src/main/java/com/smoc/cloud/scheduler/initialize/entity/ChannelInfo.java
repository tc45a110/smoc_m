package com.smoc.cloud.scheduler.initialize.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChannelInfo {

    /**
     * 通道id
     */
    private String channelID;

    /**
     * 通道名称
     */
    private String channelName;

    /**
     * 通道状态
     */
    private String channelStatus;

    /**
     * 通道运行状态
     */
    private String channelRunStatus;

    /**
     * 通道速率
     */
    private int maxSendSecond;

    /**
     * 通道连接数
     */
    private int connectNumber;

    /**
     * 通道接入码
     */
    private String channelSRCID;

    /**
     * 通道区域范围:PROVINCE、INTL、COUNTRY
     */
    private String businessAreaType;

    /**
     * 计价方式:AREA_PRICE、UNIFIED_PRICE
     */
    private String priceStyle;

    /**
     * 支持的区域范围
     */
    private String supportAreaCodes;
}
