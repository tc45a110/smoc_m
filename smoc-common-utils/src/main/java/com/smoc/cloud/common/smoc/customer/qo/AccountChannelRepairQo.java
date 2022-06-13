package com.smoc.cloud.common.smoc.customer.qo;

import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelRepairRuleValidator;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class AccountChannelRepairQo {

    private String accountId;

    private String enterpriseName;

    private String accountName;

    private String businessType;

    private String carrier;

    private String infoType;

    private String rowspan;

    private List<ConfigChannelRepairRuleValidator> repairList = new ArrayList<>();

}
