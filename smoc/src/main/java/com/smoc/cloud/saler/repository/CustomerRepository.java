package com.smoc.cloud.saler.repository;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.saler.qo.CustomerAccountInfoQo;
import com.smoc.cloud.customer.entity.AccountBasicInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 客户账号操作类
 */
public interface CustomerRepository extends JpaRepository<AccountBasicInfo, String> {

    /**
     * 查询-分页
     *
     * @param pageParams
     * @return
     */
    PageList<CustomerAccountInfoQo> page(PageParams<CustomerAccountInfoQo> pageParams);

}
