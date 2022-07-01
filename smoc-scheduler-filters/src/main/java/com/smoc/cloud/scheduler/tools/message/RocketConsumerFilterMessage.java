package com.smoc.cloud.scheduler.tools.message;

import com.smoc.cloud.common.filters.utils.RedisConstant;
import com.smoc.cloud.scheduler.filters.service.service.FiltersService;
import com.smoc.cloud.scheduler.filters.service.utils.DFA.FilterInitialize;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Rocket Message
 */
@Slf4j
//@Component
//@RocketMQMessageListener(topic = "smoc-message-filter", consumerGroup = "group-filter")
public class RocketConsumerFilterMessage implements RocketMQListener<String> {

    @Autowired
    private FiltersService filtersService;

    @Override
    public void onMessage(String message) {

        log.info("收到Rocket消息:{}", message);
        try {

            //系统敏感词
            if (RedisConstant.MESSAGE_SYSTEM_SENSITIVE.equals(message)) {
                FilterInitialize.sensitiveWordsFilter.initializeSensitiveWords(filtersService.getSensitiveWords());
            }

            //系统审核词
            if (RedisConstant.MESSAGE_SYSTEM_CHECK.equals(message)) {
                FilterInitialize.checkWordsFilter.initializeCheckWords(filtersService.getCheckWords());
            }

            //行业敏感词
            if (RedisConstant.MESSAGE_TYPE_INFO_SENSITIVE.equals(message)) {
                FilterInitialize.infoTypeSensitiveMap = filtersService.getInfoTypeSensitiveWords();
            }

            //通道敏感词
            if (RedisConstant.MESSAGE_CHANNEL_SENSITIVE.equals(message)) {
                FilterInitialize.channelSensitiveMap = filtersService.getChannelSensitiveWords();
                return;
            }

            //业务账号敏感词
            if (RedisConstant.MESSAGE_ACCOUNT_SENSITIVE.equals(message)) {
                FilterInitialize.accountSensitiveMap = filtersService.getAccountSensitiveWords();
            }

            //业务账号审核词
            if (RedisConstant.MESSAGE_ACCOUNT_CHECK.equals(message)) {
                FilterInitialize.accountCheckMap = filtersService.getAccountCheckWords();
            }

            //账号模版
            if (RedisConstant.MESSAGE_TEMPLATE.equals(message)) {
                FilterInitialize.accountFilterFixedTemplateMap = filtersService.getAccountFixedTemplates();
                FilterInitialize.accountSignTemplateMap = filtersService.getAccountSignTemplates();
                FilterInitialize.accountFilterVariableTemplateMap = filtersService.getAccountFilterVariableTemplates();
                FilterInitialize.accountNoFilterVariableTemplateMap = filtersService.getAccountNoFilterVariableTemplates();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
