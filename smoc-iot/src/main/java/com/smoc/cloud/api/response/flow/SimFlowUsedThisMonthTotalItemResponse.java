package com.smoc.cloud.api.response.flow;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SimFlowUsedThisMonthTotalItemResponse {

    /**
     *APN 名称
     */
    private String apnName;

    /**
     * APN 数据累计使用量，单
     * 位：KB
     */
    private String apnUseAmount;

    /**
     *pcc 使用量列表
     * 若该 apn 下无 pccCode 使 用量则返回[]
     */
    private List<SimFlowUsedThisMonthTotalPccCodeResponse> pccCodeUseAmountList;
}
