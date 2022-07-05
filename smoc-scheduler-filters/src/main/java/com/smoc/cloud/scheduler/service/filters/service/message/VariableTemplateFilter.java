package com.smoc.cloud.scheduler.service.filters.service.message;

import com.smoc.cloud.scheduler.service.filters.service.FiltersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 内容过滤
 * 业务账号短信变量模版过滤
 */
@Slf4j
@Component
public class VariableTemplateFilter {

    /**
     * 业务账号短信变量模版过滤
     *
     * @param account 业务账号
     * @param message 消息内容
     * @return
     */
    public Map<String, String> filter(FiltersService filtersService, String account, String message) {
        //Long start = System.currentTimeMillis();
        Map<String, String> result = new HashMap<>();
        result.put("result", "false");
        

        return result;
    }
}
