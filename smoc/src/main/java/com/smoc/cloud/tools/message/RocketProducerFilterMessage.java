package com.smoc.cloud.tools.message;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Rocket Message
 */
@Slf4j
@Component
public class RocketProducerFilterMessage {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void sendRocketMessage(String message){
        rocketMQTemplate.convertAndSend("smoc-message-filter",message);
    }

}
