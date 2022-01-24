package com.smoc.cloud.common.gateway.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter

public class RequestSignModel {

    @NotNull(message = "订单号不能为空！")
    @Pattern(regexp = "^[0-9]{27}", message = "订单号不符合规则！")
    private String orderNo;
    @NotNull(message = "姓名不符合规则")
    private String name;
    @Pattern(regexp = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}[0-9Xx]$)", message = "身份证号不符合规则")
    private String cardNo;

}
