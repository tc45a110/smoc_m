package com.smoc.cloud.scheduler.channel.price.scheduling;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 通道价格历史 批处理
 * 对新数据进行特殊处理
 */
@Component
public class NewDataChannelPriceSchedulingSchedule {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    JobOperator jobOperator;

    @Resource(name="newDataChannelPriceHistoryJob")
    private Job newDataChannelPriceHistoryJob;

    @Scheduled(cron = "0 0/5 * * * ?")
    public void newDataChannelPriceHistorySchedule() throws Exception {
        JobParameters jobParameter = new JobParametersBuilder().addLong("times",System.currentTimeMillis()).toJobParameters();
        JobExecution run = jobLauncher.run(newDataChannelPriceHistoryJob, jobParameter);
        run.getId();
    }
}
