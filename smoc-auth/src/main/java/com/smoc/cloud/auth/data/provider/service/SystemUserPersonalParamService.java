package com.smoc.cloud.auth.data.provider.service;

import com.smoc.cloud.auth.data.provider.entity.SystemUserPersonalParam;
import com.smoc.cloud.auth.data.provider.repository.SystemUserPersonalParamRepository;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;

import javax.annotation.Resource;
import java.util.List;

public class SystemUserPersonalParamService {

    @Resource
    private SystemUserPersonalParamRepository systemUserPersonalParamRepository;

    /**
     * 根据用户id查询用户个性化参数
     * @param userId
     * @return
     */
    public ResponseData findSystemUserPersonalParamByUserId(String userId){

        List<SystemUserPersonalParam> data = systemUserPersonalParamRepository.findSystemUserPersonalParamByUserId(userId);
        return ResponseDataUtil.buildSuccess(data);

    }
}
