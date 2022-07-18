package com.smoc.cloud.iot.carrier.remote;

import com.smoc.cloud.common.iot.validator.IotFlowCardsPrimaryInfoValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * 运营商物联网卡
 */
@FeignClient(name = "iot", path = "/iot")
public interface IotFlowCardsPrimaryInfoFeignClient {

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/iot/carrier/cards/page", method = RequestMethod.POST)
    ResponseData<PageList<IotFlowCardsPrimaryInfoValidator>> page(@RequestBody PageParams<IotFlowCardsPrimaryInfoValidator> pageParams) throws Exception;

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/iot/carrier/cards/findById/{id}", method = RequestMethod.GET)
    ResponseData<Map<String, Object>> findById(@PathVariable String id) throws Exception;

    /**
     * 添加、修改
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/iot/carrier/cards/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody Map<String, Object> map, @PathVariable String op) throws Exception;

    /**
     * 注销
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/iot/carrier/cards/forbidden/{id}", method = RequestMethod.GET)
    ResponseData forbidden(@PathVariable String id) throws Exception;
}
