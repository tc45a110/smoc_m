package com.smoc.cloud.http.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class MessageFormat {

    // id
    private Long id;
    //账号
    private String accountId;
    //手机号码
    private String phoneNumber;
    //提交时间
    private String submitTime;
    //短信内容
    private String messageContent;
    //内容编码
    private String messageFormat;
    //订单id
    private String messageId;
    //模版id
    private String templateId;
    //协议
    private String protocol;
    //扩展吗
    private String accountSrcId;
    //客户自定义业务编码
    private String accountBusinessCode;
    //批次号码数
    private Integer phoneNumberNumber;
    //一个批次包含的消息条数,长短信算多条
    private Integer messageContentNumber;
    //是否需要回传状态报告标识
    private Integer reportFlag;
    //HTTP协议,客户提交的可选参数,状态报告和上行会附带该值
    private String optionParam;
    //创建时间
    private Date createTime;
}
