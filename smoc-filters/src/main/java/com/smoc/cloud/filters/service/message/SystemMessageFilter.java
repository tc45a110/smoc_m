package com.smoc.cloud.filters.service.message;

import com.smoc.cloud.filters.service.FiltersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

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
        if (StringUtils.isEmpty(message)) {
            result.put("result", "false");
            return result;
        }

        //是否黑词过滤
        if (null != isBlackWordsFilter && !StringUtils.isEmpty(isBlackWordsFilter.toString()) && "1".equals(isBlackWordsFilter.toString())) {
            //log.info("[短信内容黑词过滤]：{}", isBlackWordsFilter);
        }

        //是否审核词过滤
        if (null != isCheckWordsFilter && !StringUtils.isEmpty(isCheckWordsFilter.toString()) && "1".equals(isCheckWordsFilter.toString())) {
            //log.info("[短信内容审核词过滤]：{}", isCheckWordsFilter);
        }

        result.put("result", "false");
        return result;

    }


}
