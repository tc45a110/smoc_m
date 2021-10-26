package com.smoc.cloud.admin.security.remote.service;


import com.smoc.cloud.admin.security.remote.client.SysUserFeignClient;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.Users;
import com.smoc.cloud.common.auth.validator.UserPasswordValidator;
import com.smoc.cloud.common.auth.validator.UserValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 系统用户服务
 * 2019/5/12 22:28
 */
@Slf4j
@Service
public class SysUserService {

    @Autowired
    private SysUserFeignClient sysUserFeignClient;

    /**
     * 获取用户属性
     */
    public ResponseData<UserValidator> userProfile(String id) {
        try {

            ResponseData<UserValidator> data = sysUserFeignClient.userProfile(id);
            return data;
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 重置密码
     *
     * @param userPasswordValidator
     * @return
     */
    public ResponseData resetOwnPassword(UserPasswordValidator userPasswordValidator) {

        try {
            ResponseData<UserValidator> data = sysUserFeignClient.resetOwnPassword(userPasswordValidator);
            return data;
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据组织id查询用户信息
     *
     * @param orgId
     * @return
     */
    public ResponseData<List<SecurityUser>> findSecurityUserByOrgId(@PathVariable("orgId") String orgId) {

        try {
            ResponseData<List<SecurityUser>> data = sysUserFeignClient.findSecurityUserByOrgId(orgId);
            return data;
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 分页查询用户信息
     * @param pageParams
     * @return
     */
    public PageList<Users> page(PageParams<Users> pageParams) {

        try {
            PageList<Users> data = sysUserFeignClient.page(pageParams);
            return data;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }

    }

}
