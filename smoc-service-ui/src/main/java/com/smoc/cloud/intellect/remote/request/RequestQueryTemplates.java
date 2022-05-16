package com.smoc.cloud.intellect.remote.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 模板查询请求参数
 */
@Setter
@Getter
public class RequestQueryTemplates {

    //模板 ID
    private String tplId;

    //模板名称
    private String tplName;

    //模板状态 0:禁用 1:启用
    private Integer tplState;

    //审核状态  0:未提交 1:审核中 2:审核通过 3:审核失败
    private Integer auditState;

    //业务 Id 由客户根据其业务场景定义的业务 ID。 最大长度不超过 50 个字符。 参数示例: 部门 ID:10000001
    private String bizId;

    //业务标识 由客户根据其业务场景定义的业务标 识，最大支持长度 256 个字符。
    private String bizFlag;

    //创建开始时间  格式为 yyyy-MM-dd HH:mm:ss
    private String beginTime;

    //创建结束时间  格式为 yyyy-MM-dd HH:mm:ss
    private String endTime;

    //查询起始页
    private Integer page;

    //查询条数  页容量，不超过 100 条
    private Integer size;

    private Long templateId;


}
