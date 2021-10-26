package com.smoc.cloud.auth.data.provider.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.auth.data.provider.entity.BaseRoleModule;
import com.smoc.cloud.auth.data.provider.repository.BaseRoleModuleRepository;
import com.smoc.cloud.common.auth.qo.RoleMenus;
import com.smoc.cloud.common.auth.qo.RoleNodes;
import com.smoc.cloud.common.auth.validator.RoleAuthValidator;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.utils.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色授权服务
 */
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RoleAuthService {

    @Resource
    private BaseRoleModuleRepository baseRoleModuleRepository;

    /**
     * 根据角色id 查询系统级别菜单
     *
     * @param roleId 角色id
     * @return
     */
    public ResponseData<List<RoleNodes>> loadSystemMenus(String roleId) {
        List<RoleNodes> nodes = baseRoleModuleRepository.loadSystemMenus(roleId);
        return ResponseDataUtil.buildSuccess(nodes);
    }

    /**
     * 查询 系统功能菜单
     *
     * @param roleId 角色id
     * @param parentId 父id
     * @return
     */
    public ResponseData<List<RoleNodes>> loadMenus(String parentId,String roleId) {
        List<RoleNodes> nodes = baseRoleModuleRepository.loadMenus(parentId,roleId);
        return ResponseDataUtil.buildSuccess(nodes);
    }


    /**
     * 加载角色信息
     * @return
     */
    public ResponseData<List<RoleNodes>> loadRoles(){
        List<RoleNodes> nodes = baseRoleModuleRepository.loadRoles();
        return ResponseDataUtil.buildSuccess(nodes);
    }

    /**
     * 保存角色授权信息
     *
     * @param roleAuthValidator
     * @return
     */
    @Transactional
    public ResponseData save(RoleAuthValidator roleAuthValidator) {

        baseRoleModuleRepository.deleteByRoleId(roleAuthValidator.getRoleId());

        List<BaseRoleModule> roleModules = new ArrayList<>();
        String[] menus = roleAuthValidator.getMenusId().split(",");
        for (int i=0;i<menus.length;i++){
            BaseRoleModule roleModule = new BaseRoleModule();
            roleModule.setId(UUID.uuid32());
            roleModule.setRoleId(roleAuthValidator.getRoleId());
            roleModule.setModuleId(menus[i]);
            roleModules.add(roleModule);
        }

        //记录日志
        log.info("[角色授权][save]数据:{}",JSON.toJSONString(roleModules));
        baseRoleModuleRepository.batchSave(roleModules);

        return  ResponseDataUtil.buildSuccess();
    }

    /**
     * 根据项目标示 的到系统可以操作的菜单与角色关系
     * @param projectName
     */
    public ResponseData<List<RoleMenus>> getOperatingMenus(String projectName){

        List<RoleMenus> data = baseRoleModuleRepository.getOperatingMenus(projectName);

        return  ResponseDataUtil.buildSuccess(data);

    }
}
