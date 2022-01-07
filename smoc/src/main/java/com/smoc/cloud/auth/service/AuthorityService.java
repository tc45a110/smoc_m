package com.smoc.cloud.auth.service;

import com.smoc.cloud.auth.remote.AuthorityFeignClient;
import com.smoc.cloud.common.auth.validator.OrgValidator;
import com.smoc.cloud.common.auth.validator.UserPasswordValidator;
import com.smoc.cloud.common.auth.validator.UserValidator;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统管理服务
 * 2020/5/19 20:22
 **/
@Slf4j
@Service
public class AuthorityService {

    @Autowired
    private AuthorityFeignClient authorityFeignClient;

    /**
     * 组织机构保存、修改数据
     * op 是类型 表示了保存或修改
     */
    public ResponseData saveOrg(OrgValidator orgValidator, String op) {
        try {
            ResponseData data = this.authorityFeignClient.saveOrg(orgValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 用户保存、修改数据
     * op 是类型 表示了保存或修改
     */
    public ResponseData saveUser(UserValidator userValidator, String op) {

        try {
            ResponseData responseData = this.authorityFeignClient.saveUser(userValidator, op);
            return responseData;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }


    /**
     * 根据ID查询用户信息
     */
    public ResponseData findById(String id) {

        try {
            ResponseData responseData = this.authorityFeignClient.findById(id);
            return responseData;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 用户重置密码
     * @param userPasswordValidator
     * @return
     */
    public ResponseData resetPassword(UserPasswordValidator userPasswordValidator) {

        try {
            ResponseData responseData = this.authorityFeignClient.resetPassword(userPasswordValidator);
            return responseData;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 注销、启用用户
     * @param id
     * @param status
     * @return
     */
    public ResponseData forbiddenUser(String id,String status) {

        try {
            ResponseData responseData = this.authorityFeignClient.forbiddenUser(id,status);
            return responseData;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
