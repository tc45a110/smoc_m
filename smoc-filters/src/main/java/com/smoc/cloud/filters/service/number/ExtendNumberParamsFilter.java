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
 * 业务账号手机号扩展参数过滤
 */
@Slf4j
@Component
public class ExtendNumberParamsFilter {

    /**
     * 业务账号手机号扩展参数过滤
     *
     * @param account 业务账号
     * @param phone   手机号
     * @return
     */
    public Map<String, String> filter(FiltersService filtersService, String account, String phone) {

        //Long start = System.currentTimeMillis();
        Map<String, String> result = new HashMap<>();
        result.put("result", "false");

        /**
         * 查询业务账号配置的NUMBER_BLACK_级别配置参数
         */
        Object blackPatten = filtersService.get(RedisConstant.FILTERS_CONFIG_ACCOUNT_NUMBER + "black:" + account);
        if (!StringUtils.isEmpty(blackPatten)) {
            //log.info("[号码_黑名单_扩展参数]{}:{}", model.getAccount(), blackPatten.toString());
            Boolean validator = filtersService.validator(blackPatten.toString(), phone);
            if (validator) {
                result.put("result", "true");
                result.put("code", FilterResponseCode.NUMBER_BLACK_FILTER.getCode());
                result.put("message", FilterResponseCode.NUMBER_BLACK_FILTER.getMessage());
            }
        }

        /**
         * 查询业务账号配置的NUMBER_WHITE_级别配置参数 超级洗白
         */
        if ("true".equals(result.get("result"))) {
            Object whitePatten = filtersService.get(RedisConstant.FILTERS_CONFIG_ACCOUNT_NUMBER + "white:" + account);
            if (!StringUtils.isEmpty(whitePatten)) {
                //log.info("[号码_白名单_扩展参数]{}:{}", model.getAccount(), new Gson().toJson(whitePatten));
                Boolean validator = filtersService.validator(whitePatten.toString(), phone);
                if (validator) {
                    result.put("result", "false");
                }
            }

        }

        /**
         * 查询业务账号配置的NUMBER_REGULAR_级别配置参数  满足则通过
         */
        if ("false".equals(result.get("result"))) {
            Object regularPatten = filtersService.get(RedisConstant.FILTERS_CONFIG_ACCOUNT_NUMBER + "regular:" + account);
            if (!StringUtils.isEmpty(regularPatten)) {
                //log.info("[号码_正则_扩展参数]{}:{}", model.getAccount(), new Gson().toJson(regularPatten));
                if (filtersService.validator(regularPatten.toString(), phone)) {
                    result.put("result", "false");
                } else {
                    result.put("result", "true");
                    result.put("code", FilterResponseCode.NUMBER_REGULAR_FILTER.getCode());
                    result.put("message", FilterResponseCode.NUMBER_REGULAR_FILTER.getMessage());
                }
            }
        }

        return result;
    }
}
