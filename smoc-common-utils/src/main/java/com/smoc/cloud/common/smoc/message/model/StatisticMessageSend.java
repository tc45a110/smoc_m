package com.smoc.cloud.common.smoc.message.model;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class StatisticMessageSend {

    //提交号码数
    private int submitNumber;

    //发送总数
    private int successNumber;

    //发送成功总数
    private int successSendNumber;

    //失败总数
    private int failureNumber;

    //未知总数
    private int noReportNumber;

}
