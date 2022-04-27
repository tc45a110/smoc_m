package com.smoc.cloud.intelligence.remote.response;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseMobile {

    //手机号  接收手机号。为号码国家码与手机号码 的 SHA1 算法后的摘要。例如:原文为: “8613000000000”，则填入 SHA1 摘要: “ ca79e299afc7645a26ac0066b74afd90 50d7fc26” 注意:国家码格式为纯数字，不带‘+’
    private String mobile;

    //是否有接收智能 短信能力  0:否1:是
    private Integer receiveState;

    //自定义消息 ID  客户业务系统内的标识。 最大长度不超 过 64 个字符。
    private String custId;

    //自定义扩展数据 自定义的扩展数据，最大长度 300 个字 符，可以是标签数据等
    private String extData;
}
