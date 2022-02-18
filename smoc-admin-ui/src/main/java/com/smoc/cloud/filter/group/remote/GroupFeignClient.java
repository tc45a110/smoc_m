package com.smoc.cloud.filter.group.remote;

import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.filter.FilterGroupListValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


/**
 * 群组管理远程服务接口
 * 2019/4/23 16:28
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface GroupFeignClient {

    /**
     * 根据ID 查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/filter/group/findById/{id}", method = RequestMethod.GET)
    ResponseData<FilterGroupListValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 添加、修改
     *
     * @param groupValidator
     * @return
     */
    @RequestMapping(value = "/filter/group/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody FilterGroupListValidator groupValidator, @PathVariable String op) throws Exception;

    /**
     * 根据ID 删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/group/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable String id) throws Exception;

    /**
     * 根据父id查询群组
     * @param parentId
     * @return
     */
    @RequestMapping(value = "/filter/group/findByParentId/{enterprise}/{parentId}", method = RequestMethod.GET)
    ResponseData<List<FilterGroupListValidator>> findByParentId(@PathVariable String enterprise, @PathVariable String parentId);

    /**
     * 根据父ID查询tree
     * @param parentId
     * @return
     */
    @RequestMapping(value = "/filter/group/getGroupByParentId/{enterprise}/{parentId}", method = RequestMethod.GET)
    ResponseData<List<Nodes>> getGroupByParentId(@PathVariable String enterprise, @PathVariable String parentId);

}
