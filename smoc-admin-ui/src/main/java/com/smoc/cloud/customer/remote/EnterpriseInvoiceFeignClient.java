package com.smoc.cloud.customer.remote;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseInvoiceInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


/**
 * 企业发票信息管理远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface EnterpriseInvoiceFeignClient {

    /**
     * 查询列表
     * @param enterpriseInvoiceInfoValidator
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/enterprise/invoice/page", method = RequestMethod.POST)
    ResponseData<List<EnterpriseInvoiceInfoValidator>> page(@RequestBody EnterpriseInvoiceInfoValidator enterpriseInvoiceInfoValidator) throws Exception;


    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/enterprise/invoice/findById/{id}", method = RequestMethod.GET)
    ResponseData<EnterpriseInvoiceInfoValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/enterprise/invoice/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody EnterpriseInvoiceInfoValidator enterpriseInvoiceInfoValidator, @PathVariable String op) throws Exception;

    /**
     * 根据id删除系统数据
     */
    @RequestMapping(value = "/enterprise/invoice/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable String id) throws Exception;

    /**
     * 根据类型和企业id查询数据
     * @param enterpriseId
     * @param type
     * @return
     */
    @RequestMapping(value = "/enterprise/invoice/findByEnterpriseIdAndInvoiceType/{enterpriseId}/{type}", method = RequestMethod.GET)
    ResponseData<EnterpriseInvoiceInfoValidator> findByEnterpriseIdAndInvoiceType(@PathVariable String enterpriseId, @PathVariable String type) throws Exception;
}
