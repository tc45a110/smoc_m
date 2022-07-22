package com.smoc.cloud.api.response.pool;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SimGroupUsedThisMonthShare {

    /**
     * 商品 Id
     */
    private String offeringId;

    /**
     * 商品名称
     */
    private String offeringName;

    /**
     * 总量，单位:kb
     */
    private String totalAmount;

    /**
     * 剩余量，单位:kb
     */
    private String remainAmount;

    /**
     * 使用量，单位:kb
     */
    private String useAmount;

}
