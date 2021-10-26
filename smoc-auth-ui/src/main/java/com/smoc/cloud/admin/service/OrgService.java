package com.smoc.cloud.admin.service;

import com.smoc.cloud.admin.remote.client.OrgFeignClient;
import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.auth.validator.OrgValidator;
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
 * 角色管理服务
 * 2019/4/23 16:28
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OrgService {

    @Autowired
    private OrgFeignClient orgFeignClient;

    /**
     * 根据父ID 查询组织
     *
     * @param parentId
     * @return
     */
    public ResponseData<List<Nodes>> getOrgByParentId(String parentId) {
        try {
            List<Nodes> data = this.orgFeignClient.getOrgByParentId(parentId);
            return ResponseDataUtil.buildSuccess(data);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据父ID、组织类型查询
     *
     * @param parentId
     * @param orgType  组织类型 0标示组织  1标示部门
     * @return
     */
    public ResponseData<List<OrgValidator>> findByParentIdAndOrgType(String parentId, Integer orgType) {
        try {
            ResponseData<List<OrgValidator>> data = this.orgFeignClient.findByParentIdAndOrgType(parentId, orgType);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据ID 查询
     *
     * @param id
     * @return
     */
    public ResponseData<OrgValidator> findById(String id) {
        try {
            ResponseData data = this.orgFeignClient.findById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    public ResponseData save(OrgValidator orgValidator, String op) {
        try {
            ResponseData data = this.orgFeignClient.save(orgValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id删除菜单数据
     */
    public ResponseData deleteById(String id) {
        try {
            ResponseData data = this.orgFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
