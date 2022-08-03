package com.smoc.cloud.api.request;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Setter
@Getter
public class BaseRequest {
    @NotNull(message = "账号不能为空！")
    @Pattern(regexp = "^(IOT){1}[0-9]{9}", message = "账号不符合规则！")
    private String account;

    @NotNull(message = "时间戳不能为空！")
    @Pattern(regexp = "^[0-9]{17}", message = "时间戳不符合规则！")
    private String timestamp;
}
