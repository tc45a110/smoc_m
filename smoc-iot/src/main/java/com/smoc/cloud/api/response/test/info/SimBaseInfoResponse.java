package com.smoc.cloud.api.response.test.info;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SimBaseInfoResponse {

    /**
     * 集成电路卡识别码
     */
    private String iccid;

    /**
     * 物联卡号码
     */
    private String msisdn;

    /**
     * 国际移动用户识别码
     */
    private String imsi;

    /**
     * 国际移动设备识别码
     */
    private String imei;

    /**
     * 首次激活日
     */
    private String dateActivated;

    /**
     * 开卡时间
     */
    private String dateOpen;

    /**
     * 物联网卡分配给客户时间
     */
    private String dateShipped;

    /**
     * 开卡费用，0表示不收费用
     */
    private String openCardFee;

    /**
     * 周期性功能费，0表示不收费用
     */
    private String cycleFunctionFee;

    /**
     * 计费类型  1表示单卡计费 2表示套餐计费
     */
    private String chargingType;

    /**
     * 卡状态
     */
    private String cardStatus;


}
