package com.smoc.cloud.scheduler.channel.price.processor;

import com.google.gson.Gson;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelPriceValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;


/**
 * 通道价格历史 批处理
 */
@Slf4j
@Component
public class ChannelPriceHistoryProcessor implements ItemProcessor<ChannelPriceValidator, ChannelPriceValidator> {
    @Override
    public ChannelPriceValidator process(ChannelPriceValidator channelPriceValidator) throws Exception {
        log.info("[channelPriceValidator]:{}",new Gson().toJson(channelPriceValidator));
        return channelPriceValidator;
    }
}
