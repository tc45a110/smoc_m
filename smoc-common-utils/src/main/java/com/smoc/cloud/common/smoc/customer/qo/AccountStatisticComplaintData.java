package com.smoc.cloud.common.smoc.customer.qo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountStatisticComplaintData {

    private String accountId;

    private String month;

    private String complaint;

    private String[] monthArray;

    private String[] complaintArray;

    //统计维度
    private String dimension;
    private String startDate;
    private String endDate;
}
