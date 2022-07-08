package com.smoc.cloud.scheduler.batch.filters.writer;

import com.smoc.cloud.scheduler.batch.filters.model.BusinessRouteValue;
import com.smoc.cloud.scheduler.batch.filters.repository.RouteMessageRepository;
import com.smoc.cloud.scheduler.service.MessageBusinessHandler;
import com.smoc.cloud.scheduler.service.redis.FiltersRedisDataService;
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

        messageBusinessHandler.handleMessageBusiness(list);
        routeMessageRepository.deleteByBatch(list);

    }

}
