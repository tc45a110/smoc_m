package com.smoc.cloud.configure.channel.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelPriceHistoryValidator;
import com.smoc.cloud.configure.channel.remote.ConfigChannelPriceHistoryFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConfigChannelPriceHistoryService {

    @Autowired
    private ConfigChannelPriceHistoryFeignClient configChannelPriceHistoryFeignClient;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<ConfigChannelPriceHistoryValidator>> page(PageParams<ConfigChannelPriceHistoryValidator> pageParams) {
        try {
            ResponseData<PageList<ConfigChannelPriceHistoryValidator>> data = this.configChannelPriceHistoryFeignClient.page(pageParams);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
