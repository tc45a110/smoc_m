package com.smoc.cloud.scheduler.initialize.scheduling;


import com.smoc.cloud.scheduler.initialize.Reference;
import com.smoc.cloud.scheduler.initialize.entity.AccountBaseInfo;
import com.smoc.cloud.scheduler.initialize.entity.AccountFinanceInfo;
import com.smoc.cloud.scheduler.initialize.entity.AccountMessagePrice;
import com.smoc.cloud.scheduler.initialize.model.AccountChannelBusinessModel;
import com.smoc.cloud.scheduler.initialize.model.ContentRouteBusinessModel;
import com.smoc.cloud.scheduler.initialize.model.MessagePriceBusinessModel;
import com.smoc.cloud.scheduler.initialize.repository.AccountRepository;
import com.smoc.cloud.scheduler.initialize.repository.ChannelRepository;
import com.smoc.cloud.scheduler.service.channel.ChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 定时加载系统数据
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

    /**
     * 业务账号基本信息
     * 1分钟执行一次
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void scheduleLoadAccountService() {
        log.info("[加载业务账号基本信息]");
        Map<String, AccountBaseInfo> accountBaseInfoMap = accountRepository.getAccountBaseInfo();
        Reference.accounts = accountBaseInfoMap;

        log.info("[加载业务账号财务信息]");
        Map<String, AccountFinanceInfo> accountFinances = accountRepository.getAccountFinanceInfo();
        Reference.accountFinances = accountFinances;

        log.info("[加载业务账号运营商价格信息]");
        Map<String, MessagePriceBusinessModel> accountMessagePrices = accountRepository.getAccountMessagePrice();
        Reference.accountMessagePrices = accountMessagePrices;
    }


    /**
     * 业务账号通道路由业务模型
     * 1分钟执行一次
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void scheduleLoadAccountChannelService() {
        log.info("[构建账号通道业务模型]");
        Map<String, AccountChannelBusinessModel> accountChannelBusinessModels = channelService.getAccountChannelBusinessModels();
        Reference.accountChannelBusinessModels = accountChannelBusinessModels;

    }

    /**
     * 业务账号内容路由业务模型
     * 1分钟执行一次
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void scheduleLoadContentRouteService() {
        log.info("[构建内容路由业务模型]");
        Map<String, ContentRouteBusinessModel> contentRouteBusinessModelMap = channelRepository.getContentRoutesBusinessModels();
        Reference.contentRouteBusinessModels = contentRouteBusinessModelMap;

    }
}
