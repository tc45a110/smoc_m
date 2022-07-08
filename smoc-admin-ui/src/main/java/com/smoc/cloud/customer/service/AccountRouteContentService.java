package com.smoc.cloud.customer.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.qo.AccountContentRepairQo;
import com.smoc.cloud.common.smoc.customer.validator.ConfigRouteContentRuleValidator;
import com.smoc.cloud.customer.remote.AccountRouteContentFeignClient;
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
public class AccountRouteContentService {

    @Autowired
    private AccountRouteContentFeignClient accountRouteContentFeignClient;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<ConfigRouteContentRuleValidator>> page(PageParams<ConfigRouteContentRuleValidator> pageParams) {
        try {
            PageList<ConfigRouteContentRuleValidator> pageList = this.accountRouteContentFeignClient.page(pageParams);
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
            PageList<AccountContentRepairQo> pageList = this.accountRouteContentFeignClient.accountList(params);
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
    public ResponseData save(ConfigRouteContentRuleValidator configContentRepairRuleValidator, String op) {
        try {
            ResponseData data = this.accountRouteContentFeignClient.save(configContentRepairRuleValidator, op);
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
    public ResponseData<ConfigRouteContentRuleValidator> findById(String id) {
        try {
            ResponseData data = this.accountRouteContentFeignClient.findById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    public ResponseData deleteById(String id) {
        try {
            ResponseData data = this.accountRouteContentFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    public ResponseData<ConfigRouteContentRuleValidator> findContentRepair(String accountId, String carrier) {
        try {
            ResponseData<ConfigRouteContentRuleValidator> data = this.accountRouteContentFeignClient.findContentRepair(accountId,carrier);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
