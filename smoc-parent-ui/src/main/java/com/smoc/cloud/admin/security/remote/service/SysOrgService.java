package com.smoc.cloud.admin.security.remote.service;


import com.smoc.cloud.admin.security.remote.client.SysOrgFeignClient;
import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统组织服务
 * 2019/5/12 22:28
 */
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SysOrgService {

    @Autowired
    private SysOrgFeignClient sysOrgFeignClient;

    /**
     * 根据父ID 查询组织
     *
     * @param parentId
     * @return
     */
    public ResponseData<List<Nodes>> getSysOrgByParentId(String parentId) {
        try {
            List<Nodes> data = this.sysOrgFeignClient.getSysOrgByParentId(parentId);
            return ResponseDataUtil.buildSuccess(data);
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
