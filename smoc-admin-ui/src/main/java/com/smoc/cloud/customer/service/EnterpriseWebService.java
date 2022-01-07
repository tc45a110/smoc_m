package com.smoc.cloud.customer.service;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseWebAccountInfoValidator;
import com.smoc.cloud.customer.remote.EnterpriseWebFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;


/**
 * 企业WEB登录账号管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EnterpriseWebService {

    @Autowired
    private EnterpriseWebFeignClient enterpriseWebFeignClient;

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    public ResponseData<EnterpriseWebAccountInfoValidator> findById(String id) {
        try {
            ResponseData<EnterpriseWebAccountInfoValidator> data = this.enterpriseWebFeignClient.findById(id);
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
    public ResponseData save(EnterpriseWebAccountInfoValidator enterpriseWebAccountInfoValidator, String op) {

        try {
            ResponseData data = this.enterpriseWebFeignClient.save(enterpriseWebAccountInfoValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }



}
