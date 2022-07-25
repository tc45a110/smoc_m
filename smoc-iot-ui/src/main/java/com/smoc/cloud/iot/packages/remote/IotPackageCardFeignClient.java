package com.smoc.cloud.iot.packages.remote;

import com.smoc.cloud.common.iot.validator.IotPackageCardValidator;
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
 * 产品配置的物联网卡
 */
@FeignClient(name = "iot", path = "/iot")
public interface IotPackageCardFeignClient {

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/iot/package/cards/page", method = RequestMethod.POST)
    ResponseData<PageList<IotPackageCardValidator>> page(@RequestBody PageParams<IotPackageCardValidator> pageParams) throws Exception;

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/iot/package/cards/findById/{id}", method = RequestMethod.GET)
    ResponseData<IotPackageCardValidator> findById(@PathVariable String id)  throws Exception;

    /**
     * 添加、修改
     *
     * @return
     */
    @RequestMapping(value = "/iot/package/cards/save", method = RequestMethod.POST)
    ResponseData save(@RequestBody List<IotPackageCardValidator> cards) throws Exception;
}
