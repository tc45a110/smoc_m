package com.smoc.cloud.message.service;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import com.smoc.cloud.message.remote.MessageDetailInfoFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

    public ResponseData<PageList<MessageDetailInfoValidator>> tableStorePage(PageParams<MessageDetailInfoValidator> params) {
        try {
            ResponseData<PageList<MessageDetailInfoValidator>> page = messageDetailInfoFeignClient.tableStorePage(params);
            return page;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
