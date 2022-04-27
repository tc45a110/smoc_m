package com.smoc.cloud.intelligence.remote.request;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestQueryPublicTemplates {

    //模板 ID
    private String tplId;

    //模板名称
    private String tplName;

    //创建开始时间  格式为 yyyy-MM-dd HH:mm:ss
    private String beginTime;

    //创建结束时间  格式为 yyyy-MM-dd HH:mm:ss
    private String endTime;

    //查询起始页
    private Integer page;

    //查询条数  页容量，不超过 100 条
    private Integer size;
}
