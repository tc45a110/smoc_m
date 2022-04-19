package com.smoc.cloud.common.http.server.multimedia.request;

import com.smoc.cloud.common.gateway.request.HttpServerSignModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 多媒体模板请求参数
 */
@Setter
@Getter
public class MultimediaTemplateAddParams extends HttpServerSignModel {

    //模板类型  1普通模板 2 变量模板
    @NotNull(message = "模板类型不能为空！")
    @Pattern(regexp = "^(1|2){1}", message = "模板类型不符合规则！只支持普通模板 1、变量模板 2")
    private String templateType;

    //模板标题
    @NotNull(message = "模板标题不能为空！")
    @Length(max = 64, message = "模板标题最大长度为{max}！")
    private String templateTitle;

    private List<MultimediaTemplateModel> items;
}
