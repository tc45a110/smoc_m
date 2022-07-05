package com.smoc.cloud.scheduler.service.filters.service.message;

import com.smoc.cloud.scheduler.service.filters.utils.DFA.DfaSensitiveWordsFilter;
import com.smoc.cloud.scheduler.service.filters.utils.DFA.FilterInitialize;
import com.smoc.cloud.scheduler.service.filters.utils.FilterResponseCode;
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
public class ChannelMessageFilter {

    /**
     * @param channelId      通道ID
     * @param message        短信内容
     * @return
     */
    public Map<String, String> filter(String channelId, String message) {

        Map<String, String> result = new HashMap<>();
        if (StringUtils.isEmpty(channelId)) {
            result.put("result", "false");
            return result;
        }

        /**
         * 通道敏感词过滤
         */
        log.info("[通道过滤]：{}", channelId);
        DfaSensitiveWordsFilter dfaSensitiveWordsFilter = new DfaSensitiveWordsFilter(FilterInitialize.channelSensitiveMap.get(channelId));
        Set<String> sensitiveWords = dfaSensitiveWordsFilter.getSensitiveWords(message, 1);
        if (null != sensitiveWords && sensitiveWords.size() > 0) {
            result.put("result", "true");
            result.put("code", FilterResponseCode.MESSAGE_CHANNEL_SENSITIVE_FILTER.getCode());
            result.put("message", "内容中包含通道敏感词" + sensitiveWords);
            return result;
        }
        result.put("result", "false");
        return result;

    }


}
