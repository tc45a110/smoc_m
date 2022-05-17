package com.smoc.cloud.identity.remote;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationOrdersInfoValidator;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationRequestDataValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 认证订单管理
 */
@FeignClient(name = "smoc", path = "/smoc")
public interface IdentificationOrdersFeignClient {

    /**
     * 保存订单，并冻结金额
     */
    @RequestMapping(value = "identification/order/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody IdentificationOrdersInfoValidator identificationOrdersInfoValidator, @PathVariable String op) throws Exception;

    /**
     * 修改订单，并计费
     */
    @RequestMapping(value = "identification/order/update", method = RequestMethod.POST)
    ResponseData update(@RequestBody IdentificationOrdersInfoValidator identificationOrdersInfoValidator) throws Exception;

    /**
     * 保存原数据
     */
    @RequestMapping(value = "identification/data/save", method = RequestMethod.POST)
    void save(@RequestBody IdentificationRequestDataValidator identificationRequestDataValidator) throws Exception;
}
