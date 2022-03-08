package com.smoc.cloud.material.remote;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


/**
 * 账号管理远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface BusinessAccountFeignClient {

    /**
     * 查询列表
     * @param accountBasicInfoValidator
     * @return
     */
    @RequestMapping(value = "/account/findBusinessAccount", method = RequestMethod.POST)
    ResponseData<List<AccountBasicInfoValidator>> findBusinessAccount(@RequestBody AccountBasicInfoValidator accountBasicInfoValidator)  throws Exception;
}
