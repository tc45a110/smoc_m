package com.smoc.cloud.customer.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.qo.AccountContentRepairQo;
import com.smoc.cloud.common.smoc.customer.validator.ConfigContentRepairRuleValidator;
import com.smoc.cloud.customer.remote.AccountContentRepairFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;


/**
 * 业务账号内容失败补发管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AccountContentRepairService {

    @Autowired
    private AccountContentRepairFeignClient accountContentRepairFeignClient;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<ConfigContentRepairRuleValidator>> page(PageParams<ConfigContentRepairRuleValidator> pageParams) {
        try {
            PageList<ConfigContentRepairRuleValidator> pageList = this.accountContentRepairFeignClient.page(pageParams);
            return ResponseDataUtil.buildSuccess(pageList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 业务账号列表
     * @param params
     * @return
     */
    public ResponseData<PageList<AccountContentRepairQo>> accountList(PageParams<AccountContentRepairQo> params) {
        try {
            PageList<AccountContentRepairQo> pageList = this.accountContentRepairFeignClient.accountList(params);
            return ResponseDataUtil.buildSuccess(pageList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    public ResponseData save(ConfigContentRepairRuleValidator configContentRepairRuleValidator, String op) {
        try {
            ResponseData data = this.accountContentRepairFeignClient.save(configContentRepairRuleValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据ID 查询
     *
     * @param id
     * @return
     */
    public ResponseData<ConfigContentRepairRuleValidator> findById(String id) {
        try {
            ResponseData data = this.accountContentRepairFeignClient.findById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    public ResponseData deleteById(String id) {
        try {
            ResponseData data = this.accountContentRepairFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    public ResponseData<ConfigContentRepairRuleValidator> findContentRepair(String accountId, String carrier) {
        try {
            ResponseData<ConfigContentRepairRuleValidator> data = this.accountContentRepairFeignClient.findContentRepair(accountId,carrier);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
