package com.smoc.cloud.intellect.remote.request;


import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class RequestParamList {


    //客户标识 如手机号、用户 ID 等，长度不超过 64 个字符
    private String custFlag;

    //自定义消息ID 客户业务系统内的标识。 最大长度不超过 64 个字符。
    private String custId;

    //自定义扩展参数 自定义的扩展数据，最大长度 300 个字 符，可以是标签数据等。
    private String extData;

    //动态参数 模板动态参数集体，KEY 为参数名（模板 动态参数为“参数 N”，KEY 前缀为 param， 如 模 板 动 参 使 用 自 定 义 别 名 或 默 认 “param”时，KEY 则使用自定义别名或 “param”），VALUE 为参数值。
    private Map<String, String> dyncParams;

    //自定义跳转地址  长度不超过 128 个字符。 未填时，终端用户点击访问短信原文中 的短链，跳转智能短信 H5 页； 已填时，终端用户点击访问短信原文中 的短链，跳转客户填写的链接落地页， 填写时必须为 http 或 https 做为前缀。
    private String customUrl;
}
