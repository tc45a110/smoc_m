package com.smoc.cloud.common.iot.request;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Setter
@Getter
public class SimsFlowMonthlyRequest extends BaseRequest {

    private String iccid;

    private String queryMonth;

    private String currentPage;
}
