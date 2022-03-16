package com.smoc.cloud.material.service;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.message.MessageAccountValidator;
import com.smoc.cloud.common.smoc.message.model.StatisticMessageSend;
import com.smoc.cloud.material.remote.BusinessAccountFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 账号管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BusinessAccountService {

    @Autowired
    private BusinessAccountFeignClient businessAccountFeignClient;

    /**
     * 查询企业所有的业务账号
     * @param accountBasicInfoValidator
     * @return
     */
    public ResponseData<List<AccountBasicInfoValidator>> findBusinessAccount(AccountBasicInfoValidator accountBasicInfoValidator) {
        try {
            ResponseData<List<AccountBasicInfoValidator>> data = this.businessAccountFeignClient.findBusinessAccount(accountBasicInfoValidator);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 查询企业下的账户和余额
     * @param messageAccountValidator
     * @return
     */
    public ResponseData<List<MessageAccountValidator>> messageAccountList(MessageAccountValidator messageAccountValidator) {
        try {
            ResponseData<List<MessageAccountValidator>> data = this.businessAccountFeignClient.messageAccountList(messageAccountValidator);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
