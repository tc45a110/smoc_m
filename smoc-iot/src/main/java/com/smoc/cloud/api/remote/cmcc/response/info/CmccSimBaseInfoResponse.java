package com.smoc.cloud.api.remote.cmcc.response.info;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CmccSimBaseInfoResponse {

    private String msisdn;

    private String iccid;

    private String imsi;

    private String activeDate;

    private String openDate;

    private String remark;
}
