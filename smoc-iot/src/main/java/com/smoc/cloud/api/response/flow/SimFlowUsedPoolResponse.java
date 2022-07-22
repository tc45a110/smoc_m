package com.smoc.cloud.api.response.flow;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SimFlowUsedPoolResponse {

    /**
     * APN 名称
     */
    private String apnName;

    /**
     * APN用量：单位:byte
     */
    private String apnUseAmount;
}
