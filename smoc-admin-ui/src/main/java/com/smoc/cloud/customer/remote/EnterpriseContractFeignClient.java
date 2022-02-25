package com.smoc.cloud.customer.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseContractInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * EC合同管理远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface EnterpriseContractFeignClient {

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/ec/customer/contract/page", method = RequestMethod.POST)
    PageList<EnterpriseContractInfoValidator> page(@RequestBody PageParams<EnterpriseContractInfoValidator> pageParams)  throws Exception;

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/ec/customer/contract/findById/{id}", method = RequestMethod.GET)
    ResponseData<EnterpriseContractInfoValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/ec/customer/contract/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody EnterpriseContractInfoValidator enterpriseContractInfoValidator, @PathVariable String op) throws Exception;

    /**
     * 根据id删除系统数据
     */
    @RequestMapping(value = "/ec/customer/contract/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable String id) throws Exception;
}
