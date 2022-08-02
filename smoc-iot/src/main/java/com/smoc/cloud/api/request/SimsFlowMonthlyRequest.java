package com.smoc.cloud.api.request;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SimsFlowMonthlyRequest extends BaseRequest{

    private List<String> msisdns;

    private List<String> iccids;

    private List<String> imsis;

    /**
     * 所查询月份
     */
    private String queryDate;
}
