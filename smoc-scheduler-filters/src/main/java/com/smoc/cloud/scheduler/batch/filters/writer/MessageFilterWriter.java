package com.smoc.cloud.scheduler.batch.filters.writer;

import com.smoc.cloud.scheduler.batch.filters.model.BusinessRouteValue;
import com.smoc.cloud.scheduler.batch.filters.repository.RouteMessageRepository;
import com.smoc.cloud.scheduler.filters.service.MessageBusinessHandler;
import com.smoc.cloud.scheduler.filters.service.service.FiltersRedisDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MessageFilterWriter implements ItemWriter<BusinessRouteValue> {

    @Autowired
    private FiltersRedisDataService filtersRedisDataService;

    @Autowired
    private MessageBusinessHandler messageBusinessHandler;

    @Autowired
    private RouteMessageRepository routeMessageRepository;

    @Override
    public void write(List<? extends BusinessRouteValue> list) throws Exception {

        Integer count  = list.size();
        log.info("[数据加载]条数：{}:{}", count);
        Boolean limiter = filtersRedisDataService.limiterMessageFilter("filter:speed:test",4000,4000,1,count);
        if(limiter){
            messageBusinessHandler.handleMessageBusiness(list);
            routeMessageRepository.deleteByBatch(list);
        }else {
            log.info("[------------数据库加载数据----触发限流-----------------------]");
        }

    }

}
