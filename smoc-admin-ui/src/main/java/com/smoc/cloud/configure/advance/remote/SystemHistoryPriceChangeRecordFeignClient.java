package com.smoc.cloud.configure.advance.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.SystemHistoryPriceChangeRecordValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "smoc", path = "/smoc")
public interface SystemHistoryPriceChangeRecordFeignClient {

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/configure/price/history/page", method = RequestMethod.POST)
    ResponseData<PageList<SystemHistoryPriceChangeRecordValidator>> page(@RequestBody PageParams<SystemHistoryPriceChangeRecordValidator> pageParams) throws Exception;

    /**
     * 历史价格调整
     * @param validators
     * @param changeType
     * @return
     */
    @RequestMapping(value = "/configure/price/history/save/{changeType}/{taskType}", method = RequestMethod.POST)
    ResponseData save(@RequestBody List<SystemHistoryPriceChangeRecordValidator> validators, @PathVariable String changeType,@PathVariable String taskType) throws Exception;
}
