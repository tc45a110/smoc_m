package com.smoc.cloud.customer.remote;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseExpressInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


/**
 * 企业邮寄信息管理远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface EnterpriseExpressFeignClient {

    /**
     * 查询列表
     * @param enterpriseExpressInfoValidator
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/enterprise/express/page", method = RequestMethod.POST)
    ResponseData<List<EnterpriseExpressInfoValidator>> page(@RequestBody EnterpriseExpressInfoValidator enterpriseExpressInfoValidator) throws Exception;


    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/enterprise/express/findById/{id}", method = RequestMethod.GET)
    ResponseData<EnterpriseExpressInfoValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/enterprise/express/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody EnterpriseExpressInfoValidator enterpriseExpressInfoValidator, @PathVariable String op) throws Exception;

    /**
     * 根据id删除系统数据
     */
    @RequestMapping(value = "/enterprise/express/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable String id) throws Exception;

}
