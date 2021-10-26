package com.smoc.cloud.admin.service;

import com.smoc.cloud.admin.remote.client.MenusFeignClient;
import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.auth.validator.MenusValidator;
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
 * 菜单管理服务
 * 2019/4/23 16:28
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MenusService {

    @Autowired
    private MenusFeignClient menusFeignClient;

    /**
     * 根据父id获取系统菜单
     */
    public ResponseData<List<Nodes>> getMenusByParentId(String parentId) {
        try {
            ResponseData<List<Nodes>> data = this.menusFeignClient.getMenusByParentId(parentId);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 菜单选择 查询根目录
     */
    public ResponseData<List<Nodes>> getRootByProjectName(String projectName) {
        try {
            ResponseData<List<Nodes>> data = this.menusFeignClient.getRootByProjectName(projectName);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据父id查询所有 子菜单
     */
    public ResponseData<List<Nodes>> getAllSubMenusByParentId(String parentId) {
        try {
            ResponseData<List<Nodes>> data = this.menusFeignClient.getAllSubMenusByParentId(parentId);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据父id获取菜单列表信息
     */
    public ResponseData<List<MenusValidator>> list(String parentId) {
        try {
            ResponseData<List<MenusValidator>> data = this.menusFeignClient.list(parentId);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id获取菜单信息
     */
    public ResponseData<MenusValidator> findById(String id) {
        try {
            ResponseData<MenusValidator> data = this.menusFeignClient.findById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 保存、修改系统数据
     * op 是类型 表示了保存或修改
     */
    public ResponseData save(MenusValidator menusValidator, String op) {
        try {
            ResponseData data = this.menusFeignClient.save(menusValidator, op);
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
            ResponseData data = this.menusFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }

    }

}
