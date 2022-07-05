package com.smoc.cloud.scheduler.configuration;

import com.smoc.cloud.scheduler.initialize.Reference;
import com.smoc.cloud.scheduler.initialize.entity.AccountBaseInfo;
import com.smoc.cloud.scheduler.initialize.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class ReferenceInitialize implements ApplicationRunner {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, AccountBaseInfo> accountBaseInfoMap = accountRepository.getAccountBaseInfo();
        log.info("[加载业务账号]：{}",accountBaseInfoMap.size());
        Reference.accounts = accountBaseInfoMap;

    }
}
