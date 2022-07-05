package com.smoc.cloud.scheduler.initialize.scheduling;


import com.smoc.cloud.scheduler.initialize.Reference;
import com.smoc.cloud.scheduler.initialize.entity.AccountBaseInfo;
import com.smoc.cloud.scheduler.initialize.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *
 */
@Slf4j
@Component
public class ReferenceLoadSchedule {

    @Autowired
    private AccountRepository accountRepository;

    //1分钟执行一次
    @Scheduled(cron = "0 0/1 * * * ?")
    public void scheduleLoadAccountService() {
        Map<String, AccountBaseInfo> accountBaseInfoMap = accountRepository.getAccountBaseInfo();
        log.info("[加载业务账号]：{}", accountBaseInfoMap.size());
        Reference.accounts = accountBaseInfoMap;

    }
}
