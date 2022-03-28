package com.smoc.cloud.reconciliation.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
/**
 * 对账，业务账号对象
 */
@Setter
@Getter
public class ReconciliationAccountModel {

    //业务账号
    private String account;
    //账号类型
    private String accountType;
    //付费方式
    private String payType;
    //运营商
    private List<ReconciliationCarrierModel> carriers;
}
