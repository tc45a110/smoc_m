package com.smoc.cloud.admin.security.remote.client;

import com.smoc.cloud.common.auth.validator.DictValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 字典管理远程服务接口
 * 2019/4/23 16:28
 **/
@FeignClient(name = "smoc-auth", path = "/auth")
public interface DictFeignClient {

    /**
     * 查询列表
     */
    @RequestMapping(value = "/dict/listByDictType/{typeId}/{dictType}", method = RequestMethod.GET)
    ResponseData<List<DictValidator>> listByDictType(@PathVariable(value="typeId") String typeId, @PathVariable(value="dictType") String dictType) throws Exception;

    /**
     * 根据id获取信息
     */
    @RequestMapping(value = "/dict/findById/{id}", method = RequestMethod.GET)
    ResponseData<DictValidator> findById(@PathVariable(value="id") String id) throws Exception;

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/dict/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody DictValidator dictValidator, @PathVariable(value="op") String op) throws Exception;

    /**
     * 根据id删除数据
     */
    @RequestMapping(value = "/dict/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable(value="id") String id) throws Exception;

    /**
     * 分页查询列表
     */
    @RequestMapping(value = "/dict/page", method = RequestMethod.POST)
    ResponseData<PageList<DictValidator>> page(@RequestBody PageParams<DictValidator> dictValidator) throws Exception;

}
