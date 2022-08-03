package com.smoc.cloud.api.controller;


import com.smoc.cloud.api.request.AccountPackageMonthPageRequest;
import com.smoc.cloud.api.request.AccountPackageRequest;
import com.smoc.cloud.api.request.BaseRequest;
import com.smoc.cloud.api.request.PackageCardsPageRequest;
import com.smoc.cloud.api.response.account.IotAccountPackageInfo;
import com.smoc.cloud.api.response.account.IotAccountPackageInfoMonthly;
import com.smoc.cloud.api.response.info.SimBaseInfoResponse;
import com.smoc.cloud.api.service.IotAccountInfoQueryService;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.iot.account.service.IotAccountPackageItemsService;
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
@RequestMapping("package/info")
public class IotPackageQueryController {

    @Autowired
    private IotAccountInfoQueryService iotAccountInfoQueryService;

    @Autowired
    private IotAccountPackageItemsService iotAccountPackageItemsService;

    /**
     * 根据账号查询账号拥有的流量套餐信息(最多100条)
     *
     * @param baseRequest
     * @return
     */
    @RequestMapping(value = "/queryPackages", method = RequestMethod.POST)
    public ResponseData<List<IotAccountPackageInfo>> queryAccountPackages(@RequestBody BaseRequest baseRequest) {
        //完成参数规则验证
        if (!MpmValidatorUtil.validate(baseRequest)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(baseRequest));
        }
        //log.info("[queryAccountPackages]:{}",new Gson().toJson(baseRequest));
        //初始化数据
        PageParams<IotAccountPackageInfo> params = new PageParams<>();
        params.setPageSize(100);
        params.setCurrentPage(1);
        List<IotAccountPackageInfo> list = this.iotAccountInfoQueryService.page(baseRequest.getAccount(), params);
        //log.info("[page]:{}", new Gson().toJson(list));
        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 根据账号、套餐id查询套餐详细
     *
     * @param accountPackageRequest
     * @return
     */
    @RequestMapping(value = "/queryPackageById", method = RequestMethod.POST)
    public ResponseData<IotAccountPackageInfo> queryAccountPackageById(@RequestBody AccountPackageRequest accountPackageRequest) {
        //完成参数规则验证
        if (!MpmValidatorUtil.validate(accountPackageRequest)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(accountPackageRequest));
        }
        //log.info("[queryAccountPackageById]:{}",new Gson().toJson(accountPackageRequest));
        IotAccountPackageInfo iotAccountPackageInfo = this.iotAccountInfoQueryService.queryAccountPackageById(accountPackageRequest.getAccount(), accountPackageRequest.getPackageId());
        if (null == iotAccountPackageInfo) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }
        return ResponseDataUtil.buildSuccess(iotAccountPackageInfo);
    }

    /**
     * 根据账号、套餐id查询套餐包含的物联网卡信息
     *
     * @param packageCardsPageRequest
     * @return
     */
    @RequestMapping(value = "/queryCardsByPackageId", method = RequestMethod.POST)
    public ResponseData<PageList<SimBaseInfoResponse>> queryCardsByPackageId(@RequestBody PackageCardsPageRequest packageCardsPageRequest) {
        //完成参数规则验证
        if (!MpmValidatorUtil.validate(packageCardsPageRequest)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(packageCardsPageRequest));
        }

        //初始化数据
        PageParams pageParams = new PageParams<>();
        pageParams.setPageSize(100);
        if (null == packageCardsPageRequest || null == packageCardsPageRequest.getCurrentPage() || "0".equals(packageCardsPageRequest.getCurrentPage())) {
            pageParams.setCurrentPage(1);
        } else {
            pageParams.setCurrentPage(new Integer(packageCardsPageRequest.getCurrentPage()));
        }
        PageList<SimBaseInfoResponse> page = this.iotAccountPackageItemsService.queryCardsByPackageId(packageCardsPageRequest.getAccount(), packageCardsPageRequest.getPackageId(), pageParams);
        if (null == page || null == page.getList()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }
        return ResponseDataUtil.buildSuccess(page);
    }

    /**
     * 根据用户账号、历史月份、套餐id，查询套餐历史使用情况
     *
     * @param accountPackageMonthPageRequest
     * @return
     */
    @RequestMapping(value = "/queryPackagesByMonth", method = RequestMethod.POST)
    public ResponseData<PageList<IotAccountPackageInfoMonthly>> queryAccountPackagesByMonth(@RequestBody AccountPackageMonthPageRequest accountPackageMonthPageRequest) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(accountPackageMonthPageRequest)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(accountPackageMonthPageRequest));
        }
        //log.info("[queryAccountPackagesByMonth]:{}", new Gson().toJson(accountPackageMonthPageRequest));
        //初始化数据
        PageParams<IotAccountPackageInfo> pageParams = new PageParams<>();
        pageParams.setPageSize(100);
        if (null == accountPackageMonthPageRequest || null == accountPackageMonthPageRequest.getCurrentPage() || "0".equals(accountPackageMonthPageRequest.getCurrentPage())) {
            pageParams.setCurrentPage(1);
        } else {
            pageParams.setCurrentPage(new Integer(accountPackageMonthPageRequest.getCurrentPage()));
        }
        PageList<IotAccountPackageInfoMonthly> page = this.iotAccountInfoQueryService.page(accountPackageMonthPageRequest.getAccount(), accountPackageMonthPageRequest.getQueryMonth(), accountPackageMonthPageRequest.getPackageId(), pageParams);
        return ResponseDataUtil.buildSuccess(page);
    }


}
