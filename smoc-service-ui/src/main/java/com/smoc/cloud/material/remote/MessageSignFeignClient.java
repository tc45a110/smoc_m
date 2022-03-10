package com.smoc.cloud.material.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseDocumentInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


/**
 * 资质管理远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface MessageSignFeignClient {

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/ec/customer/document/page", method = RequestMethod.POST)
    PageList<EnterpriseDocumentInfoValidator> page(@RequestBody PageParams<EnterpriseDocumentInfoValidator> pageParams)  throws Exception;

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/ec/customer/document/findById/{id}", method = RequestMethod.GET)
    ResponseData<EnterpriseDocumentInfoValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/ec/customer/document/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody EnterpriseDocumentInfoValidator enterpriseDocumentInfoValidator, @PathVariable String op) throws Exception;

    /**
     * 根据id删除系统数据
     */
    @RequestMapping(value = "/ec/customer/document/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable String id) throws Exception;

    /**
     * 查询签名
     * @param enterpriseDocumentInfoValidator
     * @return
     */
    @RequestMapping(value = "/ec/customer/document/findMessageSign", method = RequestMethod.POST)
    ResponseData<List<EnterpriseDocumentInfoValidator>> findMessageSign(@RequestBody EnterpriseDocumentInfoValidator enterpriseDocumentInfoValidator)throws Exception;
}
