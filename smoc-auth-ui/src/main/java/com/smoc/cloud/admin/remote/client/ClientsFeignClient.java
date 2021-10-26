package com.smoc.cloud.admin.remote.client;

import com.smoc.cloud.common.auth.validator.ClientDetailsValidator;
import com.smoc.cloud.common.auth.validator.ResetClientSecretValidator;
import com.smoc.cloud.common.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 客户端管理远程服务接口
 * 2019/4/23 16:28
 **/
@FeignClient(name = "smoc-auth", path = "/auth")
public interface ClientsFeignClient {

    /**
     * 查询列表
     */
    @RequestMapping(value = "/client/list", method = RequestMethod.GET)
    ResponseData<List<ClientDetailsValidator>> list() throws Exception;

    /**
     * 根据id获取信息
     */
    @RequestMapping(value = "/client/findById/{id}", method = RequestMethod.GET)
    ResponseData<ClientDetailsValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/client/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody ClientDetailsValidator clientDetailsValidator, @PathVariable String op) throws Exception;

    /**
     * 根据id删除数据
     */
    @RequestMapping(value = "/client/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable String id) throws Exception;

    /**
     * 修改客户端密码
     *
     * @param resetClientSecretValidator
     * @return
     */
    @RequestMapping(value = "/client/resetClientSecret", method = RequestMethod.POST)
    ResponseData resetClientSecret(@RequestBody ResetClientSecretValidator resetClientSecretValidator) throws Exception;

}
