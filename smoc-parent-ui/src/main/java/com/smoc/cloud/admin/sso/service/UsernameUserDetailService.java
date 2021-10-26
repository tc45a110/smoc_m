package com.smoc.cloud.admin.sso.service;


import com.smoc.cloud.common.auth.entity.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by fp295 on 2018/11/25.
 */
@Slf4j
@Service
public class UsernameUserDetailService extends BaseUserDetailService {

    @Override
    protected SecurityUser getUser(String username) {
        // 账号密码登陆调用FeignClient根据用户名查询用户
//        ResponseData<BaseUser> baseUserResponseData = baseUserService.getUserByUserName(username);
//        if(baseUserResponseData.getData() == null || !ResponseCode.SUCCESS.getCode().equals(baseUserResponseData.getCode())){
//            logger.error("找不到该用户，用户名：" + username);
//            throw new UsernameNotFoundException("找不到该用户，用户名：" + username);
//        }
        return null;
    }
}
