package com.smoc.cloud.message.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.message.MessageHttpsTaskInfoValidator;
import com.smoc.cloud.common.smoc.message.model.StatisticMessageSendData;
import com.smoc.cloud.message.entity.MessageHttpsTaskInfo;
import com.smoc.cloud.message.repository.MessageHttpsTaskInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * HTTPS任务单
 */
@Slf4j
@Service
public class MessageHttpTaskInfoService {

    @Resource
    private MessageHttpsTaskInfoRepository messageHttpsTaskInfoRepository;


    /**
     * 查询自服务http列表
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<MessageHttpsTaskInfoValidator>> page(PageParams<MessageHttpsTaskInfoValidator> pageParams) {
        PageList<MessageHttpsTaskInfoValidator> page = messageHttpsTaskInfoRepository.page(pageParams);
        return ResponseDataUtil.buildSuccess(page);
    }

    /**
     * 自服务平台不同维度统计发送量
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<StatisticMessageSendData>> messageSendNumberList(PageParams<StatisticMessageSendData> pageParams) {
        PageList<StatisticMessageSendData> page = messageHttpsTaskInfoRepository.messageSendNumberList(pageParams);
        return ResponseDataUtil.buildSuccess(page);
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData findById(String id) {
        Optional<MessageHttpsTaskInfo> data = messageHttpsTaskInfoRepository.findById(id);

        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        MessageHttpsTaskInfo entity = data.get();
        MessageHttpsTaskInfoValidator messageHttpsTaskInfoValidator = new MessageHttpsTaskInfoValidator();
        BeanUtils.copyProperties(entity, messageHttpsTaskInfoValidator);

        return ResponseDataUtil.buildSuccess(messageHttpsTaskInfoValidator);
    }
}
