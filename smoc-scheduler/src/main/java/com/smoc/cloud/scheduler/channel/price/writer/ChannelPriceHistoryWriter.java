package com.smoc.cloud.scheduler.channel.price.writer;

import com.smoc.cloud.common.smoc.configuate.validator.ChannelPriceValidator;
import com.smoc.cloud.scheduler.channel.price.service.ChannelPriceHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 通道价格历史 批处理
 */
@Slf4j
@Component
public class ChannelPriceHistoryWriter implements ItemWriter<ChannelPriceValidator> {

    @Autowired
    private ChannelPriceHistoryService channelPriceHistoryService;

    @Override
    public void write(List<? extends ChannelPriceValidator> list) throws Exception {
        channelPriceHistoryService.saveHistory(list);
    }
}
