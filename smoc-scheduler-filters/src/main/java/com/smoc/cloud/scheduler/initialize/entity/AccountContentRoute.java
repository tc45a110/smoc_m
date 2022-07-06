package com.smoc.cloud.scheduler.initialize.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class AccountContentRoute {

    /**
     * 业务账号
     */
    private String accountId;

    /**
     * 运营商
     */
    private String carrier;

    /**
     * 内容路由配置省份编码
     */
    private String areaCodes;

    /**
     * 通道本身支持省份编码
     */
    private String channelAreaCodes;

    /**
     * 通过计算后的，本通道支持的业务区域
     */
    private Set<String> supportAreaCodes;

    /**
     * 路由内容
     */
    private String routeContent;

    /**
     * 反向路由内容，包含则不路由
     */
    private String routeReverseContent;

    /**
     * 路由手机号段
     */
    private String mobileNum;

    /**
     * 路由短信字数小于值
     */
    private int minContent;

    /**
     * 路由短信字数大于值
     */
    private int maxContent;

    /**
     * 内容路由通道Id
     */
    private String routeChannelId;
}
