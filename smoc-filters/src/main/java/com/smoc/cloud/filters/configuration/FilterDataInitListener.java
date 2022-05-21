package com.smoc.cloud.filters.configuration;

import com.smoc.cloud.filters.filter.message_filter.FilterInitialize;
import com.smoc.cloud.filters.service.FiltersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FilterDataInitListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private FiltersService filtersService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("[加载系统敏感词]");
        FilterInitialize.sensitiveWordsFilter.initializeSensitiveWords(filtersService.loadSensitiveWords());
    }
}

