package com.smoc.cloud.auth.data.provider.service;

import com.smoc.cloud.auth.data.provider.entity.BaseUserRole;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author 武基慧
 * @Description //完成系统模块，资源模块
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BaseUserRoleService {

    /**
     * 保存用户角色
     * @param baseUserRoleList
     */
    @Transactional
    public ResponseData<List<BaseUserRole>> saveUserRole(List<BaseUserRole> baseUserRoleList) {
        if (baseUserRoleList.size() > 0 && !StringUtils.isEmpty(baseUserRoleList.get(0).getRoleId())) {
            BaseUserRole userRole = new BaseUserRole();
            userRole.setUserId(baseUserRoleList.get(0).getUserId());

        }

        return ResponseDataUtil.buildSuccess();
    }

    public ResponseData<BaseUserRole> findById(String id) {
        return null;
    }

    public ResponseData<BaseUserRole> save(BaseUserRole entity) {
        return null;
    }

    public ResponseData<BaseUserRole> update(BaseUserRole entity) {
        return null;
    }

    public ResponseData<BaseUserRole> deleteById(String id) {
        return null;
    }

    public boolean exists(String id) {
        return false;
    }
}
