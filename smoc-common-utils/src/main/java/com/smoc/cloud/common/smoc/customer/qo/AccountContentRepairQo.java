package com.smoc.cloud.common.smoc.customer.qo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class AccountContentRepairQo {

    private String accountId;

    private String enterpriseName;

    private String accountName;

    private String businessType;

    private String carrier;

    private String infoType;

    private int rowspan;

    private List<String> carrierList = new ArrayList<>();

}
