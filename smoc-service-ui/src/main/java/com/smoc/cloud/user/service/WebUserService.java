package com.smoc.cloud.user.service;


import com.smoc.cloud.common.auth.validator.UserPasswordValidator;
import com.smoc.cloud.common.auth.validator.UserValidator;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.user.remote.WebUserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统用户服务
 * 2019/5/12 22:28
 */
@Slf4j
@Service
public class WebUserService {

    @Autowired
    private WebUserFeignClient webUserFeignClient;

    /**
     * 重置密码
     *
     * @param userPasswordValidator
     * @return
     */
    public ResponseData resetOwnPassword(UserPasswordValidator userPasswordValidator) {

        try {
            ResponseData<UserValidator> data = webUserFeignClient.resetOwnPassword(userPasswordValidator);
            return data;
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
