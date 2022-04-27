package com.smoc.cloud.intelligence.remote.request;


import lombok.Getter;
import lombok.Setter;

/**
 * 统推送智能短信模板审核状态回执报告
 */
@Setter
@Getter
public class PushTemplateStatusReport {

    //请求包序列号 唯一的标识本次推送请求，最大长度不超 过 64 个字符。
    private String custId;

    private PushTemplateStatusReportItem msgBody;
}
