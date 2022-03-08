package com.smoc.cloud.scheduler.account.price.scheduling;

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
 * 业务账号价格历史 批处理
 */
@Component
public class AccountPriceSchedulingJob {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    JobOperator jobOperator;

    @Resource(name="accountPriceHistoryJob")
    private Job accountPriceHistoryJob;

    @Scheduled(cron = "0 0/5 * * * ?")
    public void channelPriceHistoryJob() throws Exception {
        JobParameters jobParameter = new JobParametersBuilder().addLong("times",System.currentTimeMillis()).toJobParameters();
        JobExecution run = jobLauncher.run(accountPriceHistoryJob, jobParameter);
        run.getId();
    }
}
