package com.smoc.cloud.saler.repository;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.common.smoc.saler.qo.CustomerAccountInfoQo;
import com.smoc.cloud.customer.entity.AccountBasicInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

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

    /**
     * 个账号发送量统计按月
     * @param statisticSendData
     * @return
     */
    List<AccountStatisticSendData> statisticSendNumberMonthByAccount(AccountStatisticSendData statisticSendData);

    /**
     * 根据企业查询账号发送量统计
     * @param statisticSendData
     * @return
     */
    List<AccountStatisticSendData> statisticSendNumberByEnterpriseName(AccountStatisticSendData statisticSendData);
}
