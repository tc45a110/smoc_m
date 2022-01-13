package com.smoc.cloud.customer.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 企业开户管理远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface EnterpriseFeignClient {

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/enterprise/page", method = RequestMethod.POST)
    PageList<EnterpriseBasicInfoValidator> page(@RequestBody PageParams<EnterpriseBasicInfoValidator> pageParams)  throws Exception;

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/enterprise/findById/{id}", method = RequestMethod.GET)
    ResponseData<EnterpriseBasicInfoValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/enterprise/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody EnterpriseBasicInfoValidator enterpriseBasicInfoValidator, @PathVariable String op) throws Exception;

    /**
     * 注销、启用企业业务
     * @param id
     * @param status
     * @return
     */
    @RequestMapping(value = "/enterprise/forbiddenEnterprise/{id}/{status}", method = RequestMethod.GET)
    ResponseData forbiddenEnterprise(@PathVariable String id, @PathVariable String status) throws Exception;
}
