package com.smoc.cloud.api.response.flow;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SimFlowUsedThisMonthTotalResponse {

    /**
     * 流量累积量值，单位：KB 返回" "时，表示卡未产生 用量或未订购套餐
     */
    private String dataAmount;

    /**
     * apn 使用量列表
     * 若 dataAmount 为" "或该 sim 下无 apn 使用流量时则 返回[]
     */
    private List<SimFlowUsedThisMonthTotalItemResponse> apnUseAmountList;


}
