package com.smoc.cloud.saler.remote;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.saler.qo.CustomerAccountInfoQo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(name = "smoc", path = "/smoc")
public interface CustomerFeignClient {

    /**
     * 客户业务账号列表
     * @param params
     * @return
     */
    @RequestMapping(value = "/saler/customer/page", method = RequestMethod.POST)
    ResponseData<PageList<CustomerAccountInfoQo>> page(PageParams<CustomerAccountInfoQo> params);

    /**
     * 查询账号信息
     * @param accountId
     * @return
     */
    @RequestMapping(value = "/account/findById/{id}", method = RequestMethod.GET)
    ResponseData<AccountBasicInfoValidator> findAccountById(@PathVariable String accountId);
}
