package com.smoc.cloud.api.request;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Setter
@Getter
public class SimStatusChangeRequest extends BaseRequest{

    @NotNull(message = "iccid不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z]{10,24}", message = "iccid不符合规则不符合规则！")
    private String iccid;

    //变更类型
    @NotNull(message = "变更类型不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z]{1,4}", message = "变更类型不符合规则不符合规则！")
    private String changeType;

}
