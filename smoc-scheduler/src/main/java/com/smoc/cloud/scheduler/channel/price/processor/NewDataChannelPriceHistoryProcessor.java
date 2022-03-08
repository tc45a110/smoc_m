package com.smoc.cloud.scheduler.channel.price.processor;

import com.google.gson.Gson;
import com.smoc.cloud.scheduler.channel.price.service.model.ChannelPriceModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


/**
 * 通道价格历史 批处理
 * 对新数据进行特殊处理
 */
@Slf4j
@Component
public class NewDataChannelPriceHistoryProcessor implements ItemProcessor<ChannelPriceModel, ChannelPriceModel> {
    @Override
    public ChannelPriceModel process(ChannelPriceModel channelPriceModel) throws Exception {
        //log.info("[channelPriceModel new Data]:{}",new Gson().toJson(channelPriceModel));
        return channelPriceModel;
    }
}
