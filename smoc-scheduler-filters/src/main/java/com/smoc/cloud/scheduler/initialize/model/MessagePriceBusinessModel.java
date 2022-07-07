package com.smoc.cloud.scheduler.initialize.model;

import com.smoc.cloud.scheduler.initialize.entity.AccountMessagePrice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 业务账号运营商价格 业务模型
 */
public class MessagePriceBusinessModel {

    //移动
    private AccountMessagePrice cmccAccountMessagePrice;
    //电信
    private AccountMessagePrice telcAccountMessagePrice;
    //联通
    private AccountMessagePrice unicAccountMessagePrice;
    //国际
    private Map<String, AccountMessagePrice> intlAccountMessagePrices = new HashMap<>();

    /**
     * 根据运营商、区域编码获取运营商短信单价
     *
     * @param carrier
     * @param areaCode
     * @return
     */
    public BigDecimal getMessagePrice(String carrier, String areaCode) {

        if ("CMCC".equals(carrier)) {
            if(null == cmccAccountMessagePrice){
                return null;
            }
            return cmccAccountMessagePrice.getCarrierPrice();
        }
        if ("TELC".equals(carrier)) {
            if(null == telcAccountMessagePrice){
                return null;
            }
            telcAccountMessagePrice.getCarrierPrice();
        }
        if ("UNIC".equals(carrier)) {
            if(null == unicAccountMessagePrice){
                return null;
            }
            unicAccountMessagePrice.getCarrierPrice();
        }
        if ("INTL".equals(carrier)) {
            AccountMessagePrice accountMessagePrice = intlAccountMessagePrices.get(areaCode);
            if (null == accountMessagePrice) {
                return null;
            }
            return accountMessagePrice.getCarrierPrice();
        }

        return null;
    }


    public void add(AccountMessagePrice accountMessagePrice) {

        //国内运营商
        if ("1".equals(accountMessagePrice.getCarrierType())) {
            if ("CMCC".equals(accountMessagePrice.getCarrier())) {
                cmccAccountMessagePrice = accountMessagePrice;
            }
            if ("TELC".equals(accountMessagePrice.getCarrier())) {
                telcAccountMessagePrice = accountMessagePrice;
            }
            if ("UNIC".equals(accountMessagePrice.getCarrier())) {
                unicAccountMessagePrice = accountMessagePrice;
            }
        }

        //国际运营商
        if ("2".equals(accountMessagePrice.getCarrierType())) {
            intlAccountMessagePrices.put(accountMessagePrice.getCarrier(), accountMessagePrice);
        }
    }

}
