package com.smoc.cloud.scheduler.configuration;

import com.google.gson.Gson;
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
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 系统启动，数据初始化
 */
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

        log.info("[加载业务账号基本信息]");
        Map<String, AccountBaseInfo> accountBaseInfoMap = accountRepository.getAccountBaseInfo();
        Reference.accounts = accountBaseInfoMap;

        log.info("[加载业务账号财务信息]");
        Map<String, AccountFinanceInfo> accountFinances = accountRepository.getAccountFinanceInfo();
        Reference.accountFinances = accountFinances;

        log.info("[加载业务账号运营商价格信息]");
        Map<String, MessagePriceBusinessModel> accountMessagePrices = accountRepository.getAccountMessagePrice();
        Reference.accountMessagePrices = accountMessagePrices;
        //log.info("[accountMessagePrices]:{}", new Gson().toJson(Reference.accountMessagePrices));

        log.info("[账号通道业务模型]");
        Map<String, AccountChannelBusinessModel> accountChannelBusinessModels = channelService.getAccountChannelBusinessModels();
        Reference.accountChannelBusinessModels = accountChannelBusinessModels;

        log.info("[内容路由业务模型]");
        Map<String, ContentRouteBusinessModel> contentRouteBusinessModels = channelRepository.getContentRoutesBusinessModels();
        Reference.contentRouteBusinessModels = contentRouteBusinessModels;
    }
}
