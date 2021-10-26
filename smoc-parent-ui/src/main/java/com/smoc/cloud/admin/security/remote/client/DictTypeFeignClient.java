package com.smoc.cloud.admin.security.remote.client;

import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.auth.validator.DictTypeValidator;
import com.smoc.cloud.common.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 字典类型管理远程服务接口
 * 2019/4/23 16:28
 **/
@FeignClient(name = "smoc-auth", path = "/auth")
public interface DictTypeFeignClient {

    /**
     * 查询列表
     */
    @RequestMapping(value = "/dictType/list", method = RequestMethod.GET)
    ResponseData<List<DictTypeValidator>> list() throws Exception;

    /**
     * 查询
     *
     * @param
     */
    @RequestMapping(value = "/dictType/getDictTree", method = RequestMethod.GET)
    List<Nodes> getDictTree() throws Exception;

    /**
     * 查询
     *
     * @param
     */
    @RequestMapping(value = "/dictType/getDictTree/{projectName}", method = RequestMethod.GET)
    List<Nodes> getDictTree(@PathVariable(value="projectName") String projectName) throws Exception;

    /**
     * 根据id获取信息
     */
    @RequestMapping(value = "/dictType/findById/{id}", method = RequestMethod.GET)
    ResponseData<DictTypeValidator> findById(@PathVariable(value="id") String id) throws Exception;

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/dictType/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody DictTypeValidator dictTypeValidator, @PathVariable(value="op") String op) throws Exception;

    /**
     * 根据id删除数据
     */
    @RequestMapping(value = "/dictType/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable(value="id") String id) throws Exception;


}
