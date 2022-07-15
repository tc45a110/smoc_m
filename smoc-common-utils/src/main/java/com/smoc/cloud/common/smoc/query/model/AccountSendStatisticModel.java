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

    private int totalSuccessSubmitNum;

    private int totalMessageSuccessNum;

    private int totalMessageFailureNum;

    private int totalMessageNoReportNum;
}
