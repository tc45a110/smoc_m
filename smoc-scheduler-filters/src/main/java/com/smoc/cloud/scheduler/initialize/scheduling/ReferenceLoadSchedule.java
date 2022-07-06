package com.smoc.cloud.scheduler.initialize.scheduling;


import com.smoc.cloud.scheduler.initialize.Reference;
import com.smoc.cloud.scheduler.initialize.entity.AccountBaseInfo;
import com.smoc.cloud.scheduler.initialize.model.AccountChannelBusinessModel;
import com.smoc.cloud.scheduler.initialize.model.ContentRouteBusinessModel;
import com.smoc.cloud.scheduler.initialize.repository.AccountRepository;
import com.smoc.cloud.scheduler.initialize.repository.ChannelRepository;
import com.smoc.cloud.scheduler.service.channel.ChannelService;
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


    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ChannelService channelService;

    //1分钟执行一次
    @Scheduled(cron = "0 0/1 * * * ?")
    public void scheduleLoadAccountService() {
        Map<String, AccountBaseInfo> accountBaseInfoMap = accountRepository.getAccountBaseInfo();
        log.info("[加载业务账号基本信息]：{}", accountBaseInfoMap.size());
        Reference.accounts = accountBaseInfoMap;

    }

    //1分钟执行一次
    @Scheduled(cron = "0 0/1 * * * ?")
    public void scheduleLoadAccountChannelService() {
        Map<String, AccountChannelBusinessModel> accountChannelBusinessModels = channelService.getAccountChannelBusinessModels();
        log.info("[账号通道业务模型]");
        Reference.accountChannelBusinessModels = accountChannelBusinessModels;

    }

    //1分钟执行一次
    @Scheduled(cron = "0 0/1 * * * ?")
    public void scheduleLoadContentRouteService() {
        Map<String, ContentRouteBusinessModel> contentRouteBusinessModelMap = channelRepository.getContentRoutesBusinessModels();
        log.info("[内容路由业务模型]");
        Reference.contentRouteBusinessModels = contentRouteBusinessModelMap;

    }
}
