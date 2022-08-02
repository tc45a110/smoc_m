package com.smoc.cloud.api.response.flow;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SimFlowUsedThisMonthResponse {

    private String iccid;

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
}
