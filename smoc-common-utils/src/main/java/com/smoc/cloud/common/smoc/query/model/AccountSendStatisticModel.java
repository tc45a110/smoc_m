package com.smoc.cloud.common.smoc.query.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
public class AccountSendStatisticModel {

    private String messageDate;

    private String businessAccount;

    private String accountName;

    private String enterpriseName;

    private String startDate;

    private String endDate;

    private List<AccountSendStatisticItemsModel> items = new ArrayList<>();

    private String totalSuccessSubmitNum;

    private String totalMessageSuccessNum;

    private String totalMessageFailureNum;

    private String totalMessageNoReportNum;
}
