package com.smoc.cloud.common.smoc.reconciliation.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 对账，企业对象
 */
@Setter
@Getter
public class ReconciliationEnterpriseModel {

    //账期
    private String accountingPeriod;
    //企业id
    private String enterpriseId;
    //企业名称
    private String enterpriseName;
    //账单状态
    private String accountingStatus;

    private String quantity;

    private String sum;
    //业务账号
    private List<ReconciliationAccountModel> accounts;

}
