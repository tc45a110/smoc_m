package com.smoc.cloud.message.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.message.MessageDailyStatisticValidator;
import com.smoc.cloud.common.smoc.query.model.AccountSendStatisticItemsModel;
import com.smoc.cloud.common.smoc.query.model.ChannelSendStatisticModel;
import com.smoc.cloud.message.repository.MessageDailyStatisticRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 短信日统计
 */
@Slf4j
@Service
public class MessageDailyStatisticService {

    @Resource
    private MessageDailyStatisticRepository messageDailyStatisticRepository;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<MessageDailyStatisticValidator>> page(PageParams<MessageDailyStatisticValidator> pageParams) {

        PageList<MessageDailyStatisticValidator> page = messageDailyStatisticRepository.page(pageParams);

        return ResponseDataUtil.buildSuccess(page);

    }

    /**
     * 统计发送数量
     *
     * @param qo
     * @return
     */
    public ResponseData<Map<String, Object>> countSum(MessageDailyStatisticValidator qo) {

        Map<String, Object> map = messageDailyStatisticRepository.countSum(qo);
        return ResponseDataUtil.buildSuccess(map);

    }

    /**
     * 查询通道发送量
     * @param params
     * @return
     */
    public ResponseData<PageList<ChannelSendStatisticModel>> queryChannelSendStatistics(PageParams<ChannelSendStatisticModel> params) {
        PageList<ChannelSendStatisticModel> page = messageDailyStatisticRepository.queryChannelSendStatistics(params);

        return ResponseDataUtil.buildSuccess(page);
    }

    /**
     * 查询通道下面账号发送量
     * @param params
     * @return
     */
    public ResponseData<PageList<AccountSendStatisticItemsModel>> accountMessageSendListByChannel(PageParams<AccountSendStatisticItemsModel> params) {
        PageList<AccountSendStatisticItemsModel> page = messageDailyStatisticRepository.accountMessageSendListByChannel(params);

        return ResponseDataUtil.buildSuccess(page);
    }

    /**
     * 通道发送量统计
     * @param qo
     * @return
     */
    public ResponseData<Map<String, Object>> channelSendCountSum(MessageDailyStatisticValidator qo) {
        Map<String, Object> map = messageDailyStatisticRepository.channelSendCountSum(qo);
        return ResponseDataUtil.buildSuccess(map);
    }
}
