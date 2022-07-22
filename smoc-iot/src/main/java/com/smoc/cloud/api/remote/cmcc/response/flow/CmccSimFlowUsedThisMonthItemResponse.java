package com.smoc.cloud.api.remote.cmcc.response.flow;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CmccSimFlowUsedThisMonthItemResponse {

    /**
     * 资费 Id
     */
    private String offeringId;
    /**
     * 资费名称
     */
    private String offeringName;
    /**
     * APN 名称，无值则返回为" "
     */
    private String apnName;
    /**
     * 总量，单位：KB
     */
    private String totalAmount;
    /**
     * 使用量，单位：KB
     */
    private String useAmount;
    /**
     * 剩余量，单位：KB
     */
    private String remainAmount;
    /**
     * pccServiceCode 编码无值则返回为" "
     */
    private String pccCode;
}
