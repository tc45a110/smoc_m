package com.smoc.cloud.http.filters.service.message;

import com.smoc.cloud.http.filters.service.FiltersService;
import com.smoc.cloud.http.filters.utils.DFA.DfaSensitiveWordsFilter;
import com.smoc.cloud.http.filters.utils.DFA.FilterInitialize;
import com.smoc.cloud.http.filters.utils.FilterResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 行业敏感词过滤
 */
@Slf4j
@Component
public class InfoTypeMessageFilter {

    /**
     * @param filtersService               业务服务
     * @param infoTypeSensitiveWordsFilter 行业
     * @param message                      短信内容
     * @return
     */
    public Map<String, String> filter(FiltersService filtersService, Object infoTypeSensitiveWordsFilter, String message) {

        Map<String, String> result = new HashMap<>();
        if (StringUtils.isEmpty(infoTypeSensitiveWordsFilter)) {
            result.put("result", "false");
            return result;
        }

        /**
         * 行业敏感词过滤
         */
        //long start = System.currentTimeMillis();
        if (null != infoTypeSensitiveWordsFilter && !StringUtils.isEmpty(infoTypeSensitiveWordsFilter)) {
            //log.info("[行业过滤_行业]：{}", infoTypeSensitiveWordsFilter);
            DfaSensitiveWordsFilter dfaSensitiveWordsFilter = new DfaSensitiveWordsFilter(FilterInitialize.infoTypeSensitiveMap.get(infoTypeSensitiveWordsFilter));
            Set<String> sensitiveWords = dfaSensitiveWordsFilter.getSensitiveWords(message, 1);
            //log.info("[行业敏感词过滤]：{}", sensitiveWords);
            if (null != sensitiveWords && sensitiveWords.size() > 0) {
                result.put("result", "true");
                result.put("code", FilterResponseCode.MESSAGE_INFO_TYPE_SENSITIVE_FILTER.getCode());
                result.put("message", "内容中包含行业敏感词" + sensitiveWords.toString());
                return result;
            }
        }
        result.put("result", "false");
        return result;

    }


}
