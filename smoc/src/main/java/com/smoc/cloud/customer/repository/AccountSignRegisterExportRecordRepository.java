package com.smoc.cloud.customer.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.validator.AccountSignRegisterExportRecordValidator;
import com.smoc.cloud.customer.entity.AccountSignRegisterExportRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountSignRegisterExportRecordRepository extends JpaRepository<AccountSignRegisterExportRecord, String> {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    PageList<AccountSignRegisterExportRecordValidator> page(PageParams pageParams);

    List<AccountSignRegisterExportRecord> findByRegisterOrderNo(String registerOrderNo);

    @Modifying
    @Query(value = "update account_sign_register_export_record set REGISTER_STATUS=:status where REGISTER_ORDER_NO = :registerOrderNo ", nativeQuery = true)
    void updateRegisterStatusByOrderNo(@Param("registerOrderNo") String registerOrderNo, @Param("status") String status);
}
