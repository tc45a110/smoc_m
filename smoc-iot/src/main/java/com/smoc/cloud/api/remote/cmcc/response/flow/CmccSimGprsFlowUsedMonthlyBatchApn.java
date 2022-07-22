package com.smoc.cloud.api.remote.cmcc.response.flow;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CmccSimGprsFlowUsedMonthlyBatchApn {

    /**
     * Apn 名称
     */
    private String apnName;

    /**
     * Apn 使用量（KB）
     */
    private String apnDataAmount;
}
