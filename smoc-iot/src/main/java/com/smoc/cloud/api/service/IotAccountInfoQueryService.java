package com.smoc.cloud.api.service;


import com.smoc.cloud.iot.repository.ApiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IotAccountInfoQueryService {

    @Autowired
    private ApiRepository apiRepository;

}
