package com.smoc.cloud.iot.packages.remote;

import com.smoc.cloud.common.iot.validator.IotFlowCardsPrimaryInfoValidator;
import com.smoc.cloud.common.iot.validator.IotPackageInfoValidator;
import com.smoc.cloud.common.iot.validator.PackageCards;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * iot产品管理
 */
@FeignClient(name = "iot", path = "/iot")
public interface IotPackageInfoFeignClient {

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/iot/package/page", method = RequestMethod.POST)
    ResponseData<PageList<IotPackageInfoValidator>> page(@RequestBody PageParams<IotPackageInfoValidator> pageParams) throws Exception;

    /**
     * 根据产品id查询，产品配置的信息，及未配置的 物联网卡信息
     *
     * @param packageId
     * @return
     */
    @RequestMapping(value = "/iot/package/list/{packageId}", method = RequestMethod.GET)
    ResponseData<List<IotFlowCardsPrimaryInfoValidator>> list(@PathVariable String packageId) throws Exception;


    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/iot/package/findById/{id}", method = RequestMethod.GET)
    ResponseData<IotPackageInfoValidator> findById(@PathVariable String id) throws Exception;


    /**
     * 添加、修改
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/iot/package/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody IotPackageInfoValidator iotPackageInfoValidator, @PathVariable String op) throws Exception;

    /**
     * 添加、修改
     *
     * @return
     */
    @RequestMapping(value = "/iot/package/config/save", method = RequestMethod.POST)
    ResponseData saveConfig(@RequestBody PackageCards packageCards) throws Exception;

    /**
     * 注销
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/iot/package/forbidden/{id}", method = RequestMethod.GET)
    ResponseData forbidden(@PathVariable String id) throws Exception;
}
