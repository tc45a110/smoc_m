package com.smoc.cloud.scheduler.service.log;


import com.smoc.cloud.scheduler.batch.filters.model.BusinessRouteValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogService {

    public void info(BusinessRouteValue businessRouteValue) {
        log.info("info:accountId={}&phoneNumber={}&messageContent={}&statusCode={}&statusMessage={}", businessRouteValue.getAccountId(), businessRouteValue.getPhoneNumber(), businessRouteValue.getMessageContent(), businessRouteValue.getStatusCode(), businessRouteValue.getStatusMessage());
    }
    public void error(BusinessRouteValue businessRouteValue) {
        log.error("error:accountId={}&phoneNumber={}&messageContent={}&statusCode={}&statusMessage={}", businessRouteValue.getAccountId(), businessRouteValue.getPhoneNumber(), businessRouteValue.getMessageContent(), businessRouteValue.getStatusCode(), businessRouteValue.getStatusMessage());
    }
    public void warning(BusinessRouteValue businessRouteValue) {
        log.error("warning:accountId={}&phoneNumber={}&messageContent={}&statusCode={}&statusMessage={}", businessRouteValue.getAccountId(), businessRouteValue.getPhoneNumber(), businessRouteValue.getMessageContent(), businessRouteValue.getStatusCode(), businessRouteValue.getStatusMessage());
    }
}
