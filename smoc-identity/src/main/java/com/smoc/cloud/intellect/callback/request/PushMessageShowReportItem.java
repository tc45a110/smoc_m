package com.smoc.cloud.intellect.callback.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PushMessageShowReportItem {

    //客户标识 如手机号、用户 ID 等
    private String custFlag;

    //智能短信平台生成的模板 ID，由 9 位数字 组成。
    private String tplId;

    //智能短信短链 带 URL 地址的智能短信短链
    private String aimUrl;

    //智能短信编码 不带 URL 地址前缀的智能短信编码
    private String aimCode;

    //客户业务系统内的标识。 最大长度不超过 64 个字符。
    private String custId;

    //自定义扩展参数 自定义的扩展数据，最大长度 300 个字 符，可以是标签数据等。
    private String extData;

    //解析状态  0:成功 非 0:失败
    private Integer status;

    //回执错误码描述
    private String describe;
}
