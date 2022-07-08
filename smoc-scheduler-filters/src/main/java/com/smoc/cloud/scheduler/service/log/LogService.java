package com.smoc.cloud.scheduler.service.log;


import com.smoc.cloud.scheduler.batch.filters.model.BusinessRouteValue;
import com.smoc.cloud.scheduler.configuration.SystemConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 统一日志输出格式
 */
@Slf4j
@Service
public class LogService {

    @Autowired
    private SystemConfiguration systemConfiguration;

    public void info(BusinessRouteValue businessRouteValue) {
        log.info("[info]accountId={}&phoneNumber={}&messageContent={}&statusCode={}&statusMessage={}", businessRouteValue.getAccountId(), businessRouteValue.getPhoneNumber(), businessRouteValue.getMessageContent(), businessRouteValue.getStatusCode(), businessRouteValue.getStatusMessage());
    }

    public void error(BusinessRouteValue businessRouteValue) {
        log.error("[error]accountId={}&phoneNumber={}&messageContent={}&statusCode={}&statusMessage={}", businessRouteValue.getAccountId(), businessRouteValue.getPhoneNumber(), businessRouteValue.getMessageContent(), businessRouteValue.getStatusCode(), businessRouteValue.getStatusMessage());
    }

    public void warning(BusinessRouteValue businessRouteValue) {
        log.warn("[warning]accountId={}&phoneNumber={}&messageContent={}&statusCode={}&statusMessage={}", businessRouteValue.getAccountId(), businessRouteValue.getPhoneNumber(), businessRouteValue.getMessageContent(), businessRouteValue.getStatusCode(), businessRouteValue.getStatusMessage());
    }
}
