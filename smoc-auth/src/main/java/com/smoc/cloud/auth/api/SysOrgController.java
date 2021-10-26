package com.smoc.cloud.auth.api;


import com.smoc.cloud.auth.data.provider.service.BaseOrganizationService;
import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

/**
 * 组织管理接口
 * 2019/4/16 20:26
 **/
@Slf4j
@RestController
@RequestMapping("/sysOrg")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class SysOrgController {

    @Autowired
    private BaseOrganizationService baseOrganizationService;

    /**
     * 根据父ID 查询组织
     *
     * @param parentId
     * @return
     */
    @RequestMapping(value = "/getSysOrgByParentId/{parentId}", method = RequestMethod.GET)
    public List<Nodes> getSysOrgByParentId(@PathVariable String parentId) {

        ResponseData<List<Nodes>> data = baseOrganizationService.getOrgByParentId(parentId);
        return data.getData();
    }

}
