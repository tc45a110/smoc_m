package com.smoc.cloud.common.smoc.reconciliation.model;

import com.smoc.cloud.common.smoc.reconciliation.ReconciliationCarrierItemsValidator;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
public class ReconciliationChannelCarrierModel {

    //账单周期
    private String channelPeriod;

    //供应商
    private String channelProvder;

    private String channelPeriodStatus;

    private int rowspan;

    private List<ReconciliationCarrierItemsValidator> carrierList = new ArrayList<>();

}
