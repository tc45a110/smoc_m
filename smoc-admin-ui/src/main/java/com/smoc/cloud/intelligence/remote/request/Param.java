package com.smoc.cloud.intelligence.remote.request;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Param {

    //动态参数类型 1:表示文本类型
    private Integer type;

    //动态参数名称,不能超过 20 个字符，参 数名称不能带下划线。 参数示例:${param1}
    private String name;

    //动态参数示例 动态参数对应的示例，不能大于 100 个 字符。 参数示例:淘宝
    private String example;

}
