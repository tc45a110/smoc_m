package com.smoc.cloud.reconciliation.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.reconciliation.ReconciliationPeriodValidator;
import com.smoc.cloud.reconciliation.entity.ReconciliationPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ReconciliationPeriodRepository extends JpaRepository<ReconciliationPeriod, String> {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    PageList<ReconciliationPeriodValidator> page(PageParams<ReconciliationPeriodValidator> pageParams);

    /**
     * 创建业务账号对账
     */
    void buildAccountPeriod(ReconciliationPeriodValidator validator,String uuid);

    /**
     * 查询近5个月账期
     */
    List<String> findAccountPeriod();

    /**
     * 删除账期
     */
    void deleteAccountPeriod(String id);
}
