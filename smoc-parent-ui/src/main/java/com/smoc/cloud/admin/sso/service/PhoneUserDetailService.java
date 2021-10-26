package com.smoc.cloud.admin.sso.service;

import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by fp295 on 2018/11/25.
 */
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PhoneUserDetailService extends BaseUserDetailService {

//    @Override
    protected SecurityUser getUser(String ca) {
        // 手机验证码调用FeignClient根据电话号码查询用户
        //通过token 获取用户信息
        ResponseData<SecurityUser> securityUserResponseData = oauthTokenService.getUserByCA(ca);
        if(securityUserResponseData.getData() == null || !ResponseCode.SUCCESS.getCode().equals(securityUserResponseData.getCode())){
            log.error("[SSO][用户不存在]数据：{}", ca);
            throw new UsernameNotFoundException("UserNotFound");
        }
        return securityUserResponseData.getData();
    }
}
