package com.smoc.cloud.customer.repository;


import com.smoc.cloud.customer.entity.AccountInterfaceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * 账号接口操作类
 */
public interface AccountInterfaceRepository extends CrudRepository<AccountInterfaceInfo, String>, JpaRepository<AccountInterfaceInfo, String> {




}
