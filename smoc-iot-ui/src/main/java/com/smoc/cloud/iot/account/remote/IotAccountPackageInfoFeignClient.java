package com.smoc.cloud.iot.account.remote;


import com.smoc.cloud.common.iot.validator.AccountPackage;
import com.smoc.cloud.common.iot.validator.IotAccountPackageItemsValidator;
import com.smoc.cloud.common.iot.validator.IotFlowCardsPrimaryInfoValidator;
import com.smoc.cloud.common.iot.validator.IotPackageInfoValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "iot", path = "/iot")
public interface IotAccountPackageInfoFeignClient {

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/iot/account/package/page", method = RequestMethod.POST)
    ResponseData<PageList<IotAccountPackageItemsValidator>> page(@RequestBody PageParams<IotAccountPackageItemsValidator> pageParams) throws Exception;

    /**
     * 查询账号配置得套餐
     *
     * @param account
     * @return
     */
    @RequestMapping(value = "/iot/account/package/listAccountPackages/{account}", method = RequestMethod.GET)
    ResponseData<List<IotPackageInfoValidator>> listAccountPackages(@PathVariable String account) throws Exception;


    /**
     * 根据套餐id，查询套餐绑定的物联网卡
     *
     * @param packageId
     * @return
     */
    @RequestMapping(value = "/iot/package/cards/listCardsByPackageId/{account}/{packageId}", method = RequestMethod.GET)
    ResponseData<List<IotFlowCardsPrimaryInfoValidator>> listCardsByPackageId(@PathVariable String account,@PathVariable String packageId)  throws Exception;

    /**
     * 查询列表
     *
     * @param account
     * @return
     */
    @RequestMapping(value = "/iot/account/package/list/{account}", method = RequestMethod.GET)
    ResponseData<List<IotPackageInfoValidator>> list(@PathVariable String account) throws Exception;

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/iot/account/package/findById/{id}", method = RequestMethod.GET)
    ResponseData<IotAccountPackageItemsValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 添加、修改
     *
     * @return
     */
    @RequestMapping(value = "/iot/account/package/save", method = RequestMethod.POST)
    ResponseData save(@RequestBody AccountPackage accountPackage) throws Exception;

}
