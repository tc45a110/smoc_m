package com.smoc.cloud.intellect.remote.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 申请短链
 */
@Setter
@Getter
public class RequestApplyShortUrl {

    //模板 ID  智能短信平台生成的模板 ID，由 9 位数 字组成。
    private String tplId;

    //参数对象集合  接收智能短信测试短信手机号及动态参 数对象列表，最大为 100 个 注：oppo 模板一次最多只能申请 10 个
    private List<RequestParamList> paramList;

    //短信签名  终端接收到短信的签名，与短链内的智 能短信编码对应，最多 10 个签名，每个 签名长度不超过 18 个字 注:签名内容不包括前后中英文括号
    private String[] smsSigns;

    //显示次数 群发单个智能短信短链短信给 N 个号码 的场景，防止展示次数超过预期带来安 全风险，指定此智能短信短链最大成功 展示次数。
    private Integer showTimes;

    //智能短信编码类型 智能短信编码类型 1:群发 2:个性化
    private Integer aimCodeType;

    //自定义短连域名 发送账号分配的短链域名，需要提前报 备，不填时，如果账号只分配一个域名， 则使用分配的域名，如果账号分配多个 或未分配域名，则使用系统默认域名， 长度不超过 100 个字符。
    private String domain;

    //失效时间 智能短信编码类型为群发：时间可任意 填，不填则默认 7 天； 智能短信编码类型为个性化：时间最小 1 天，最大 7
    private Integer expireTimes;

}
