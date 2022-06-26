package com.smoc.cloud.scheduler.message.service.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class MessageTimingModel {

    private String id;

    private String accountId;

    //上传方式
    private String upType;

    private String inputMobile;

    private String mobileFile;

    private String groupId;

    private String businessType;

    private String messageType;

    private String templateId;

    private String messageContent;
}
