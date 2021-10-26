package com.smoc.cloud.admin.service;

import com.smoc.cloud.admin.remote.client.UsersFeignClient;
import com.smoc.cloud.common.auth.qo.Users;
import com.smoc.cloud.common.auth.validator.UserPasswordValidator;
import com.smoc.cloud.common.auth.validator.UserValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

/**
 * 用户管理服务
 * 2019/4/23 16:28
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UsersService {


    @Autowired
    private UsersFeignClient usersFeignClient;

    /**
     * 查询列表
     */
    public ResponseData<PageList<Users>> page(PageParams<Users> pageParams) {

        try {
            PageList<Users> pageList = this.usersFeignClient.page(pageParams);
            return ResponseDataUtil.buildSuccess(pageList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id获取信息
     */
    public ResponseData<UserValidator> findById(String id) {

        try {
            ResponseData<UserValidator> responseData = this.usersFeignClient.findById(id);
            return responseData;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    public ResponseData save(UserValidator userValidator, String op) {

        try {
            ResponseData responseData = this.usersFeignClient.save(userValidator, op);
            return responseData;
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
            ResponseData responseData = this.usersFeignClient.deleteById(id);
            return responseData;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }

    }

    /**
     * 重置用户密码
     *
     * @param userPasswordValidator
     * @return
     */
    public ResponseData resetPassword(UserPasswordValidator userPasswordValidator) {
        try {
            ResponseData responseData = this.usersFeignClient.resetPassword(userPasswordValidator);
            return responseData;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 禁用、启用用户
     *
     * @param id
     * @return
     */
    public ResponseData forbiddenUser(String id, Integer status) {
        try {
            ResponseData responseData = this.usersFeignClient.forbiddenUser(id, status);
            return responseData;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }


}
