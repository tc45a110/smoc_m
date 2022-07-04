package com.smoc.cloud.scheduler.channel.price.writer;

import com.smoc.cloud.scheduler.channel.price.service.ChannelPriceHistoryService;
import com.smoc.cloud.scheduler.channel.price.service.model.ChannelFutruePriceModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 通道未来价格 批处理
 */
@Slf4j
@Component
public class ChannelPriceFutureWriter implements ItemWriter<ChannelFutruePriceModel> {

    @Autowired
    private ChannelPriceHistoryService channelPriceHistoryService;

    @Override
    public void write(List<? extends ChannelFutruePriceModel> list) throws Exception {
        channelPriceHistoryService.saveFutrue(list);
    }
}
