package com.smoc.cloud.intellect.remote.request;


import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class PushTemplateStatusReportItem {

    //智能短信平台生成的模板 ID，由 9 位数字 组成。
    private String tplId;

    //业务 Id
    private String bizId;

    //业务标识
    private String bizFlag;

    //模板状态 0:禁用 1:启用 模板状态为 1 时，表示模板可用
    private Integer tplState;

    //审核状态 审核总状态 1:待审核 2:审核通过 3:审核未通过
    private Integer auditState;

    //厂商审核状态 各终端厂商的审核状态
    private List<Map<String,String>> factoryInfoList;

    //审核意见
    private String auditDesc;
}
