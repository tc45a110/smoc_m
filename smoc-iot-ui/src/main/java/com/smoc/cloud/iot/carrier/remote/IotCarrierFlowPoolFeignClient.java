package com.smoc.cloud.iot.carrier.remote;


import com.smoc.cloud.common.iot.validator.IotCarrierFlowPoolValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 运营商流量池
 */
@FeignClient(name = "iot", path = "/iot")
public interface IotCarrierFlowPoolFeignClient {


    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/iot/carrier/pool/page", method = RequestMethod.POST)
    ResponseData<PageList<IotCarrierFlowPoolValidator>> page(@RequestBody PageParams<IotCarrierFlowPoolValidator> pageParams) throws Exception;

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/iot/carrier/pool/findById/{id}", method = RequestMethod.GET)
    ResponseData<IotCarrierFlowPoolValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 添加、修改
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/iot/carrier/pool/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody IotCarrierFlowPoolValidator iotCarrierFlowPoolValidator, @PathVariable String op) throws Exception;

    /**
     * 注销
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/iot/carrier/pool/forbidden/{id}", method = RequestMethod.GET)
    ResponseData forbidden(@PathVariable String id) throws Exception;
}
