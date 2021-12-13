package com.smoc.cloud.admin.service;

import com.smoc.cloud.admin.remote.client.SystemExtendBusinessParamFeignClient;
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
public class SystemExtendBusinessParamService {

    @Autowired
    private SystemExtendBusinessParamFeignClient systemExtendBusinessParamFeignClient;

    /**
     * 查询列表
     */
    public ResponseData<List<SystemExtendBusinessParamValidator>> list(String businessType) {
        try {
            ResponseData<List<SystemExtendBusinessParamValidator>> data = this.systemExtendBusinessParamFeignClient.list(businessType);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id获取信息
     */
    public ResponseData<SystemExtendBusinessParamValidator> findById(String id) {
        try {
            ResponseData<SystemExtendBusinessParamValidator> data = this.systemExtendBusinessParamFeignClient.findById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    public ResponseData save(SystemExtendBusinessParamValidator systemExtendBusinessParamValidator, String op) {
        try {
            ResponseData data = this.systemExtendBusinessParamFeignClient.save(systemExtendBusinessParamValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id删除
     */
    public ResponseData deleteById(String id) {
        try {
            ResponseData data = this.systemExtendBusinessParamFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
