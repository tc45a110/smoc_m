package com.smoc.cloud.message.service;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.message.MessageDailyStatisticValidator;
import com.smoc.cloud.common.smoc.query.model.AccountSendStatisticItemsModel;
import com.smoc.cloud.common.smoc.query.model.ChannelSendStatisticModel;
import com.smoc.cloud.message.remote.MessageDailyStatisticFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 短信日统计
 */
@Slf4j
@Service
public class MessageDailyStatisticService {

    @Autowired
    private MessageDailyStatisticFeignClient messageDailyStatisticFeignClient;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<MessageDailyStatisticValidator>> page(PageParams<MessageDailyStatisticValidator> pageParams) {
        try {
            ResponseData<PageList<MessageDailyStatisticValidator>> page = messageDailyStatisticFeignClient.page(pageParams);
            return page;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 统计发送数量
     *
     * @param qo
     * @return
     */
    public ResponseData<Map<String, Object>> count(MessageDailyStatisticValidator qo) {
        try {
            ResponseData<Map<String, Object>> count = messageDailyStatisticFeignClient.count(qo);
            return count;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 查询通道发送量
     * @param params
     * @return
     */
    public ResponseData<PageList<ChannelSendStatisticModel>> queryChannelSendStatistics(PageParams<ChannelSendStatisticModel> params) {
        try {
            ResponseData<PageList<ChannelSendStatisticModel>> page = messageDailyStatisticFeignClient.queryChannelSendStatistics(params);
            return page;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 查询通道下面账号发送量
     * @param params
     * @return
     */
    public ResponseData<PageList<AccountSendStatisticItemsModel>> accountMessageSendListByChannel(PageParams<AccountSendStatisticItemsModel> params) {
        try {
            ResponseData<PageList<AccountSendStatisticItemsModel>> page = messageDailyStatisticFeignClient.accountMessageSendListByChannel(params);
            return page;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
