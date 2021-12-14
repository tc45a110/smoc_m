package com.smoc.cloud.admin.security.remote.service;


import com.smoc.cloud.admin.security.remote.client.SystemExtendBusinessParameterFeignClient;
import com.smoc.cloud.common.auth.validator.SystemExtendBusinessParamValidator;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务扩展参数
 */
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SystemExtendBusinessParameterService {

    @Autowired
    private SystemExtendBusinessParameterFeignClient SystemExtendBusinessParameterFeignClient;

    /**
     * 获取列表数据
     */
    public ResponseData<List<SystemExtendBusinessParamValidator>> list(String businessType) {
        try {
            ResponseData<List<SystemExtendBusinessParamValidator>> data = this.SystemExtendBusinessParameterFeignClient.list(businessType);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
