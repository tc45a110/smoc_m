package com.smoc.cloud.iot.system.service;

import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.iot.system.remote.EnterpriseFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 企业开户管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EnterpriseService {

    @Autowired
    private EnterpriseFeignClient enterpriseFeignClient;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<EnterpriseBasicInfoValidator>> page(PageParams<EnterpriseBasicInfoValidator> pageParams) {
        try {
            PageList<EnterpriseBasicInfoValidator> pageList = this.enterpriseFeignClient.page(pageParams);
            return ResponseDataUtil.buildSuccess(pageList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData<EnterpriseBasicInfoValidator> findById(String id) {
        try {
            ResponseData<EnterpriseBasicInfoValidator> data = this.enterpriseFeignClient.findById(id);
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
    public ResponseData save(EnterpriseBasicInfoValidator enterpriseBasicInfoValidator, String op) {

        try {
            ResponseData data = this.enterpriseFeignClient.save(enterpriseBasicInfoValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 注销、启用企业业务
     *
     * @param id
     * @param status
     * @return
     */
    public ResponseData forbiddenEnterprise(String id, String status) {
        try {
            ResponseData data = this.enterpriseFeignClient.forbiddenEnterprise(id, status);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 生成企业标识
     *
     * @return
     */
    public ResponseData<String> createEnterpriseFlag() {
        try {
            ResponseData<String> enterpriseFlag = this.enterpriseFeignClient.createEnterpriseFlag();
            return enterpriseFlag;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据账号类型查询企业列表
     *
     * @return
     */
    public ResponseData<List<Nodes>> findByAccountBusinessType(String businessType) {
        try {
            ResponseData<List<Nodes>> data = this.enterpriseFeignClient.findByAccountBusinessType(businessType);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
