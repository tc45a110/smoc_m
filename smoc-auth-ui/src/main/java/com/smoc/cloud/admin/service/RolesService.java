package com.smoc.cloud.admin.service;

import com.smoc.cloud.admin.remote.client.RolesFeignClient;
import com.smoc.cloud.common.auth.validator.RoleValidator;
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
public class RolesService {

    @Autowired
    private RolesFeignClient rolesFeignClient;

    /**
     * 查询列表
     */
    public ResponseData<List<RoleValidator>> list() {
        try {
            ResponseData<List<RoleValidator>> data = this.rolesFeignClient.list();
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id获取信息
     */
    public ResponseData<RoleValidator> findById(String id) {
        try {
            ResponseData<RoleValidator> data = this.rolesFeignClient.findById(id);
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
    public ResponseData save(RoleValidator roleValidator, String op) {
        try {
            ResponseData data = this.rolesFeignClient.save(roleValidator, op);
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
            ResponseData data = this.rolesFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
