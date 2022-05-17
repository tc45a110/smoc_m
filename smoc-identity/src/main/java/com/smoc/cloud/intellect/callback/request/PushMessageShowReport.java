package com.smoc.cloud.intellect.callback.request;


import lombok.Getter;
import lombok.Setter;

/**
 * 推送智能短信终端展示状态回执
 */
@Setter
@Getter
public class PushMessageShowReport {

    //请求包序列号 唯一的标识本次推送请求，最大长度不超 过 64 个字符。
    private String custId;

    private PushMessageShowReportItem msgBody;
}
