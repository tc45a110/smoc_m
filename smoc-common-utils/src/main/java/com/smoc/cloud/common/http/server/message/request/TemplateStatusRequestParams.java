package com.smoc.cloud.common.http.server.message.request;


import com.smoc.cloud.common.gateway.request.HttpServerSignModel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Setter
@Getter
public class TemplateStatusRequestParams extends HttpServerSignModel {

    @NotNull(message = "模板ID不能为空！")
    @Pattern(regexp = "^[0-9A-Za-z]{6,16}", message = "模板ID不符合规则！")
    private String templateId;

}
