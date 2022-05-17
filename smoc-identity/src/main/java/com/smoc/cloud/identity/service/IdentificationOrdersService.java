package com.smoc.cloud.identity.service;

import com.google.gson.Gson;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationOrdersInfoValidator;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationRequestDataValidator;
import com.smoc.cloud.identity.remote.IdentificationOrdersFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 认证订单管理
 */
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class IdentificationOrdersService {

    @Autowired
    private IdentificationOrdersFeignClient identificationOrdersFeignClient;

    /**
     * 保存订单，并冻结金额
     */
    public ResponseData save(IdentificationOrdersInfoValidator identificationOrdersInfoValidator) {

        try {
            ResponseData data = this.identificationOrdersFeignClient.save(identificationOrdersInfoValidator, "add");
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 修改订单，并计费
     */
    @Async
    public void update(IdentificationOrdersInfoValidator identificationOrdersInfoValidator) {

        try {
            ResponseData data = this.identificationOrdersFeignClient.update(identificationOrdersInfoValidator);
            log.info("[计费状态]订单号{}:{}",identificationOrdersInfoValidator.getOrderNo(),new Gson().toJson(data));
        } catch (Exception e) {
            log.error("[计费状态]:{}",e.getMessage());
        }
    }

    /**
     * 保存原数据
     */
    @Async
    public void save(IdentificationRequestDataValidator identificationRequestDataValidator) {
        try {
            this.identificationOrdersFeignClient.save(identificationRequestDataValidator);;
        } catch (Exception e) {
            log.error("[计费状态]:{}",e.getMessage());
        }
    }
}
