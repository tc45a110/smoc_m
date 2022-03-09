package com.smoc.cloud.scheduler.channel.price.scheduling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 更新消息日汇总 通道价格
 */
@Slf4j
@Component
public class UpdateMessageDailyStatisticsChannelSchedule {

    @Scheduled(cron = "0 0/1 * * * ?")
    public void  updateMessageDailyStatisticsChannelSchedule() throws Exception {

    }
}
