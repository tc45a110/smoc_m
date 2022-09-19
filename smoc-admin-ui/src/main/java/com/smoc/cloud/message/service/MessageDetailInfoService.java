package com.smoc.cloud.message.service;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.message.MessageCodeValidator;
import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import com.smoc.cloud.common.smoc.message.TableStoreMessageDetailInfoValidator;
import com.smoc.cloud.message.remote.MessageDetailInfoFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * 短信明细
 */
@Slf4j
@Service
public class MessageDetailInfoService {

    @Autowired
    private MessageDetailInfoFeignClient messageDetailInfoFeignClient;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<MessageDetailInfoValidator>> page(PageParams<MessageDetailInfoValidator> pageParams) {
        try {
            ResponseData<PageList<MessageDetailInfoValidator>> page = messageDetailInfoFeignClient.page(pageParams);
            return page;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    public ResponseData<PageList<TableStoreMessageDetailInfoValidator>> tableStorePage(PageParams<TableStoreMessageDetailInfoValidator> params) {
        try {
            ResponseData<PageList<TableStoreMessageDetailInfoValidator>> page = messageDetailInfoFeignClient.tableStorePage(params);
            return page;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据企业实时查询发送总量
     * @param messageDetailInfoValidator
     * @return
     */
    public ResponseData<Map<String, Object>> statisticEnterpriseSendMessage(MessageDetailInfoValidator messageDetailInfoValidator) {
        try {
            ResponseData<Map<String, Object>> page = messageDetailInfoFeignClient.statisticEnterpriseSendMessage(messageDetailInfoValidator);
            return page;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 消息状态码统计查询
     * @param params
     * @return
     */
    public ResponseData<PageList<MessageCodeValidator>> messageCcodeStautsList(PageParams<MessageCodeValidator> params) {
        try {
            ResponseData<PageList<MessageCodeValidator>> page = messageDetailInfoFeignClient.messageCcodeStautsList(params);
            return page;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
