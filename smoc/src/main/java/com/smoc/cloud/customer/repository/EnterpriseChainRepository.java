package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseChainInfoValidator;
import com.smoc.cloud.customer.entity.EnterpriseChainInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


/**
 * 签名合同链操作类
 */
public interface EnterpriseChainRepository extends CrudRepository<EnterpriseChainInfo, String>, JpaRepository<EnterpriseChainInfo, String> {


    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    List<EnterpriseChainInfoValidator> page(EnterpriseChainInfoValidator pageParams);


    @Modifying
    @Query(value = "update enterprise_chain_info set SIGN_CHAIN_STATUS = 0 where ID = :id ",nativeQuery = true)
    void updateStatusById(String id);

    List<EnterpriseChainInfo> findByIdAndSignChainStatus(String id, String s);
}
