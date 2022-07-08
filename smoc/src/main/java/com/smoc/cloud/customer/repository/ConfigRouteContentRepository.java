package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.qo.AccountContentRepairQo;
import com.smoc.cloud.common.smoc.customer.validator.ConfigRouteContentRuleValidator;
import com.smoc.cloud.customer.entity.ConfigRouteContentRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 内容路由操作类
 */
public interface ConfigRouteContentRepository extends CrudRepository<ConfigRouteContentRule, String>, JpaRepository<ConfigRouteContentRule, String> {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    PageList<ConfigRouteContentRuleValidator> page(PageParams<ConfigRouteContentRuleValidator> pageParams);

    @Modifying
    @Query(value = "update config_route_content_rule set ROUTE_STATUS = 0 where ID =:id ",nativeQuery = true)
    void updateStatusById(@Param("id") String id);

    /**
     * 业务账号列表
     * @param pageParams
     * @return
     */
    PageList<AccountContentRepairQo> accountList(PageParams<AccountContentRepairQo> pageParams);

    List<ConfigRouteContentRule> findByAccountIdAndCarrierAndRouteContentAndRouteStatus(String accountId, String carrier, String routeContent, String status);

    ConfigRouteContentRule findByAccountIdAndCarrier(String accountId, String carrier);
}
