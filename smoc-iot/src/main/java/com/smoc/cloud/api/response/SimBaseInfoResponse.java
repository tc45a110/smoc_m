package com.smoc.cloud.api.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SimBaseInfoResponse {

    /**
     * 物联卡号码
     */
    private String msisdn;

    /**
     * 集成电路卡识别码
     */
    private String iccid;

    /**
     * 国际移动用户识别码
     */
    private String imsi;

    /**
     * 激活日期（首次）
     */
    private String activeDate;

    /**
     * 开卡时间
     */
    private String openDate;

    /**
     * SIM 卡备注信息，即客户在平台标
     * 注的卡备注信息
     */
    private String remark;
}
