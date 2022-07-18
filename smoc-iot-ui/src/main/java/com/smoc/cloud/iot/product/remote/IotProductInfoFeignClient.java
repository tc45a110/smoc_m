package com.smoc.cloud.iot.product.remote;

import com.smoc.cloud.common.iot.validator.IotProductInfoValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * iot产品管理
 */
@FeignClient(name = "iot", path = "/iot")
public interface IotProductInfoFeignClient {

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/iot/product/page", method = RequestMethod.POST)
    ResponseData<PageList<IotProductInfoValidator>> page(@RequestBody PageParams<IotProductInfoValidator> pageParams) throws Exception;

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/iot/product/findById/{id}", method = RequestMethod.GET)
    ResponseData<IotProductInfoValidator> findById(@PathVariable String id) throws Exception;


    /**
     * 添加、修改
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/iot/product/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody IotProductInfoValidator iotProductInfoValidator, @PathVariable String op) throws Exception;

    /**
     * 注销
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/iot/product/forbidden/{id}", method = RequestMethod.GET)
    ResponseData forbidden(@PathVariable String id) throws Exception;
}
