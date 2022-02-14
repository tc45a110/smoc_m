package com.smoc.cloud.customer.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 业务账号管理远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface BusinessAccountFeignClient {

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/account/page", method = RequestMethod.POST)
    PageList<AccountBasicInfoValidator> page(@RequestBody PageParams<AccountBasicInfoValidator> pageParams)  throws Exception;

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/account/findById/{id}", method = RequestMethod.GET)
    ResponseData<AccountBasicInfoValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/account/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody AccountBasicInfoValidator accountBasicInfoValidator, @PathVariable String op) throws Exception;

    /**
     * 注销、启用账号
     * @param id
     * @param status
     * @return
     */
    @RequestMapping(value = "/account/forbiddenAccountById/{id}/{status}", method = RequestMethod.GET)
    ResponseData forbiddenAccountById(@PathVariable String id, @PathVariable String status) throws Exception;
}
