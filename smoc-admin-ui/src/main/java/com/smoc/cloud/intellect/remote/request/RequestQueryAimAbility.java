package com.smoc.cloud.intellect.remote.request;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 获取号码接收智能短信能力
 */
@Setter
@Getter
public class RequestQueryAimAbility {

    //模板ID 智能短信平台生成的模板 ID，由 9 位数 字组成。 填写时，根据该模板所支持的厂商返回 手机终端展示智能短信的能力状态；不 填则返回手机终端在所有厂商展示智能 短信的能力状态。
    private String tplId;

    //参数对象集合 接收手机号，最大支持 100 个号码
    private List<RequestMobile> mobiles;
}
