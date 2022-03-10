package com.smoc.cloud.configure.channel.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelPriceHistoryValidator;
import com.smoc.cloud.configure.channel.repository.ConfigChannelPriceHistoryRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ConfigChannelPriceHistoryService {

    @Resource
    private ConfigChannelPriceHistoryRepository configChannelPriceHistoryRepository;


    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<ConfigChannelPriceHistoryValidator>> page(PageParams<ConfigChannelPriceHistoryValidator> pageParams) {

        PageList<ConfigChannelPriceHistoryValidator> data = configChannelPriceHistoryRepository.page(pageParams);
        return ResponseDataUtil.buildSuccess(data);

    }
}
