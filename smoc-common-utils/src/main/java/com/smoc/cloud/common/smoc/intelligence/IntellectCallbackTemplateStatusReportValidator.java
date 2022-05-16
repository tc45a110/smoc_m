package com.smoc.cloud.common.smoc.intelligence;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class IntellectCallbackTemplateStatusReportValidator {

    private String id;

    private String orderNo;

    private String tplId;

    private String bizId;

    private String bizFlag;

    private Integer tplState;

    private Integer auditState;

    private String factoryInfoList;

    private String auditDesc;

    private String factoryType;

    private Integer state;

    private String createdBy;

    private String createdTime;

    private String enterpriseName;
}
