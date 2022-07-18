package com.smoc.cloud.iot.carrier.remote;


import com.smoc.cloud.common.iot.validator.IotCarrierInfoValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 运营商接入
 */
@FeignClient(name = "iot", path = "/iot")
public interface IotCarrierInfoFeignClient {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/iot/carrier/page", method = RequestMethod.POST)
    ResponseData<PageList<IotCarrierInfoValidator>> page(@RequestBody PageParams<IotCarrierInfoValidator> pageParams) throws Exception;

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/iot/carrier/findById/{id}", method = RequestMethod.GET)
    ResponseData<IotCarrierInfoValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 添加、修改
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/iot/carrier/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody IotCarrierInfoValidator iotCarrierInfoValidator, @PathVariable String op) throws Exception;

    /**
     * 注销
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/iot/carrier/forbidden/{id}", method = RequestMethod.GET)
    ResponseData forbidden(@PathVariable String id) throws Exception;
}
