package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseContractInfoValidator;
import com.smoc.cloud.customer.entity.EnterpriseContractInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * 企业合同操作类
 */
public interface EnterpriseContractRepository extends CrudRepository<EnterpriseContractInfo, String>, JpaRepository<EnterpriseContractInfo, String> {


    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    PageList<EnterpriseContractInfoValidator> page(PageParams<EnterpriseContractInfoValidator> pageParams);

    /**
     * 查重
     * @param enterpriseId
     * @param contractNo
     * @param status
     * @return
     */
    List<EnterpriseContractInfo> findByEnterpriseIdAndContractNoAndContractStatus(String enterpriseId, String contractNo, String status);

    //更新状态
    @Modifying
    @Query(value = "update enterprise_contract_info set CONTRACT_STATUS = 0 where ID = :id ",nativeQuery = true)
    void updateStatusById(@Param("id") String id);
}
