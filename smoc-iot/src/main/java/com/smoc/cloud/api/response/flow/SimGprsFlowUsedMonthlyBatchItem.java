package com.smoc.cloud.api.response.flow;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SimGprsFlowUsedMonthlyBatchItem {

    /**
     * Apn 名称
     */
    private String apnName;

    /**
     * Apn 使用量（KB）
     */
    private String apnDataAmount;
}
