package com.smoc.cloud.intelligence.remote.request;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 智能模版提交请求参数
 */
@Setter
@Getter
public class RequestSubmitTemplate {


    //版式 ID
    private String cardId;

    //模板名称
    private String tplName;

    //使用场景 不能超过 10 个字符
    private String scene;

    //模板用途  1:表示商用 2:表示试商用
    private String useId;

    //业务 Id
    private String bizId;

    //短信示例 所要发送的短信原文的示例，用于模版审核 时使用，最大 70 个字符。
    private String smsExample;

    //模板协议 最大支持 10 页协议
    private List<Page> pages;

    //模板参数集 最大支持 20 个动态参数
    private List<Param> params;

    //图片比例 指定模板提交的厂商，不填则由梦网运营人 员选择提交的厂商。 厂商类型: HuaWei:表示华为厂商 XiaoMi:表示小米厂商 OPPO:表示 OPPO 厂商 VIVO:表示 VIVO 厂商 MEIZU:表示魅族厂商 注：各厂商支持情况，请咨询梦网支撐人员
    private String[] factorys;

    //图片比例
    private String imageRate;
}
