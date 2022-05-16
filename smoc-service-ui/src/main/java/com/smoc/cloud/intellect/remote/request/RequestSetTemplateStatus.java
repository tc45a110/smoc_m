package com.smoc.cloud.intellect.remote.request;


import lombok.Getter;
import lombok.Setter;

/**
 * 启用、禁止模板
 */
@Setter
@Getter
public class RequestSetTemplateStatus {

    //智能短信平台模 板 ID
    private String tplId;

    //模板状态 0:禁用 1:启用
    private String tplState;

    //禁用原因 禁用时必填
    private String disableDesc;
}
