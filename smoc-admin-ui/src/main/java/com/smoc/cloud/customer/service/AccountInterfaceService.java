package com.smoc.cloud.customer.service;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.AccountInterfaceInfoValidator;
import com.smoc.cloud.customer.remote.AccountInterfaceFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 账号接口管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AccountInterfaceService {

    @Autowired
    private AccountInterfaceFeignClient accountInterfaceFeignClient;

    /**
     * 查询账号接口信息
     * @param accountId
     * @return
     */
    public ResponseData<AccountInterfaceInfoValidator> findByAccountId(String accountId) {
        try {
            ResponseData<AccountInterfaceInfoValidator> list = this.accountInterfaceFeignClient.findByAccountId(accountId);
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 保存、修改数据
     *  op 是类型 表示了保存或修改
     * @param accountInterfaceInfoValidator
     * @param op
     * @return
     */
    public ResponseData save(AccountInterfaceInfoValidator accountInterfaceInfoValidator, String op) {
        try {
            ResponseData data = this.accountInterfaceFeignClient.save(accountInterfaceInfoValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
