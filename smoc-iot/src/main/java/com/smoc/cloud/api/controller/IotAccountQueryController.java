package com.smoc.cloud.api.controller;


import com.google.gson.Gson;
import com.smoc.cloud.api.request.AccountPackageMonthRequest;
import com.smoc.cloud.api.request.AccountPackageRequest;
import com.smoc.cloud.api.request.BaseRequest;
import com.smoc.cloud.api.response.account.IotAccountPackageInfo;
import com.smoc.cloud.api.response.account.IotAccountPackageInfoMonthly;
import com.smoc.cloud.api.response.flow.SimFlowUsedThisMonthResponse;
import com.smoc.cloud.api.service.IotAccountInfoQueryService;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 根据账号，查询账号级信息
 */
@Slf4j
@RestController
@RequestMapping("account/info")
public class IotAccountQueryController {

    @Autowired
    private IotAccountInfoQueryService iotAccountInfoQueryService;

    /**
     * 根据账号查询账号拥有的流量套餐信息(最多100条)
     *
     * @param baseRequest
     * @return
     */
    @RequestMapping(value = "/queryAccountPackages", method = RequestMethod.POST)
    public ResponseData<List<IotAccountPackageInfo>> queryAccountPackages(@RequestBody BaseRequest baseRequest) {

        //log.info("[queryAccountPackages]:{}",new Gson().toJson(baseRequest));
        //初始化数据
        PageParams<IotAccountPackageInfo> params = new PageParams<>();
        params.setPageSize(100);
        params.setCurrentPage(1);
        List<IotAccountPackageInfo> list = this.iotAccountInfoQueryService.page(baseRequest.getAccount(), params);
        log.info("[page]:{}", new Gson().toJson(list));
        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 根据账号、套餐id查询套餐信息
     *
     * @param accountPackageRequest
     * @return
     */
    @RequestMapping(value = "/queryAccountPackageById", method = RequestMethod.POST)
    public ResponseData<IotAccountPackageInfo> queryAccountPackageById(@RequestBody AccountPackageRequest accountPackageRequest) {
        //log.info("[queryAccountPackageById]:{}",new Gson().toJson(accountPackageRequest));
        IotAccountPackageInfo iotAccountPackageInfo = this.iotAccountInfoQueryService.queryAccountPackageById(accountPackageRequest.getAccount(), accountPackageRequest.getPackageId());
        return ResponseDataUtil.buildSuccess(iotAccountPackageInfo);
    }

    /**
     * 根据账号、套餐id查询套餐历史月份套餐信息
     *
     * @param accountPackageMonthRequest
     * @return
     */
    @RequestMapping(value = "/queryAccountPackageByIdAndMonth", method = RequestMethod.POST)
    public ResponseData<IotAccountPackageInfoMonthly> queryAccountPackageByIdAndMonth(@RequestBody AccountPackageMonthRequest accountPackageMonthRequest) {
        log.info("[queryAccountPackageByIdAndMonth]:{}", new Gson().toJson(accountPackageMonthRequest));
        IotAccountPackageInfoMonthly iotAccountPackageInfoMonthly = this.iotAccountInfoQueryService.queryAccountPackageByIdAndMonth(accountPackageMonthRequest.getAccount(), accountPackageMonthRequest.getPackageId(), accountPackageMonthRequest.getQueryMonth());
        return ResponseDataUtil.buildSuccess(iotAccountPackageInfoMonthly);
    }

    /**
     * 根据账号、月份查询套餐历史月份套餐信息
     *
     * @param accountPackageMonthRequest
     * @return
     */
    @RequestMapping(value = "/queryAccountPackagesByMonth", method = RequestMethod.POST)
    public ResponseData<List<IotAccountPackageInfoMonthly>> queryAccountPackagesByMonth(@RequestBody AccountPackageMonthRequest accountPackageMonthRequest) {
        log.info("[queryAccountPackagesByMonth]:{}", new Gson().toJson(accountPackageMonthRequest));
        //初始化数据
        PageParams<IotAccountPackageInfo> params = new PageParams<>();
        params.setPageSize(100);
        params.setCurrentPage(1);
        List<IotAccountPackageInfoMonthly> list = this.iotAccountInfoQueryService.page(accountPackageMonthRequest.getAccount(), accountPackageMonthRequest.getQueryMonth(), params);
        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 根据套餐id查询本月流量累计使用量
     *
     * @param baseRequest
     * @return
     */
    @RequestMapping(value = "/queryPackageUsedThisMonth", method = RequestMethod.POST)
    public ResponseData<SimFlowUsedThisMonthResponse> queryPackageUsedThisMonth(@RequestBody BaseRequest baseRequest) {
        return null;
    }

    /**
     * 根据套餐id查询
     *
     * @param baseRequest
     * @return
     */
    @RequestMapping(value = "/queryPackageUsedMonthly", method = RequestMethod.POST)
    public ResponseData<SimFlowUsedThisMonthResponse> queryPackageUsedMonthly(@RequestBody BaseRequest baseRequest) {
        return null;
    }

}
