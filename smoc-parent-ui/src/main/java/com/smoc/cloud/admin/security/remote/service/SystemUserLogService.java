package com.smoc.cloud.admin.security.remote.service;


import com.smoc.cloud.admin.security.remote.client.SystemUserLogClient;
import com.smoc.cloud.common.auth.validator.SystemUserLogValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
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
     * 分页查询日志信息
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<SystemUserLogValidator>> page(PageParams<SystemUserLogValidator> pageParams) {

        try {
            ResponseData<PageList<SystemUserLogValidator>> data = systemUserLogClient.page(pageParams);
            return data;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }

    }

}
