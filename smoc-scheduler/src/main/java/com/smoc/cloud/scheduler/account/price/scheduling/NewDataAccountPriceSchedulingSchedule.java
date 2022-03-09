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
 * 对新数据进行特殊处理
 */
@Component
public class NewDataAccountPriceSchedulingSchedule {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    JobOperator jobOperator;

    @Resource(name="newDataAccountPriceHistoryJob")
    private Job newDataAccountPriceHistoryJob;

    @Scheduled(cron = "0 0/5 * * * ?")
    public void newDataAccountPriceHistorySchedule() throws Exception {
        JobParameters jobParameter = new JobParametersBuilder().addLong("times",System.currentTimeMillis()).toJobParameters();
        JobExecution run = jobLauncher.run(newDataAccountPriceHistoryJob, jobParameter);
        run.getId();
    }
}
