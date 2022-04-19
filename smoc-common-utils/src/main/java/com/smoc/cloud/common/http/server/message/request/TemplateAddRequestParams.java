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

    //模板类型  1普通模板 2 变量模板
    @NotNull(message = "模板类型不能为空！")
    @Pattern(regexp = "^(1|2){1}", message = "模板类型不符合规则！只支持普通模板 1、变量模板 2")
    private String templateType;

    @NotNull(message = "模板内容不能为空！")
    @Length(max = 3600, message = "模板内容最大长度为{max}！")
    private String content;


}
