package com.smoc.cloud.http.entity;

import com.smoc.cloud.common.http.server.message.response.ReportResponseParams;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PushReportParams {


    private String orderNo;

    private List<ReportResponseParams> data;

    private String timestamp;
}
