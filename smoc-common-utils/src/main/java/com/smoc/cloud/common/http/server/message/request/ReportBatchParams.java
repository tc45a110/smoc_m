package com.smoc.cloud.common.http.server.message.request;


import com.smoc.cloud.common.gateway.request.HttpServerSignModel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Setter
@Getter
public class ReportBatchParams extends HttpServerSignModel {

    //开始日期
    @NotNull(message = "开始日期不能为空！")
    @Pattern(regexp = "^(20[2-9][0-9]-[0-1][0-9]-[0-3][0-9])", message = "开始日期格式不符合规则！")
    private String startDate;

    //结束日期
    @NotNull(message = "结束日期不能为空！")
    @Pattern(regexp = "^(20[2-9][0-9]-[0-1][0-9]-[0-3][0-9])", message = "结束日期格式不符合规则！")
    private String endDate;
}
