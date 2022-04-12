package com.smoc.cloud.common.smoc.customer.qo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountStatisticSendData {

    private String accountId;

    private int index;

    private String data1;

    private String data2;

    private Object[] month;

    private Object[] sendNumber;

    //统计维度
    private String dimension;
    private String startDate;
    private String endDate;
}
