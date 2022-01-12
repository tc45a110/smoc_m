package com.smoc.cloud.customer.service;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseInvoiceInfoValidator;
import com.smoc.cloud.customer.remote.EnterpriseInvoiceFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 企业发票信息管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EnterpriseInvoiceService {

    @Autowired
    private EnterpriseInvoiceFeignClient enterpriseInvoiceFeignClient;

    /**
     * 查询列表
     * @param enterpriseInvoiceInfoValidator
     * @return
     */
    public ResponseData<List<EnterpriseInvoiceInfoValidator>> page(EnterpriseInvoiceInfoValidator enterpriseInvoiceInfoValidator) {
        try {
            ResponseData<List<EnterpriseInvoiceInfoValidator>> data = this.enterpriseInvoiceFeignClient.page(enterpriseInvoiceInfoValidator);
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
    public ResponseData<EnterpriseInvoiceInfoValidator> findById(String id) {
        try {
            ResponseData<EnterpriseInvoiceInfoValidator> data = this.enterpriseInvoiceFeignClient.findById(id);
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
    public ResponseData save(EnterpriseInvoiceInfoValidator enterpriseInvoiceInfoValidator, String op) {

        try {
            ResponseData data = this.enterpriseInvoiceFeignClient.save(enterpriseInvoiceInfoValidator, op);
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
            ResponseData data = this.enterpriseInvoiceFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据类型和企业id查询数据
     * @param enterpriseId
     * @param type
     * @return
     */
    public ResponseData<EnterpriseInvoiceInfoValidator> findByEnterpriseIdAndInvoiceType(String enterpriseId, String type) {
        try {
            ResponseData<EnterpriseInvoiceInfoValidator> data = this.enterpriseInvoiceFeignClient.findByEnterpriseIdAndInvoiceType(enterpriseId,type);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
