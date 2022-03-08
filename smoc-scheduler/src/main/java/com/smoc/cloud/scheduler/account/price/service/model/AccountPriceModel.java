package com.smoc.cloud.scheduler.account.price.service.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class AccountPriceModel {

    private String id;

    //业务账号
    private String accountId;

    //运营商
    private String carrier;

    //运营商价格
    private BigDecimal carrierPrice;

    //价格日期
    private String priceData;

    //上次批处理日期
    private String batchDate;

    //距离上次批处理 相隔天数
    private Integer days;

    //创建日期
    private String createTime;
}
