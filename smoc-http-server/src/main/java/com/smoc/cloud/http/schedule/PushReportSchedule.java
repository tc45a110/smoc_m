package com.smoc.cloud.http.schedule;

import com.smoc.cloud.http.service.PushReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 状态报告Schedule
 */
@Component
public class PushReportSchedule {

    @Autowired
    private PushReportService pushReportService;

    @Scheduled(cron = "* */10 * * * ?")
    public void scheduleMoService() {
        pushReportService.push();
    }
}
