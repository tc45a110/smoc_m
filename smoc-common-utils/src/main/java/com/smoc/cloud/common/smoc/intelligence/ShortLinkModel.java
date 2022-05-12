package com.smoc.cloud.common.smoc.intelligence;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ShortLinkModel {

    @NotNull(message = "企业id不能为空！")
    private String enterpriseId;

    @NotNull(message = "业务账号不能为空！")
    private String accountId;

    //模板 ID  智能短信平台生成的模板 ID，由 9 位数 字组成。
    @NotNull(message = "模版id不能为空！")
    private String tplId;

    //短信签名  终端接收到短信的签名，与短链内的智 能短信编码对应，最多 10 个签名，每个 签名长度不超过 18 个字 注:签名内容不包括前后中英文括号
    @NotNull(message = "短信签名不能为空！")
    @Size(min = 1, max = 128, message = "短信签名！")
    private String smsSigns;

    //显示次数 群发单个智能短信短链短信给 N 个号码 的场景，防止展示次数超过预期带来安 全风险，指定此智能短信短链最大成功 展示次数。
    private Integer showTimes;

    //智能短信编码类型 智能短信编码类型 1:群发 2:个性化
    private Integer aimCodeType;

    //自定义短连域名 发送账号分配的短链域名，需要提前报 备，不填时，如果账号只分配一个域名， 则使用分配的域名，如果账号分配多个 或未分配域名，则使用系统默认域名， 长度不超过 100 个字符。
    private String domain;

    //失效时间 智能短信编码类型为群发：时间可任意 填，不填则默认 7 天； 智能短信编码类型为个性化：时间最小 1 天，最大 7
    private Integer expireTimes;

    //终端用户点击访问短信原文中 的短链，跳转客户填写的链接落地页；不填写则跳转智能短信 H5 页；填写时必须为 http 或 https 做为前缀。
    @Size(min = 0, max = 128, message = "自定义跳转地址不符合规则！")
    private String customUrl;

    private String params;
}
