package com.smoc.cloud.remote.auth.service;


import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.remote.auth.client.SystemUserLogClient;
import com.smoc.cloud.common.auth.validator.SystemUserLogValidator;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.utils.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SystemUserLogService {

    @Autowired
    private SystemUserLogClient systemUserLogClient;

    /**
     * 异步保存用户操作日志
     *
     * @param userId          用户ID
     * @param module          操作模块标识
     * @param operationType   操作类型
     * @param simpleIntroduce 操作简介
     * @param data            操作数据（json）
     */
    public void logsAsync(String userId, String module, String operationType, String simpleIntroduce, String data) {

        SystemUserLogValidator systemUserLogValidator = new SystemUserLogValidator();
        systemUserLogValidator.setId(UUID.uuid32());
        systemUserLogValidator.setUserId(userId);
        systemUserLogValidator.setModule(module);
        systemUserLogValidator.setOperationType(operationType);
        systemUserLogValidator.setSimpleIntroduce(simpleIntroduce);
        systemUserLogValidator.setLogData(data);
        systemUserLogValidator.setCreatedTime(DateTimeUtils.getNowDateTime());
        this.save(systemUserLogValidator);

    }

    /**
     * 异步保存日志信息
     */
    @Async
    public ResponseData save(SystemUserLogValidator systemUserLogValidator) {
        try {
            ResponseData data = this.systemUserLogClient.save(systemUserLogValidator);
            log.info("[保存操作日志] 状态：{}", data.getMessage());
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }




}
