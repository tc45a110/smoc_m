package com.smoc.cloud.common.http.server.message.request;


import com.smoc.cloud.common.gateway.request.HttpServerSignModel;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReportStatusRequestParams extends HttpServerSignModel {

    private String msgId;

    private String mobile;
}
