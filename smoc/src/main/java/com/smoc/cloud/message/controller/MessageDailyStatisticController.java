package com.smoc.cloud.message.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

/**
 * 短信日统计
 */
@Slf4j
@RestController
@RequestMapping("message/daily")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class MessageDailyStatisticController {
}
