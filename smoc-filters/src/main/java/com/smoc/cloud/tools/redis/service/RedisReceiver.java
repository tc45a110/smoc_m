package com.smoc.cloud.tools.redis.service;

import com.smoc.cloud.common.filters.utils.RedisConstant;
import com.smoc.cloud.filters.service.FiltersService;
import com.smoc.cloud.filters.utils.DFA.FilterInitialize;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RedisReceiver {

    @Autowired
    private FiltersService filtersService;

    /**
     * 这里是收到通道的消息之后执行的方法
     *
     * @param message
     */
    public void receiveMessage(String message) {

        log.info("[收到发布的消息]:{}", message);
        message = message.replace("\"", "");
        try {

            if (RedisConstant.MESSAGE_SYSTEM_SENSITIVE.equals(message)) {
                FilterInitialize.sensitiveWordsFilter.initializeSensitiveWords(filtersService.getSensitiveWords());
            }

            if (RedisConstant.MESSAGE_SYSTEM_CHECK.equals(message)) {
                FilterInitialize.checkWordsFilter.initializeCheckWords(filtersService.getCheckWords());
            }

            if (RedisConstant.MESSAGE_SYSTEM_SUPER_WHITE.equals(message)) {
                FilterInitialize.superWhiteWordsFilter.initializeSuperWhiteWords(filtersService.getSuperWhiteWords());
            }

            if (RedisConstant.MESSAGE_TYPE_INFO_SENSITIVE.equals(message)) {
                FilterInitialize.infoTypeSensitiveMap = filtersService.getInfoTypeSensitiveWords();
            }

            if (RedisConstant.MESSAGE_ACCOUNT_SENSITIVE.equals(message)) {
                FilterInitialize.accountSensitiveMap = filtersService.getAccountSensitiveWords();
            }

            if (RedisConstant.MESSAGE_ACCOUNT_CHECK.equals(message)) {
                FilterInitialize.accountCheckMap = filtersService.getAccountCheckWords();
            }

            if (RedisConstant.MESSAGE_ACCOUNT_SUPER_WHITE.equals(message)) {
                FilterInitialize.accountSuperWhiteMap = filtersService.getAccountSuperWhiteWords();
            }
            if (RedisConstant.MESSAGE_TEMPLATE.equals(message)) {
                FilterInitialize.accountSignTemplateMap = filtersService.getAccountSignTemplates();
                FilterInitialize.accountFilterVariableTemplateMap = filtersService.getAccountFilterVariableTemplates();
                FilterInitialize.accountNoFilterVariableTemplateMap = filtersService.getAccountNoFilterVariableTemplates();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
