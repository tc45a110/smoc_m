package com.smoc.cloud.customer.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.validator.AccountSignRegisterValidator;
import com.smoc.cloud.customer.entity.AccountSignRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountSignRegisterRepository extends JpaRepository<AccountSignRegister, String> {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    PageList<AccountSignRegisterValidator> page(PageParams<AccountSignRegisterValidator> pageParams);

    List<AccountSignRegister> findByAccountAndSignExtendNumber(String account, String signExtendNumber);

    /**
     * 注销、启用
     *
     * @param id
     * @param status
     */
    @Modifying
    @Query(value = "update account_sign_register set REGISTER_STATUS = :status where ID = :id", nativeQuery = true)
    void delete(@Param("id") String id, @Param("status") String status);
}
