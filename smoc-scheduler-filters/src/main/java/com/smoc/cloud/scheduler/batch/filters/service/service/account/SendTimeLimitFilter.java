package com.smoc.cloud.scheduler.service.filters.service.account;

import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.scheduler.batch.filters.service.utils.FilterResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 业务账号 发送时段限制
 */
@Slf4j
@Component
public class SendTimeLimitFilter {

    /**
     * 发送时段限制
     *
     * @param sendTimeLimit 限制时间段
     * @return
     */
    public Map<String, String> filter(Object sendTimeLimit) {
        Map<String, String> result = new HashMap<>();
        if (StringUtils.isEmpty(sendTimeLimit)) {
            result.put("result", "false");
            return result;
        }

        String currentTime = DateTimeUtils.getDateFormat(new Date(), "HHmm");
        //log.info("[发送时间限制]：{}", sendTimeLimit.toString());
        String[] times = sendTimeLimit.toString().split("-");
        if (times.length == 2) {
            if ((new Integer(times[0]) <= new Integer(currentTime) && new Integer(currentTime) <= new Integer(times[1]))) {
                result.put("result", "false");
                return result;
            }
            result.put("result", "true");
            result.put("code", FilterResponseCode.LIMIT_TIME.getCode());
            result.put("message", FilterResponseCode.LIMIT_TIME.getMessage());
            return result;
        }

        result.put("result", "true");
        result.put("code", FilterResponseCode.LIMIT_TIME_CONFIG_ERROR.getCode());
        result.put("message", FilterResponseCode.LIMIT_TIME_CONFIG_ERROR.getMessage());
        return result;
    }

}
