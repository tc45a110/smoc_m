package com.smoc.cloud.customer.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountSignRegisterValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "smoc", path = "/smoc")
public interface AccountSignRegisterFeignClient {

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/sign/register/page", method = RequestMethod.POST)
    ResponseData<PageList<AccountSignRegisterValidator>> page(@RequestBody PageParams<AccountSignRegisterValidator> pageParams) throws Exception;

    /**
     * 添加、修改
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/sign/register/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody AccountSignRegisterValidator accountSignRegisterValidator, @PathVariable String op) throws Exception;

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/sign/register/findById/{id}", method = RequestMethod.GET)
    ResponseData<AccountSignRegisterValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 注销
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/sign/register/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable String id) throws Exception;

    /**
     * 根据业务账号，查询已占用的签名自定义扩展号
     * @param account
     * @param id 当id 不为空时候，不查询本id的签名自定义扩展号
     * @return
     */
    @RequestMapping(value = "/sign/register/findExtendDataByAccount/{account}/{id}", method = RequestMethod.GET)
    ResponseData<List<String>> findExtendDataByAccount(@PathVariable String account, @PathVariable String id) throws Exception;
}
