package com.smoc.cloud.message.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.message.MessageMoInfoValidator;
import com.smoc.cloud.message.repository.MessageMoInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 短信上行
 */
@Slf4j
@Service
public class MessagMoInfoService {

    @Resource
    private MessageMoInfoRepository messageMoInfoRepository;

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<MessageMoInfoValidator>> page(PageParams<MessageMoInfoValidator> pageParams){

        PageList<MessageMoInfoValidator> page = messageMoInfoRepository.page(pageParams);

        return ResponseDataUtil.buildSuccess(page);

    }

}
