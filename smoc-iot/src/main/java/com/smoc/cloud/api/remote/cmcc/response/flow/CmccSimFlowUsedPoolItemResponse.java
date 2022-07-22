package com.smoc.cloud.api.remote.cmcc.response.flow;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CmccSimFlowUsedPoolItemResponse {

    /**
     * APN 名称
     */
    private String apnName;

    /**
     * APN 用量：单位:byte
     */
    private String apnUseAmount;
}
