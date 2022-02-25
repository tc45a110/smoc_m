package com.smoc.cloud.customer.remote;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseChainInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


/**
 * 签名合同链远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface EnterpriseChainFeignClient {

    /**
     * 查询列表
     * @param enterpriseChainInfoValidator
     * @return
     */
    @RequestMapping(value = "/ec/customer/chain/page", method = RequestMethod.POST)
    ResponseData<List<EnterpriseChainInfoValidator>> page(@RequestBody EnterpriseChainInfoValidator enterpriseChainInfoValidator)  throws Exception;

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/ec/customer/chain/findById/{id}", method = RequestMethod.GET)
    ResponseData<EnterpriseChainInfoValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/ec/customer/chain/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody EnterpriseChainInfoValidator enterpriseChainInfoValidator, @PathVariable String op) throws Exception;

    /**
     * 根据id删除系统数据
     */
    @RequestMapping(value = "/ec/customer/chain/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable String id) throws Exception;
}
