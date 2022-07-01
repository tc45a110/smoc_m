package com.smoc.cloud.scheduler.filters.service.configuration;

import com.smoc.cloud.scheduler.filters.service.service.FiltersService;
import com.smoc.cloud.scheduler.filters.service.utils.DFA.FilterInitialize;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 系统完成启动后，初始化过滤参数，主要是敏感词类参数
 */
@Slf4j
@Component
public class FilterDataInitListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private FiltersService filtersService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        FilterInitialize.sensitiveWordsFilter.initializeSensitiveWords(filtersService.getSensitiveWords());
        FilterInitialize.checkWordsFilter.initializeCheckWords(filtersService.getCheckWords());
//        FilterInitialize.superWhiteWordsFilter.initializeSuperWhiteWords(filtersService.getSuperWhiteWords());
        FilterInitialize.infoTypeSensitiveMap = filtersService.getInfoTypeSensitiveWords();
        FilterInitialize.channelSensitiveMap = filtersService.getChannelSensitiveWords();
        FilterInitialize.accountSensitiveMap = filtersService.getAccountSensitiveWords();
        FilterInitialize.accountCheckMap = filtersService.getAccountCheckWords();
//        FilterInitialize.accountSuperWhiteMap = filtersService.getAccountSuperWhiteWords();
        FilterInitialize.accountFilterFixedTemplateMap = filtersService.getAccountFixedTemplates();
        FilterInitialize.accountSignTemplateMap = filtersService.getAccountSignTemplates();
        FilterInitialize.accountFilterVariableTemplateMap = filtersService.getAccountFilterVariableTemplates();
        FilterInitialize.accountNoFilterVariableTemplateMap = filtersService.getAccountNoFilterVariableTemplates();

    }
}

