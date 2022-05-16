package com.smoc.cloud.common.smoc.intelligence;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class IntellectCallbackShowReportValidator {

    private String id;

    private String orderNo;

    private String custFlag;

    private String custId;

    private String tplId;

    private String aimUrl;

    private String aimCode;

    private String extData;

    private Integer status;

    private String describe;

    private String createdBy;

    private String createdTime;
}
