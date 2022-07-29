package com.smoc.cloud.common.smoc.saler.qo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerComplaintQo {

    private String accountId;

    private String month;

    private String complaint;

    private String[] monthArray;

    private String[] complaintArray;

    private String[] complaintArrayBefore;

    private String[] year;

    private String saler;

    private String startDate;
    private String endDate;
}
