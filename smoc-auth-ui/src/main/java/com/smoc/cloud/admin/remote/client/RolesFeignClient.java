package com.smoc.cloud.admin.remote.client;

import com.smoc.cloud.common.auth.validator.RoleValidator;
import com.smoc.cloud.common.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 角色管理远程服务接口
 * 2019/4/23 16:28
 **/
@FeignClient(name = "smoc-auth",path = "/auth")
public interface RolesFeignClient {

    /**
     * 查询列表
     */
    @RequestMapping(value = "/roles/list", method = RequestMethod.GET)
    ResponseData<List<RoleValidator>> list() throws Exception;

    /**
     * 根据id获取信息
     */
    @RequestMapping(value = "/roles/findById/{id}", method = RequestMethod.GET)
    ResponseData<RoleValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/roles/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody RoleValidator roleValidator, @PathVariable String op) throws Exception;

    /**
     * 根据id删除菜单数据
     */
    @RequestMapping(value = "/roles/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable String id) throws Exception;

}
