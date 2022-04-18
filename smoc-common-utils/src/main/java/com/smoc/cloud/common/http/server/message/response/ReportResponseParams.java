package com.smoc.cloud.common.http.server.message.response;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReportResponseParams {

    private String account;

    private String mobile;

    private String msgId;

    private String reportTime;

    private String status;
}
