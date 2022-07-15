package com.smoc.cloud.iot.carrier.service;

import com.smoc.cloud.iot.carrier.repository.IotFlowCardsPrimaryInfoRepository;
import com.smoc.cloud.iot.carrier.repository.IotFlowCardsSecondaryInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class IotFlowCardsPrimaryInfoService {

    @Resource
    private IotFlowCardsPrimaryInfoRepository iotFlowCardsPrimaryInfoRepository;

    @Resource
    private IotFlowCardsSecondaryInfoRepository iotFlowCardsSecondaryInfoRepository;
}
