package com.smoc.cloud.filters.service.account;

import com.smoc.cloud.filters.service.FiltersService;
import com.smoc.cloud.filters.utils.FilterResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务账号 日发送量限制
 */
@Slf4j
@Component
public class CarrierDailyLimiterFilter {

    /**
     * @param filtersService
     * @param dailyLimitStyle 运营商日限量方式
     * @param dailyLimit      日限量
     * @param numbers         拆分后短信条数
     * @return
     */
    public Map<String, String> filter(FiltersService filtersService, Object dailyLimitStyle, Object dailyLimit, String account, String carrier, Integer numbers) {
        Map<String, String> result = new HashMap<>();
        if (null == dailyLimit || StringUtils.isEmpty(dailyLimit.toString()) || StringUtils.isEmpty(carrier)) {
            result.put("result", "false");
            return result;
        }

        //log.info("[账号-运营商-日限量-方式]:{}", dailyLimitStyle.toString());
        //log.info("[账号-运营商-日限量]:{}", dailyLimit.toString());

        //无限量
        if (!(null == dailyLimitStyle || StringUtils.isEmpty(dailyLimitStyle.toString())) && "0".equals(dailyLimitStyle)) {
            result.put("result", "false");
            return result;
        }
        //限量、计量方式
        Integer times = 1;
        if (!(null == dailyLimitStyle || StringUtils.isEmpty(dailyLimitStyle.toString())) && ("1".equals(dailyLimitStyle) || "3".equals(dailyLimitStyle))) {
            times = numbers == null ? 1 : numbers;
        }

//        //redis 增加次数
//        Long number = filtersService.incrementAccountDailyLimit(account, carrier,times);
        //触发日限量
        if (filtersService.accountDailyLimit(account, carrier, new Long(dailyLimit.toString()), times)) {
            result.put("result", "true");
            result.put("code", FilterResponseCode.LIMIT_DAILY_CARRIER.getCode());
            result.put("message", "达到" + carrier + "的日限量限制！");
            return result;
        }

        result.put("result", "false");
        return result;
    }
}
