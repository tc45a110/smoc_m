package com.smoc.cloud.iot.carrier.service;

import com.smoc.cloud.iot.carrier.repository.IotCarrierFlowPoolRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class IotCarrierFlowPoolService {

    @Resource
    private IotCarrierFlowPoolRepository iotCarrierFlowPoolRepository;
}
