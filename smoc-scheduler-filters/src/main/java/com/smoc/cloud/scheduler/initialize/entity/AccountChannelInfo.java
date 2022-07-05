package com.smoc.cloud.scheduler.initialize.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * 业务账号通道i西南西
 */
@Setter
@Getter
public class AccountChannelInfo {

//    private String id;
    /**
     * 业务账号
     */
    private String accountId;
    /**
     * 配置类型
     */
    private String configType;
    /**
     * 通道支持运营商
     */
    private String carrier;

    /**
     * 通道业务支持类型 PROVINCE 为分省支持；COUNTRY 为全国；INTL为国际
     */
    private String businessAreaType;

    /**
     * businessAreaType前提下，支持的区域范围
     */
    private String supportAreaCodes;

    /**
     * 通道支持省份代码
     */
    private Set<String> areaCodes;
    /**
     * 通道组id
     */
    private String channelGroupId;
    /**
     * 通道id
     */
    private String channelId;
    /**
     * 通道优先级
     */
    private String channelPriority;
    /**
     * 通道权重
     */
    private int channelWeight;

    /**
     * 通道状态
     */
    private String channelStatus;
}
