package com.smoc.cloud.admin.remote.client;

import com.smoc.cloud.common.auth.validator.SystemExtendBusinessParamValidator;
import com.smoc.cloud.common.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 业务扩展参数远程服务接口
 */
@FeignClient(name = "smoc-auth",path = "/auth")
public interface SystemExtendBusinessParamFeignClient {

    /**
     * 查询列表
     */
    @RequestMapping(value = "/param/list/{businessType}", method = RequestMethod.GET)
    ResponseData<List<SystemExtendBusinessParamValidator>> list(@PathVariable String businessType) throws Exception;

    /**
     * 根据id获取信息
     */
    @RequestMapping(value = "/param/findById/{id}", method = RequestMethod.GET)
    ResponseData<SystemExtendBusinessParamValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/param/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody SystemExtendBusinessParamValidator systemExtendBusinessParamValidator, @PathVariable String op) throws Exception;

    /**
     * 根据id删除
     */
    @RequestMapping(value = "/param/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable String id) throws Exception;

}
