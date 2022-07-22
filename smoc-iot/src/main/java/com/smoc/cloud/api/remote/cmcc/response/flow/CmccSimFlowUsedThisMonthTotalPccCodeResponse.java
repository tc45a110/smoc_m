package com.smoc.cloud.api.remote.cmcc.response.flow;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CmccSimFlowUsedThisMonthTotalPccCodeResponse {

    /**
     * pccServiceCode 编码
     */
    private String pccCode;

    /**
     * 该 pccCode 数据累积量值
     * 单位：KB
     */
    private String pccCodeUseAmount;
}
