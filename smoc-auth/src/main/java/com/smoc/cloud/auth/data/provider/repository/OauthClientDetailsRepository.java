package com.smoc.cloud.auth.data.provider.repository;

import com.smoc.cloud.auth.data.provider.entity.OauthClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * client数据操作类
 * 2019/3/29 14:29
 */
public interface OauthClientDetailsRepository extends CrudRepository<OauthClientDetails, String>, JpaRepository<OauthClientDetails, String> {

    /**
     * 修改客户端密码
     *
     * @param id
     * @param secret
     */
    void resetClientSecret(String id, String secret);

}
