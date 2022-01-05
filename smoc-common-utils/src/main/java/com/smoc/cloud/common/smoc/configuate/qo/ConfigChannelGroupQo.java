package com.smoc.cloud.common.smoc.configuate.qo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 已配置通道
 */
@Setter
@Getter
public class ConfigChannelGroupQo {

    private String id;

    private String channelId;

    //通道名称
    private String channelName;

    //信息分类
    private String infoType;

    //发送号码范围
    private String carrier;

    //通道协议类型
    private String protocol;

    //优先级
    private Integer channelPriority;

    //权重
    private Integer channelWeight;

    //通道介绍
    private String channelIntroduce;

    private String channelGroupId;
}
