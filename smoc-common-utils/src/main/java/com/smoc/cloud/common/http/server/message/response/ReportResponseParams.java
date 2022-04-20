package com.smoc.cloud.common.http.server.message.response;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReportResponseParams {

    //id 唯一标识
    private String id;

    //业务账号
    private String account;

    //手机号
    private String mobile;

    //状态吗
    private String statusCode;

    //报告时间
    private String reportTime;

    //客户提交时间
    private String submitTime;

    //订单号
    private String orderNo;

    //模版id
    private String templateId;

    //扩展吗
    private String extNumber;

    //客户业务分类
    private String business;

    //总条数
    private Integer messageTotal;

    //当前条数
    private Integer messageIndex;


}
