package com.smoc.cloud.intellect.remote.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseAccountBalance {

    //账号
    private String account;

    //查询结果状态 0: 成功 非 0: 失败 如果失败,则余额信息为空
    private Integer queryState;

    //查询余额结果描 述
    private String queryDesc;

    //产品类型 产品类型： 21:AIM(短信+解析) 22:AIM+(纯解析)
    private String productType;

    //计费对象类型: 0:按账号计费 1:按企业计费
    private String chargeObj;

    //结算类型： 1:预付 2:后付
    private String settleType;

    //扣费类型 0:按条扣费 1:按金额扣费
    private String chargeType;

    //数量；当 chargeType=0 时，表示指定产品剩 余总条数，例如：999999； 当 chargeType=1 时，表示计费对象的 剩余总金额，精度为小数点后两位，例 如：6.80
    private String chargeValue;

}
