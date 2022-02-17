package com.smoc.cloud.message.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import com.smoc.cloud.message.repository.MessageDetailInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 短信明细
 */
@Slf4j
@Service
public class MessageDetailInfoService {

    @Resource
    private MessageDetailInfoRepository messageDetailInfoRepository;

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<MessageDetailInfoValidator>> page(PageParams<MessageDetailInfoValidator> pageParams){

        PageList<MessageDetailInfoValidator> page = messageDetailInfoRepository.page(pageParams);

        return ResponseDataUtil.buildSuccess(page);

    }
}
