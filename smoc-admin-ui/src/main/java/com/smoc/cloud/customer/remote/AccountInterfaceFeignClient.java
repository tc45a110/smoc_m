package com.smoc.cloud.customer.remote;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountInterfaceInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


/**
 * 账号接口管理远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface AccountInterfaceFeignClient {


    /**
     * 查询账号接口信息
     * @param accountId
     * @return
     */
    @RequestMapping(value = "/account/interface/findByAccountId", method = RequestMethod.POST)
    ResponseData<AccountInterfaceInfoValidator> findByAccountId(@PathVariable String accountId) throws Exception;

    /**
     * 保存、修改数据
     *  op 是类型 表示了保存或修改
     * @param accountInterfaceInfoValidator
     * @param op
     * @return
     */
    @RequestMapping(value = "/account/interface/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody AccountInterfaceInfoValidator accountInterfaceInfoValidator, @PathVariable String op) throws Exception;
}
