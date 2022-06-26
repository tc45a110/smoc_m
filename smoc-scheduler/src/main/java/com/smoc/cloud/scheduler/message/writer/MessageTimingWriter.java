package com.smoc.cloud.scheduler.message.writer;

import com.smoc.cloud.scheduler.message.service.model.MessageTimingModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时发送短信任务 批处理
 */
@Slf4j
@Component
public class MessageTimingWriter implements ItemWriter<MessageTimingModel> {

    @Override
    public void write(List<? extends MessageTimingModel> list) throws Exception {

    }
}
