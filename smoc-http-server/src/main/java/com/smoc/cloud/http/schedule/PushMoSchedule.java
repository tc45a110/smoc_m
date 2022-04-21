package com.smoc.cloud.http.schedule;

import com.smoc.cloud.http.service.PushMoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 上行短信推送 Schedule
 */
@Component
public class PushMoSchedule {

    @Autowired
    private PushMoService pushMoService;

    @Scheduled(cron = "* */30 * * * ?")
    public void scheduleMoService() {
        pushMoService.pushMo();
    }

}
