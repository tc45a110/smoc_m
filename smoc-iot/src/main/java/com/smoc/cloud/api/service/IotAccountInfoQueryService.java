package com.smoc.cloud.api.service;


import com.smoc.cloud.api.response.account.IotAccountPackageInfo;
import com.smoc.cloud.api.response.account.IotAccountPackageInfoMonthly;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.iot.packages.repository.IotPackageInfoRepository;
import com.smoc.cloud.iot.packages.repository.IotPackageUsedMonthlyRepository;
import com.smoc.cloud.iot.repository.ApiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class IotAccountInfoQueryService {

    @Autowired
    private IotPackageInfoRepository iotPackageInfoRepository;

    @Autowired
    private IotPackageUsedMonthlyRepository iotPackageUsedMonthlyRepository;

    /**
     * 根据用户账号分页查询账号套餐
     *
     * @param account
     * @return
     */
    public List<IotAccountPackageInfo> page(String account, PageParams<IotAccountPackageInfo> pageParams) {
        PageList<IotAccountPackageInfo> page = iotPackageInfoRepository.page(account, pageParams);
        return page.getList();
    }

    /**
     * 根据账号、套餐id查询套餐信息
     *
     * @param account
     * @param packageId
     * @return
     */
    public IotAccountPackageInfo queryAccountPackageById(String account, String packageId) {
        IotAccountPackageInfo iotAccountPackageInfo = this.iotPackageInfoRepository.queryAccountPackageById(account, packageId);
        return iotAccountPackageInfo;
    }


    /**
     * 根据用户账号、历史月份、套裁id，查询套餐历史使用情况
     *
     * @param account
     * @param queryMonth
     * @param packageId
     * @param pageParams
     * @return
     */
    public PageList<IotAccountPackageInfoMonthly> page(String account, String queryMonth, String packageId, PageParams pageParams){
        PageList<IotAccountPackageInfoMonthly> page = this.iotPackageUsedMonthlyRepository.page(account, queryMonth,packageId,pageParams);
        return page;
    }

    /**
     * 根据用户账号、套餐id、月份查询套裁历史使用情况
     *
     * @param account
     * @param packageId
     * @param queryMonth
     * @return
     */
    public IotAccountPackageInfoMonthly queryAccountPackageByIdAndMonth(String account, String packageId, String queryMonth) {
        IotAccountPackageInfoMonthly iotAccountPackageInfoMonthly = this.iotPackageUsedMonthlyRepository.queryAccountPackageByIdAndMonth(account, packageId, queryMonth);
        return iotAccountPackageInfoMonthly;
    }

}
