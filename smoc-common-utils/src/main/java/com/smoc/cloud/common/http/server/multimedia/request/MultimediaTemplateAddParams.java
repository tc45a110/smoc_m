package com.smoc.cloud.common.http.server.multimedia.request;

import com.smoc.cloud.common.gateway.request.HttpServerSignModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 多媒体模板请求参数
 */
@Setter
@Getter
public class MultimediaTemplateAddParams extends HttpServerSignModel {

    //模板标题
    @NotNull(message = "模板标题不能为空！")
    @Length(max = 64, message = "模板标题最大长度为{max}！")
    private String templateTitle;

    private List<MultimediaTemplateModel> items;
}
