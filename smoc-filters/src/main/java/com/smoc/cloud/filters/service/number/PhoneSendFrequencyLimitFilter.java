package com.smoc.cloud.filters.service.number;

import com.smoc.cloud.common.filters.utils.RedisConstant;
import com.smoc.cloud.filters.service.FiltersService;
import com.smoc.cloud.filters.utils.FilterResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

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
        if (StringUtils.isEmpty(phoneFrequencyLimit)) {
            result.put("result", "false");
            return result;
        }
//        log.info("[号码_发送频率限制]：{}", phoneFrequencyLimit);

        //无限制
        if ("0".equals(phoneFrequencyLimit.toString())) {
            result.put("result", "false");
            return result;
        }

        //免频次限制手机号
        Object whitePatten = filtersService.get(RedisConstant.FILTERS_CONFIG_ACCOUNT_NUMBER + "white:" + account);
        if (!StringUtils.isEmpty(whitePatten)) {
            //log.info("[号码_白名单_扩展参数]{}:{}", model.getAccount(), new Gson().toJson(whitePatten));
            Boolean validator = filtersService.validator(whitePatten.toString(), phone);
            if (validator) {
                result.put("result", "false");
                return result;
            }
        }

        //限流默认数量
        int maxBurst;
        //按分钟限制
        String[] minutes = phoneFrequencyLimit.toString().split("M");
        if (null != minutes && minutes.length ==2) {
            //时间，转换成秒
            int seconds = new Integer(minutes[0]) * 60;
            //次数
            String[] times = minutes[1].split("B");
            //tokens表示每seconds生成的次数
            int tokens = new Integer(times[0]);
            //最大爆发数，可以连续发送数
            maxBurst = new Integer(times[1])-1;
            if (!filtersService.phoneFrequencyLimiterByAccount(account, phone, maxBurst, tokens, seconds, 1)) {
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
            int seconds = new Integer(hours[0]) * 60 * 60;
            //次数
            String[] times = hours[1].split("B");
            //tokens表示每seconds生成的次数
            int tokens = new Integer(times[0]);
            //最大爆发数，可以连续发送数
            maxBurst = new Integer(times[1])-1;
            if (!filtersService.phoneFrequencyLimiterByAccount(account, phone, maxBurst, tokens, seconds, 1)) {
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
        if (null != days && days.length == 2) {
            int seconds = new Integer(days[0]) * 60 * 60 * 24;
            //次数
            String[] times = days[1].split("B");
            //tokens表示每seconds生成的次数
            int tokens = new Integer(times[0]);
            //最大爆发数，可以连续发送数
            maxBurst = new Integer(times[1])-1;
            if (!filtersService.phoneFrequencyLimiterByAccount(account, phone, maxBurst, tokens, seconds, 1)) {
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
