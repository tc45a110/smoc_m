package com.smoc.cloud.common.http.server.message.request;

import com.smoc.cloud.common.gateway.request.HttpServerSignModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Setter
@Getter
public class TemplateAddRequestParams extends HttpServerSignModel {

    @NotNull(message = "模板内容不能为空！")
    @Length(max = 3600, message = "模板内容最大长度为{max}！")
    private String content;


}
