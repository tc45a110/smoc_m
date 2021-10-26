package com.smoc.cloud.admin.service;

import com.smoc.cloud.admin.remote.client.RoleAuthFeignClient;
import com.smoc.cloud.common.auth.qo.RoleNodes;
import com.smoc.cloud.common.auth.validator.RoleAuthValidator;
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
 * 角色授权服务
 */
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RoleAuthService {

    @Autowired
    private RoleAuthFeignClient roleAuthFeignClient;

    /**
     * 根据角色id 查询系统级别菜单
     *
     * @param roleId 角色id
     * @return
     */
    public ResponseData<List<RoleNodes>> loadSystemMenus(String roleId) {
        try {
            ResponseData<List<RoleNodes>> data = this.roleAuthFeignClient.loadSystemMenus(roleId);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 查询 系统功能菜单
     *
     * @param roleId   角色id
     * @param parentId 父id
     * @return
     */
    public ResponseData<List<RoleNodes>> loadMenus(String parentId, String roleId) {
        try {
            ResponseData<List<RoleNodes>> data = this.roleAuthFeignClient.loadMenus(parentId, roleId);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 加载角色信息
     *
     * @return
     */
    public ResponseData<List<RoleNodes>> loadRoles() {
        try {
            ResponseData<List<RoleNodes>> data = this.roleAuthFeignClient.loadRoles();
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 保存角色授权信息
     *
     * @param roleAuthValidator
     * @return
     */
    public ResponseData save(RoleAuthValidator roleAuthValidator) {
        try {
            ResponseData data = this.roleAuthFeignClient.save(roleAuthValidator);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
