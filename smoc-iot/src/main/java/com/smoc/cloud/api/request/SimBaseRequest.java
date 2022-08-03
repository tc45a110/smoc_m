package com.smoc.cloud.api.request;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Setter
@Getter
public class SimBaseRequest extends BaseRequest{

    @NotNull(message = "iccid不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z]{10,24}", message = "iccid不符合规则不符合规则！")
    private String iccid;

}
