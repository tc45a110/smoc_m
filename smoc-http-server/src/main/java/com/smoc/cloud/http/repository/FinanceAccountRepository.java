package com.smoc.cloud.http.repository;

import com.smoc.cloud.http.entity.FinanceAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * 账号操作类
 */
public interface FinanceAccountRepository extends CrudRepository<FinanceAccount, String>, JpaRepository<FinanceAccount, String> {

}
