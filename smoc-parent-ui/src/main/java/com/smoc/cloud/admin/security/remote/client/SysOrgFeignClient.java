package com.smoc.cloud.admin.security.remote.client;

import com.smoc.cloud.common.auth.qo.Nodes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


/**
 * 组织管理远程服务接口
 * 2019/4/23 16:28
 **/
@FeignClient(name = "smoc-auth",path = "/auth")
public interface SysOrgFeignClient {


    /**
     * 根据父ID 查询组织
     *
     * @param parentId
     * @return
     */
    @RequestMapping(value = "/sysOrg/getSysOrgByParentId/{parentId}", method = RequestMethod.GET)
    List<Nodes> getSysOrgByParentId(@PathVariable(value="parentId") String parentId) throws Exception;


}
