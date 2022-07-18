package com.smoc.cloud.iot.carrier.remote;

import com.smoc.cloud.common.iot.validator.IotFlowCardsChangeHistoryValidator;
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
 * 物联网卡变更历史
 */
@FeignClient(name = "iot", path = "/iot")
public interface IotFlowCardsChangeHistoryFeignClient {

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/iot/carrier/card/history/page", method = RequestMethod.POST)
    ResponseData<PageList<IotFlowCardsChangeHistoryValidator>> page(@RequestBody PageParams<IotFlowCardsChangeHistoryValidator> pageParams) throws Exception;

    /**
     * 添加、修改
     *
     * @return
     */
    @RequestMapping(value = "/iot/carrier/card/history/save/{msisdn}/{imsi}/{iccid}", method = RequestMethod.POST)
    ResponseData save(@RequestBody List<IotFlowCardsChangeHistoryValidator> histories, @PathVariable String msisdn, @PathVariable String imsi, @PathVariable String iccid) throws Exception;
}
