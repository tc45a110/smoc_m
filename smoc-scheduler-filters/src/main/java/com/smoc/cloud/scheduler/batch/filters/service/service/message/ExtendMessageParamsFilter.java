package com.smoc.cloud.scheduler.service.filters.service.message;


import com.smoc.cloud.common.filters.utils.RedisConstant;
import com.smoc.cloud.scheduler.service.filters.service.FiltersService;
import com.smoc.cloud.scheduler.batch.filters.service.utils.FilterResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务账号短信内容扩展参数过滤
 */
@Slf4j
@Component
public class ExtendMessageParamsFilter {

    /**
     * 业务账号短信内容扩展参数过滤
     *
     * @param account 业务账号
     * @return
     */
    public Map<String, String> filter(FiltersService filtersService, String account, String message) {
        //Long start = System.currentTimeMillis();
        Map<String, String> result = new HashMap<>();
        result.put("result", "false");
        /**
         * 查询业务账号配置的MESSAGE_BLACK_级别配置参数
         */
        Object blackPatten = filtersService.get(RedisConstant.FILTERS_CONFIG_ACCOUNT_MESSAGE + "black:" + account);
        if (!StringUtils.isEmpty(blackPatten)) {
            //log.info("[内容_敏感词_扩展参数]{}:{}", model.getAccount(), new Gson().toJson(blackPatten));
            Boolean validator = filtersService.validator(blackPatten.toString(), message);
            if (validator) {
                result.put("result", "true");
                result.put("code", FilterResponseCode.MESSAGE_SENSITIVE_EXTEND_FILTER.getCode());
                result.put("message", FilterResponseCode.MESSAGE_SENSITIVE_EXTEND_FILTER.getMessage());
            }
        }

        /**
         * 查询业务账号配置的MESSAGE_WHITE_级别配置参数
         */
        if ("true".equals(result.get("result"))) {
            Object whitePatten = filtersService.get(RedisConstant.FILTERS_CONFIG_ACCOUNT_MESSAGE + "white:" + account);
            if (!StringUtils.isEmpty(whitePatten)) {
                //log.info("[内容_白词_扩展参数]{}:{}", model.getAccount(), new Gson().toJson(whitePatten));
                if (null != whitePatten && !StringUtils.isEmpty(whitePatten.toString())) {
                    Boolean validator = filtersService.validator(whitePatten.toString(), message);
                    if (validator) {
                        result.put("result", "false");
                    }
                }
            }
        }

        /**
         * 查询业务账号配置的MESSAGE_REGULAR_级别配置参数
         */
//        if ("true".equals(result.get("result"))) {
//            Object regularPatten = filtersService.get(RedisConstant.FILTERS_CONFIG_ACCOUNT_MESSAGE + "regular:" + account);
//            if (!StringUtils.isEmpty(regularPatten)) {
//                //log.info("[内容_正则_扩展参数]{}:{}", model.getAccount(), new Gson().toJson(regularPatten));
//                if (filtersService.validator(regularPatten.toString(), message)) {
//                    result.put("result", "false");
//                } else {
//                    result.put("result", "true");
//                    result.put("code", FilterResponseCode.MESSAGE_REGULAR_FILTER.getCode());
//                    result.put("message", FilterResponseCode.MESSAGE_REGULAR_FILTER.getMessage());
//                }
//            }
//        }

        return result;
    }
}
