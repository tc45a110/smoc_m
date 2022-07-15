package com.smoc.cloud.iot.carrier.service;

import com.smoc.cloud.iot.carrier.repository.IotFlowCardsChangeHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class IotFlowCardsChangeHistoryService {

    @Resource
    private IotFlowCardsChangeHistoryRepository iotFlowCardsChangeHistoryRepository;
}
