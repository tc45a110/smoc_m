package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.qo.AccountContentRepairQo;
import com.smoc.cloud.common.smoc.customer.validator.ConfigContentRepairRuleValidator;
import com.smoc.cloud.customer.entity.ConfigContentRepairRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 内容失败补发操作类
 */
public interface ConfigContentRepairRepository extends CrudRepository<ConfigContentRepairRule, String>, JpaRepository<ConfigContentRepairRule, String> {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    PageList<ConfigContentRepairRuleValidator> page(PageParams<ConfigContentRepairRuleValidator> pageParams);

    @Modifying
    @Query(value = "update config_content_repair_rule set REPAIR_STATUS = 0 where ID =:id ",nativeQuery = true)
    void updateStatusById(@Param("id") String id);

    /**
     * 业务账号列表
     * @param pageParams
     * @return
     */
    PageList<AccountContentRepairQo> accountList(PageParams<AccountContentRepairQo> pageParams);

    List<ConfigContentRepairRule> findByAccountIdAndCarrierAndRepairContentAndRepairStatus(String accountId, String carrier, String repairContent, String status);
}
