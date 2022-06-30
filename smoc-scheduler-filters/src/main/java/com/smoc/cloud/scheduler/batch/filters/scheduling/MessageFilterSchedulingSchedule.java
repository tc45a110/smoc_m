package com.smoc.cloud.scheduler.batch.filters.scheduling;

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
 *
 */
@Component
public class MessageFilterSchedulingSchedule {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    JobOperator jobOperator;

    @Resource(name="messageFilterJob")
    private Job messageFilterJob;

    @Scheduled(cron = "*/1 * * * * ?")
    public void accountPriceHistorySchedule() throws Exception {
        JobParameters jobParameter = new JobParametersBuilder().addLong("filters",System.currentTimeMillis()).toJobParameters();
        JobExecution run = jobLauncher.run(messageFilterJob, jobParameter);
        run.getId();
    }
}
