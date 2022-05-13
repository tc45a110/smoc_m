package com.smoc.cloud.saler.service;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.saler.qo.CustomerAccountInfoQo;
import com.smoc.cloud.saler.remote.CustomerFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 客户管理
 */
@Slf4j
@Service
public class CustomerService {

    @Autowired
    private CustomerFeignClient customerFeignClient;

    /**
     * 客户业务账号列表
     * @param params
     * @return
     */
    public ResponseData<PageList<CustomerAccountInfoQo>> page(PageParams<CustomerAccountInfoQo> params) {
        try {
            ResponseData<PageList<CustomerAccountInfoQo>> page = customerFeignClient.page(params);
            return page;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 查询账号信息
     * @param accountId
     * @return
     */
    public ResponseData<AccountBasicInfoValidator> findAccountById(String accountId) {
        try {
            ResponseData<AccountBasicInfoValidator> data = this.customerFeignClient.findAccountById(accountId);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
