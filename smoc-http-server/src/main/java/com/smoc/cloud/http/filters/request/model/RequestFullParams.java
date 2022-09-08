package com.smoc.cloud.http.filters.request.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Setter
@Getter
public class RequestFullParams {

    //phone     手机号  字段为空，则跳出所有涉及该字段的过滤
    @Pattern(regexp = "^[0-9]{11}", message = "手机号不符合规则！")
    private String phone;

    //account   业务账号 字段为空，则跳出所有涉及该字段的过滤
    private String account;

    //message   消息内容
    private String message;

    private String templateId;

    //carrier   运营商  CMCC、UNIC、TELC、INTL 字段为空，则跳出所有涉及该字段的过滤
    @Pattern(regexp = "(CMCC|UNIC|TELC){1}", message = "运营商不符合规则！")
    private String carrier;

    //province  省份编码 字段为空，则跳出所有涉及该字段的过滤
    private String provinceCode;

    //通道id
    private String channelId;

    //拆分后短信条数
    private Integer numbers = 1;

}
