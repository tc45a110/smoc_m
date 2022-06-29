package com.smoc.cloud.scheduler.batch.filters.listener;

import com.smoc.cloud.common.utils.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class MessageFilterListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("[短信过滤 Listener][开始]数据：{}", DateTimeUtils.getDateFormat(new Date(), "yyyy-MM-dd HH mm ss SSS"));
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("[短信过滤 Listener][结束]数据：{}", DateTimeUtils.getDateFormat(new Date(), "yyyy-MM-dd HH mm ss SSS"));
    }
}
