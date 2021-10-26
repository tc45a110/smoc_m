package com.smoc.cloud.admin.security.controller;

import com.smoc.cloud.admin.security.remote.service.SysOrgService;
import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

/**
 * 系统组织基本操作
 * 2019/5/16 12:47
 **/
@RestController
@RequestMapping("/sysOrg")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class SysOrgController {

    @Autowired
    private SysOrgService sysOrgService;

    /**
     * 根据父ID 查询组织
     *
     * @param parentId
     * @return
     */
    @RequestMapping(value = "/getSysOrgByParentId/{parentId}", method = RequestMethod.GET)
    public List<Nodes> getSysOrgByParentId(@PathVariable String parentId) {

        ResponseData<List<Nodes>> data = sysOrgService.getSysOrgByParentId(parentId);
        return data.getData();
    }
}
