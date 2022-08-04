package com.smoc.cloud.iot.packages.remote;

import com.smoc.cloud.common.iot.reponse.IotAccountPackageInfoMonthly;
import com.smoc.cloud.common.iot.request.AccountPackageMonthPageRequest;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "iot", path = "/iot")
public interface PackageFlowFeignClient {

    /**
     * 根据用户账号、历史月份、套餐id，查询套餐历史使用情况
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/package/flow/monthly/page", method = RequestMethod.POST)
    ResponseData<PageList<IotAccountPackageInfoMonthly>> page(@RequestBody PageParams<AccountPackageMonthPageRequest> pageParams) throws Exception;
}
