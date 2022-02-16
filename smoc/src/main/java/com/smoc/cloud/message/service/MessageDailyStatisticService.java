package com.smoc.cloud.message.service;

import com.smoc.cloud.message.repository.MessageDailyStatisticRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class MessageDailyStatisticService {

    @Resource
    private MessageDailyStatisticRepository messageDailyStatisticRepository;
}
