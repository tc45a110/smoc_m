package com.smoc.cloud.iot.account.service;

import com.smoc.cloud.iot.account.repository.IotUserProductInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class IotUserProductInfoService {

    @Resource
    private IotUserProductInfoRepository iotUserProductInfoRepository;
}
