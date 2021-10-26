package com.smoc.cloud.common.auth.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class FlowApproveValidator {

    private String id;
    private String approveType;
    private String approveId;
    private Date submitTime;
    private String userId;
    private String approveAdvice;
    private Date approveTime;
    private Integer approveStatus;
    private String userApproveId;
    private Integer flowStatus;
    private String busiUrl;

    private String userName;//提交人
    private String checkName;//审核人
    private String submitTimeStr;
    private String approveTimeStr;
}
