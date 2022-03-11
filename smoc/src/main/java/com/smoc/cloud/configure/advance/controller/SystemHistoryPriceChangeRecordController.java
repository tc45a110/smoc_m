package com.smoc.cloud.configure.advance.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

@Slf4j
@RestController
@RequestMapping("configure/channel")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class SystemHistoryPriceChangeRecordController {
}
