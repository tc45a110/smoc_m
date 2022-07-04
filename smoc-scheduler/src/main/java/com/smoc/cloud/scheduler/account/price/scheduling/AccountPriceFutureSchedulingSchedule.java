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
 * 业务账号未来价格 批处理
 */
@Component
public class AccountPriceFutureSchedulingSchedule {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    JobOperator jobOperator;

    @Resource(name="accountPriceFutureJob")
    private Job accountPriceFutureJob;

    @Scheduled(cron = "0 0/5 * * * ?")
    public void accountPriceHistorySchedule() throws Exception {
        JobParameters jobParameter = new JobParametersBuilder().addLong("times",System.currentTimeMillis()).toJobParameters();
        JobExecution run = jobLauncher.run(accountPriceFutureJob, jobParameter);
        run.getId();
    }
}
