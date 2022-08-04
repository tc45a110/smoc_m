package com.smoc.cloud.iot.packages.service;

import com.smoc.cloud.common.iot.reponse.IotAccountPackageInfoMonthly;
import com.smoc.cloud.common.iot.request.AccountPackageMonthPageRequest;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.iot.packages.remote.PackageFlowFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PackageFlowService {

    @Autowired
    private PackageFlowFeignClient packageFlowFeignClient;

    /**
     * 根据用户账号、历史月份、套餐id，查询套餐历史使用情况
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<IotAccountPackageInfoMonthly>> page( PageParams<AccountPackageMonthPageRequest> pageParams){
        try {
            ResponseData<PageList<IotAccountPackageInfoMonthly>> data = this.packageFlowFeignClient.page(pageParams);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
