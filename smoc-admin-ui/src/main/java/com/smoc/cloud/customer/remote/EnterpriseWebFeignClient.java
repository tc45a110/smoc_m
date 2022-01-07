package com.smoc.cloud.customer.remote;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseWebAccountInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 企业WEB登录账号管理远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface EnterpriseWebFeignClient {


    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/enterprise/web/findById/{id}", method = RequestMethod.GET)
    ResponseData<EnterpriseWebAccountInfoValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/enterprise/web/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody EnterpriseWebAccountInfoValidator enterpriseWebAccountInfoValidator, @PathVariable String op) throws Exception;


}
