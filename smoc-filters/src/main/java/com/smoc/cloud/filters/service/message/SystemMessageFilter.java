package com.smoc.cloud.filters.service.message;

import com.google.gson.Gson;
import com.smoc.cloud.filters.service.FiltersService;
import com.smoc.cloud.filters.utils.DFA.FilterInitialize;
import com.smoc.cloud.filters.utils.DFA.WordsSensitiveFilter;
import com.smoc.cloud.filters.utils.FilterResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 系统级短信内容 黑词、审核次、各种白词洗白
 */
@Slf4j
@Component
public class SystemMessageFilter {

    /**
     * @param filtersService     业务服务
     * @param isBlackWordsFilter 是否过滤黑词
     * @param isCheckWordsFilter 是否过滤审核词
     * @param message            短信内容
     * @return
     */
    public Map<String, String> filter(FiltersService filtersService, Object isBlackWordsFilter, Object isCheckWordsFilter, String message) {

        Map<String, String> result = new HashMap<>();

        long start = System.currentTimeMillis();

        /**
         * 先过滤超级白词,超级白词比较少
         */
        Boolean isExistSuperWhiteWords = FilterInitialize.superWhiteWordsFilter.isContain(message, 1);
        if (isExistSuperWhiteWords) {
            result.put("result", "false");
            return result;
        }

        //是否敏感词过滤
        if (null != isBlackWordsFilter && !StringUtils.isEmpty(isBlackWordsFilter) && "1".equals(isBlackWordsFilter.toString())) {

            /**
             * 系统敏感词过滤
             */
            //long start = System.currentTimeMillis();
            WordsSensitiveFilter sensitiveWordsFilter = FilterInitialize.sensitiveWordsFilter;
            Set<String> sensitiveWords = sensitiveWordsFilter.getSensitiveWords(message, 1);
            if (null != sensitiveWords && sensitiveWords.size() > 0) {
                //log.info("[敏感词过滤]：{}", sensitiveWords);
                List<Object> whiteWords = filtersService.getWhiteWordsBySensitive(sensitiveWords);
                //log.info("[洗黑白词]：{}", new Gson().toJson(whiteWords));
                for (Object obj : whiteWords) {
                    if (ObjectUtils.isEmpty(obj)) {
                        result.put("result", "true");
                        result.put("code", FilterResponseCode.MESSAGE_SENSITIVE_FILTER.getCode());
                        result.put("message", "内容中包含敏感词" + sensitiveWords.toString());
                        return result;
                    }
                    Pattern pattern = Pattern.compile(obj.toString());
                    Matcher matcher = pattern.matcher(message);
                    if (matcher.find()) {
                        continue;
                    } else {
                        result.put("result", "true");
                        result.put("code", FilterResponseCode.MESSAGE_SENSITIVE_FILTER.getCode());
                        result.put("message", "内容中包含敏感词" + sensitiveWords.toString());
                        return result;
                    }
                }

            }
        }


        //是否审核词过滤
        if (null != isCheckWordsFilter && !StringUtils.isEmpty(isCheckWordsFilter) && "1".equals(isCheckWordsFilter.toString())) {
            /**
             * 系统审核词过滤
             */
            Set<String> checkWords = FilterInitialize.checkWordsFilter.getCheckWords(message, 1);
            //包含审核词
            if (null != checkWords && checkWords.size() > 0) {
                log.info("[审核词过滤]：{}", checkWords);
                List<Object> whiteWords = filtersService.getWhiteWordsByCheck(checkWords);
                log.info("[免审白词]：{}", new Gson().toJson(whiteWords));
                for (Object obj : whiteWords) {
                    if (ObjectUtils.isEmpty(obj)) {
                        result.put("result", "true");
                        result.put("code", FilterResponseCode.MESSAGE_CHECK_FILTER.getCode());
                        result.put("message", "内容中包含审核词" + checkWords.toString());
                        return result;
                    }
                    Pattern pattern = Pattern.compile(obj.toString());
                    Matcher matcher = pattern.matcher(message);
                    if (matcher.find()) {
                        continue;
                    } else {
                        result.put("result", "true");
                        result.put("code", FilterResponseCode.MESSAGE_CHECK_FILTER.getCode());
                        result.put("message", "内容中包含审核词" + checkWords.toString());
                        return result;
                    }
                }
            }

            long end = System.currentTimeMillis();
            log.info("[洗白敏感词]：耗时{}毫秒", end - start);

        }


        result.put("result", "false");
        return result;

    }


}
