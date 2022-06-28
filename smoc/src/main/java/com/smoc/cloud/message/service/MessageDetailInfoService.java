package com.smoc.cloud.message.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import com.smoc.cloud.common.smoc.message.model.MessageTaskDetail;
import com.smoc.cloud.common.smoc.message.model.StatisticMessageSendData;
import com.smoc.cloud.message.repository.MessageDetailInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    /**
     * 统计自服务平台短信明细列表
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<MessageDetailInfoValidator>> servicerPage(PageParams<MessageDetailInfoValidator> pageParams) {
        PageList<MessageDetailInfoValidator> page = messageDetailInfoRepository.servicerPage(pageParams);

        return ResponseDataUtil.buildSuccess(page);
    }

    /**
     * 查询自服务web短信明细列表
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<MessageTaskDetail>> webTaskDetailList(PageParams<MessageTaskDetail> pageParams) {
        PageList<MessageTaskDetail> page = messageDetailInfoRepository.webTaskDetailList(pageParams);
        return ResponseDataUtil.buildSuccess(page);
    }

    /**
     * 查询自服务http短信明细列表
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<MessageTaskDetail>> httpTaskDetailList(PageParams<MessageTaskDetail> pageParams) {
        PageList<MessageTaskDetail> page = messageDetailInfoRepository.httpTaskDetailList(pageParams);
        return ResponseDataUtil.buildSuccess(page);
    }

    /**
     * 单条短信发送记录
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<MessageDetailInfoValidator>> sendMessageList(PageParams<MessageDetailInfoValidator> pageParams) {
        PageList<MessageDetailInfoValidator> page = messageDetailInfoRepository.sendMessageList(pageParams);
        return ResponseDataUtil.buildSuccess(page);
    }

    public ResponseData<PageList<MessageDetailInfoValidator>> tableStorePage(PageParams<MessageDetailInfoValidator> pageParams) {
        PageList<MessageDetailInfoValidator> page = messageDetailInfoRepository.tableStorePage(pageParams);

        return ResponseDataUtil.buildSuccess(page);
    }
}
