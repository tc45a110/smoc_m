package com.smoc.cloud.api.response.flow;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SimGprsFlowUsedMonthlyBatch{

    private String  msisdn;

    private String imsi;

    private String iccid;

    /**
     * 指 定 月 份 的 数据 使 用 量
     * （KB）
     */
    private String dataAmount;

    /**
     * 分 APN 的数据使用情况
     * 列表；dataAmount 为 0 则 不返回
     */
    private List<SimGprsFlowUsedMonthlyBatchItem> apnDataAmountList;
}
