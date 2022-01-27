package com.smoc.cloud.common.gateway.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 自定义标准头部请求参数
 */
@Setter
@Getter
public class RequestStardardHeaders {

    @NotNull(message = "signatureNonce不能为空！")
    @Pattern(regexp = "^[0-9]{27}", message = "signatureNonce不符合规则！")
    private String signatureNonce;
    @NotNull(message = "signature不能为空！")
    private String signature;

}
