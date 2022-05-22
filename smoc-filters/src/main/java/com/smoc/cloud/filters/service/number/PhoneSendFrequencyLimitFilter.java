package com.smoc.cloud.filters.service.number;

import com.smoc.cloud.filters.service.FiltersService;
import com.smoc.cloud.filters.utils.FilterResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 单手机号发送频率限制
 */
@Slf4j
@Component
public class PhoneSendFrequencyLimitFilter {

    /**
     * @param filtersService      业务服务
     * @param phoneFrequencyLimit 日限量规则
     * @param account             业务账号
     * @param phone               手机号
     * @return
     */
    public Map<String, String> filter(FiltersService filtersService, Object phoneFrequencyLimit, String account, String phone) {

        Map<String, String> result = new HashMap<>();
        if (null == phoneFrequencyLimit || StringUtils.isEmpty(phoneFrequencyLimit.toString())) {
            result.put("result", "false");
            return result;
        }
        log.info("[号码_限量]：{}", phoneFrequencyLimit);

        //无限制
        if ("0".equals(phoneFrequencyLimit.toString())) {
            result.put("result", "false");
            return result;
        }

        //每次消耗的条数 默认1
        int times = 1;
        //限流默认数量
        int maxBurst = 1;

        //按分钟限制
        String[] minutes = phoneFrequencyLimit.toString().split("M");
        if (null != minutes && minutes.length == 2) {
            Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
            if (!pattern.matcher(minutes[0]).matches() || !pattern.matcher(minutes[1]).matches()) {
                result.put("result", "true");
                result.put("code", FilterResponseCode.NUMBER_FREQUENCY_LIMIT_PARAM.getCode());
                result.put("message", FilterResponseCode.NUMBER_FREQUENCY_LIMIT_PARAM.getMessage());
                return result;
            }
            int seconds = new Integer(minutes[0]) * 60;
            int tokens = new Integer(minutes[1]);
            if (!filtersService.phoneFrequencyLimiterByAccount(account, phone, maxBurst, tokens, seconds, times)) {
                result.put("result", "true");
                result.put("code", FilterResponseCode.NUMBER_FREQUENCY_LIMIT.getCode());
                result.put("message", FilterResponseCode.NUMBER_FREQUENCY_LIMIT.getMessage());
                return result;
            }
            result.put("result", "false");
            return result;
        }

        //按小时限制
        String[] hours = phoneFrequencyLimit.toString().split("H");
        if (null != hours && hours.length == 2) {
            Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
            if (!pattern.matcher(hours[0]).matches() || !pattern.matcher(hours[1]).matches()) {
                result.put("result", "true");
                result.put("code", FilterResponseCode.NUMBER_FREQUENCY_LIMIT_PARAM.getCode());
                result.put("message", FilterResponseCode.NUMBER_FREQUENCY_LIMIT_PARAM.getMessage());
                return result;
            }

            int seconds = new Integer(hours[0]) * 60 * 60;
            int tokens = new Integer(hours[1]);
            if (!filtersService.phoneFrequencyLimiterByAccount(account, phone, maxBurst, tokens, seconds, times)) {
                result.put("result", "true");
                result.put("code", FilterResponseCode.NUMBER_FREQUENCY_LIMIT.getCode());
                result.put("message", FilterResponseCode.NUMBER_FREQUENCY_LIMIT.getMessage());
                return result;
            }
            result.put("result", "false");
            return result;
        }

        //按天限制
        String[] days = phoneFrequencyLimit.toString().split("D");
        if (null != hours && hours.length == 2) {
            Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
            if (!pattern.matcher(days[0]).matches() || !pattern.matcher(days[1]).matches()) {
                result.put("result", "true");
                result.put("code", FilterResponseCode.NUMBER_FREQUENCY_LIMIT_PARAM.getCode());
                result.put("message", FilterResponseCode.NUMBER_FREQUENCY_LIMIT_PARAM.getMessage());
                return result;
            }

            int seconds = new Integer(days[0]) * 60 * 60 * 24;
            int tokens = new Integer(days[1]);
            if (!filtersService.phoneFrequencyLimiterByAccount(account, phone, maxBurst, tokens, seconds, times)) {
                result.put("result", "true");
                result.put("code", FilterResponseCode.NUMBER_FREQUENCY_LIMIT.getCode());
                result.put("message", FilterResponseCode.NUMBER_FREQUENCY_LIMIT.getMessage());
                return result;
            }
            result.put("result", "false");
            return result;
        }

        result.put("result", "true");
        result.put("code", FilterResponseCode.NUMBER_NO_CONFIG_FREQUENCY_LIMIT.getCode());
        result.put("message", FilterResponseCode.NUMBER_NO_CONFIG_FREQUENCY_LIMIT.getMessage());
        return result;

    }
}
