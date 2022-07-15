package com.smoc.cloud.iot.product.service;

import com.smoc.cloud.iot.product.repository.IotProductCardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class IotProductCardService {

    @Resource
    private IotProductCardRepository iotProductCardRepository;
}
