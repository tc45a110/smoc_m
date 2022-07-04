package com.smoc.cloud.scheduler.channel.price.processor;

import com.smoc.cloud.scheduler.channel.price.service.model.ChannelFutruePriceModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;


/**
 * 通道未来价格 批处理
 */
@Slf4j
@Component
public class ChannelPriceFutureProcessor implements ItemProcessor<ChannelFutruePriceModel, ChannelFutruePriceModel> {
    @Override
    public ChannelFutruePriceModel process(ChannelFutruePriceModel channelFutruePriceModel) throws Exception {
        //log.info("[AccountPriceModel]:{}",new Gson().toJson(accountPriceModel));
        return channelFutruePriceModel;
    }
}
