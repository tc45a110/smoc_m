package com.smoc.cloud.iot.packages.repository;

import com.smoc.cloud.api.response.account.IotAccountPackageInfo;
import com.smoc.cloud.api.response.account.IotAccountPackageInfoMonthly;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.iot.packages.entity.IotPackageUsedMonthly;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IotPackageUsedMonthlyRepository extends JpaRepository<IotPackageUsedMonthly, String> {


    /**
     * 根据用户账号、套餐id、月份查询套裁历史使用情况
     *
     * @param account
     * @param packageId
     * @param queryMonth
     * @return
     */
    IotAccountPackageInfoMonthly queryAccountPackageByIdAndMonth(String account, String packageId, String queryMonth);

    /**
     * 根据用户账号、月份查询套裁历史使用情况
     *
     * @param account
     * @param queryMonth
     * @param pageParams
     * @return
     */
    PageList<IotAccountPackageInfoMonthly> page(String account, String queryMonth, PageParams<IotAccountPackageInfo> pageParams);
}
