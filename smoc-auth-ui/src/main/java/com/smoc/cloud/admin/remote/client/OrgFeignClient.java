package com.smoc.cloud.admin.remote.client;

import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.auth.validator.OrgValidator;
import com.smoc.cloud.common.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 组织管理远程服务接口
 * 2019/4/23 16:28
 **/
@FeignClient(name = "smoc-auth", path = "/auth")
public interface OrgFeignClient {

    /**
     * 根据父ID 查询组织
     *
     * @param parentId
     * @return
     */
    @RequestMapping(value = "/org/getOrgByParentId/{parentId}", method = RequestMethod.GET)
    List<Nodes> getOrgByParentId(@PathVariable String parentId) throws Exception;

    /**
     * 根据父ID、组织类型查询
     *
     * @param parentId
     * @param orgType  组织类型 0标示组织  1标示部门
     * @return
     */
    @RequestMapping(value = "/org/findByParentIdAndOrgType/{parentId}/{orgType}", method = RequestMethod.GET)
    ResponseData<List<OrgValidator>> findByParentIdAndOrgType(@PathVariable String parentId, @PathVariable Integer orgType) throws Exception;

    /**
     * 根据ID 查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/org/findById/{id}", method = RequestMethod.GET)
    ResponseData<OrgValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 添加、修改
     *
     * @param orgValidator
     * @return
     */
    @RequestMapping(value = "/org/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody OrgValidator orgValidator, @PathVariable String op) throws Exception;

    /**
     * 根据ID 删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/org/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable String id) throws Exception;

}
