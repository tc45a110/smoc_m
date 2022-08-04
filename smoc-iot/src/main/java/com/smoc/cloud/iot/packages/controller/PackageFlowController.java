package com.smoc.cloud.iot.packages.controller;


import com.smoc.cloud.api.request.AccountPackageMonthPageRequest;
import com.smoc.cloud.api.response.account.IotAccountPackageInfoMonthly;
import com.smoc.cloud.api.service.IotAccountInfoQueryService;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 套餐流量查询
 */
@Slf4j
@RestController
@RequestMapping("package/flow")
public class PackageFlowController {

    @Autowired
    private IotAccountInfoQueryService iotAccountInfoQueryService;

    /**
     * 根据用户账号、历史月份、套餐id，查询套餐历史使用情况
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/monthly/page", method = RequestMethod.POST)
    public ResponseData<PageList<IotAccountPackageInfoMonthly>> page(@RequestBody PageParams<AccountPackageMonthPageRequest> pageParams) {

        AccountPackageMonthPageRequest accountPackageMonthPageRequest = pageParams.getParams();
        PageList<IotAccountPackageInfoMonthly> page = this.iotAccountInfoQueryService.page(accountPackageMonthPageRequest.getAccount(), accountPackageMonthPageRequest.getQueryMonth(), accountPackageMonthPageRequest.getPackageId(), pageParams);
        return ResponseDataUtil.buildSuccess(page);
    }


}
