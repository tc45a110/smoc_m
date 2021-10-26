package com.smoc.cloud.auth.data.provider.repository;

import com.smoc.cloud.auth.data.provider.entity.BaseUserExtends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * 用户扩展数据操作类
 * 2019/3/29 14:29
 */
public interface BaseUserExtendsRepository extends CrudRepository<BaseUserExtends, String>, JpaRepository<BaseUserExtends, String> {

    BaseUserExtends findByCode(String parentCode);
}
