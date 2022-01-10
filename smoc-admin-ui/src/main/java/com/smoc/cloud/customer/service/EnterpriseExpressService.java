package com.smoc.cloud.customer.service;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseExpressInfoValidator;
import com.smoc.cloud.customer.remote.EnterpriseExpressFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 企业邮寄信息管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EnterpriseExpressService {

    @Autowired
    private EnterpriseExpressFeignClient enterpriseExpressFeignClient;

    /**
     * 查询列表
     * @param enterpriseExpressInfoValidator
     * @return
     */
    public ResponseData<List<EnterpriseExpressInfoValidator>> page(EnterpriseExpressInfoValidator enterpriseExpressInfoValidator) {
        try {
            ResponseData<List<EnterpriseExpressInfoValidator>> data = this.enterpriseExpressFeignClient.page(enterpriseExpressInfoValidator);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    public ResponseData<EnterpriseExpressInfoValidator> findById(String id) {
        try {
            ResponseData<EnterpriseExpressInfoValidator> data = this.enterpriseExpressFeignClient.findById(id);
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
    public ResponseData save(EnterpriseExpressInfoValidator enterpriseExpressInfoValidator, String op) {

        try {
            ResponseData data = this.enterpriseExpressFeignClient.save(enterpriseExpressInfoValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }


    /**
     * 根据id删除系统数据
     */
    public ResponseData deleteById(String id){
        try {
            ResponseData data = this.enterpriseExpressFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
