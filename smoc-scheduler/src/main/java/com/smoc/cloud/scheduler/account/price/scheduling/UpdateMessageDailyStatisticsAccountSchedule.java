package com.smoc.cloud.scheduler.account.price.scheduling;

import lombok.extern.slf4j.Slf4j;
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
 * 更新消息日汇总 业务账号价格
 */
@Slf4j
@Component
public class UpdateMessageDailyStatisticsAccountSchedule {

    @Scheduled(cron = "0 0/1 * * * ?")
    public void  updateMessageDailyStatisticsAccountSchedule() throws Exception {

    }
}
