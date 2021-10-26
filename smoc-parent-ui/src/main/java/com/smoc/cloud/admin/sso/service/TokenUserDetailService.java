package com.smoc.cloud.admin.sso.service;

import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by fp295 on 2018/11/25.
 */
@Slf4j
@Service
public class TokenUserDetailService extends BaseUserDetailService {

    @Override
    protected SecurityUser getUser(String token) {

        //通过token 获取用户信息
        ResponseData<SecurityUser> securityUserResponseData = oauthTokenService.getUser(token);
        return securityUserResponseData.getData();
    }
}
