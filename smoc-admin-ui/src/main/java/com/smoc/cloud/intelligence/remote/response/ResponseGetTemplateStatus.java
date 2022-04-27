package com.smoc.cloud.intelligence.remote.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 获取模版状态
 */
@Setter
@Getter
public class ResponseGetTemplateStatus {

    //模板 ID
    private String tplId;

    //模板状态 0:禁用 1:启用 模板状态为 1 时，表示模板可用
    private Integer tplState;

    //审核总状态 1:待审核 2:审核通过 3:审核未通过
    private Integer auditState;

    //厂商审核状态
    private List<Map<String,String>> factoryInfoList;

    //模板掩码 使用模板掩码调用私有化部署寻址系统接口，可返回该模板在手机终端显示智能短 信能力的状态。
    private String tplMask;

    //审核意见
    private String auditDesc;
}
