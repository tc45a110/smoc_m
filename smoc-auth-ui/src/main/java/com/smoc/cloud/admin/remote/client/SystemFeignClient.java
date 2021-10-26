package com.smoc.cloud.admin.remote.client;

import com.smoc.cloud.common.auth.validator.SystemValidator;
import com.smoc.cloud.common.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 系统管理远程服务接口
 * 2019/4/23 16:28
 **/
@FeignClient(name = "smoc-auth",path = "/auth")
public interface SystemFeignClient {

    /**
     * 获取系统数据
     */
    @RequestMapping(value = "/system/list", method = RequestMethod.GET)
    ResponseData<Iterable<SystemValidator>> list() throws Exception;

    /**
     * 根据id获取系统数据
     */
    @RequestMapping(value = "/system/findById/{id}", method = RequestMethod.GET)
    ResponseData<SystemValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 保存、修改系统数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/system/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody SystemValidator systemValidator, @PathVariable String op) throws Exception;


    /**
     * 根据id删除系统数据
     */
    @RequestMapping(value = "/system/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable String id) throws Exception;
}
