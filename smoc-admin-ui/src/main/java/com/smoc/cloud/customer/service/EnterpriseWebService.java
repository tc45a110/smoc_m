package com.smoc.cloud.customer.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseWebAccountInfoValidator;
import com.smoc.cloud.customer.remote.EnterpriseWebFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.List;


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

    /**
     * 查询列表
     * @param enterpriseWebAccountInfoValidator
     * @return
     */
    public ResponseData<List<EnterpriseWebAccountInfoValidator>> page(EnterpriseWebAccountInfoValidator enterpriseWebAccountInfoValidator) {
        try {
            ResponseData<List<EnterpriseWebAccountInfoValidator>> data = this.enterpriseWebFeignClient.page(enterpriseWebAccountInfoValidator);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 重置密码
     * @param enterpriseWebAccountInfoValidator
     * @return
     */
    public ResponseData resetPassword(EnterpriseWebAccountInfoValidator enterpriseWebAccountInfoValidator) {

        try {
            ResponseData data = this.enterpriseWebFeignClient.resetPassword(enterpriseWebAccountInfoValidator);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 注销、启用账号
     * @param id
     * @param status
     * @return
     */
    public ResponseData forbiddenWeb(String id, String status) {
        try {
            ResponseData data = this.enterpriseWebFeignClient.forbiddenWeb(id,status);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 查询web账号
     * @param params
     * @return
     */
    public ResponseData<PageList<EnterpriseWebAccountInfoValidator>> webAll(PageParams<EnterpriseWebAccountInfoValidator> params) {
        try {
            ResponseData<PageList<EnterpriseWebAccountInfoValidator>> data = this.enterpriseWebFeignClient.webAll(params);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
