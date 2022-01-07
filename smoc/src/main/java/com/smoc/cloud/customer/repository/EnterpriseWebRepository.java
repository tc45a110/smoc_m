package com.smoc.cloud.customer.repository;


import com.smoc.cloud.customer.entity.EnterpriseWebAccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * 企业WEB登录操作类
 */
public interface EnterpriseWebRepository extends CrudRepository<EnterpriseWebAccountInfo, String>, JpaRepository<EnterpriseWebAccountInfo, String> {


    Iterable<EnterpriseWebAccountInfo> findByWebLoginName(String webLoginName);
}
