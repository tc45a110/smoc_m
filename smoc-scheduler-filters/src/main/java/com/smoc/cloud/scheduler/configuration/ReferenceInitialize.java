package com.smoc.cloud.scheduler.configuration;

import com.google.gson.Gson;
import com.smoc.cloud.scheduler.initialize.Reference;
import com.smoc.cloud.scheduler.initialize.entity.AccountBaseInfo;
import com.smoc.cloud.scheduler.initialize.repository.AccountRepository;
import com.smoc.cloud.scheduler.initialize.repository.ChannelRepository;
import com.smoc.cloud.scheduler.service.channel.ChannelService;
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

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ChannelService channelService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, AccountBaseInfo> accountBaseInfoMap = accountRepository.getAccountBaseInfo();
        log.info("[加载业务账号基本信息]");
        Reference.accounts = accountBaseInfoMap;
        log.info("[账号通道业务模型]");
        Reference.accountChannelBusinessModels = channelService.getAccountChannelBusinessModels();
        log.info("[内容路由业务模型]");
        Reference.contentRouteBusinessModels = channelRepository.getContentRoutesBusinessModels();
        log.info("{}",new Gson().toJson(Reference.contentRouteBusinessModels));
    }
}
