package com.smoc.cloud.auth.data.provider.repository;

import com.smoc.cloud.auth.data.provider.entity.SystemUserPersonalParam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 用户个性化参数
 */
public interface SystemUserPersonalParamRepository extends JpaRepository<SystemUserPersonalParam, String> {

    /**
     * 根据用户id查询用户个性化参数
     * @param userId
     * @return
     */
    List<SystemUserPersonalParam> findSystemUserPersonalParamByUserId(String userId);
}