package com.smoc.cloud.api.remote.cmcc.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CmccBatchSimBaseInfoResponse {

    /**
     *查询状态，0-成功，非 0-失败
     */
    private String status;

    /**
     *错误信息。
     */
    private String message;

    /**
     *所查询的物联卡号码
     */
    private String msisdn;

    /**
     *集成电路卡识别码
     */
    private String iccid;

    /**
     *国际移动用户识别码
     */
    private String imsi;

}
