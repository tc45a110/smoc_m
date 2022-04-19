package com.smoc.cloud.common.http.server.message.request;


import com.smoc.cloud.common.gateway.request.HttpServerSignModel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Setter
@Getter
public class SendMessageByTemplateRequestParams extends HttpServerSignModel {

    @NotNull(message = "模板ID不能为空！")
    @Pattern(regexp = "^TEMP[0-9]{1,9}", message = "模板ID不符合规则！")
    private String templateId;

    //13915863571|${不高}|${压弯}
    @NotNull(message = "模板短信内容不能为空！")
    private String[] content;

    //扩展号码
    @Pattern(regexp = "^[0-9]{0,4}", message = "扩展号码不符合规则！")
    private String extNumber;

    //客户可选业务类型
    private String business;

}
