package com.smoc.cloud.user.remote;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 企业管理远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface WebEnterpriseFeignClient {

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/enterprise/findById/{id}", method = RequestMethod.GET)
    ResponseData<EnterpriseBasicInfoValidator> findById(@PathVariable String id) throws Exception;

}
