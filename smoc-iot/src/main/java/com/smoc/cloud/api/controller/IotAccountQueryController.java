package com.smoc.cloud.api.controller;


import com.smoc.cloud.api.request.BaseRequest;
import com.smoc.cloud.api.response.flow.SimFlowUsedThisMonthResponse;
import com.smoc.cloud.api.service.IotAccountInfoQueryService;
import com.smoc.cloud.common.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 根据账号，查询账号级信息
 */
@Slf4j
@RestController
@RequestMapping("iot/account/info")
public class IotAccountQueryController {

    @Autowired
    private IotAccountInfoQueryService iotAccountInfoQueryService;

    /**
     * 根据账号查询账号拥有的流量套餐信息
     *
     * @param baseRequest
     * @return
     */
    @RequestMapping(value = "/queryAccountPackageInfo", method = RequestMethod.POST)
    public ResponseData<SimFlowUsedThisMonthResponse> queryAccountPackageInfo(@RequestBody BaseRequest baseRequest) {
        return null;
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
