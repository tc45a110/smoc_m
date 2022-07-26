package com.smoc.cloud.common.gateway.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class IotRequestSignModel {

    @NotNull(message = "业务账号不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z]{12}", message = "业务账号不符合规则！")
    private String account;
    @NotNull(message = "时间戳不能为空！")
    @Pattern(regexp = "^(20[2-9][0-9][0-1][0-9][0-3][0-9][0-2][0-9][0-5][0-9][0-5][0-9]\\d{3}$)", message = "时间戳不符合规则！")
    private String timestamp;
}
