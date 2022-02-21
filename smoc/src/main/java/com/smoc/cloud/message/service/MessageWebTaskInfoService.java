package com.smoc.cloud.message.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.template.MessageWebTaskInfoValidator;
import com.smoc.cloud.message.repository.MessageWebTaskInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * web任务单
 */
@Slf4j
@Service
public class MessageWebTaskInfoService {

    @Resource
    private MessageWebTaskInfoRepository messageWebTaskInfoRepository;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<MessageWebTaskInfoValidator>> page(PageParams<MessageWebTaskInfoValidator> pageParams) {

        PageList<MessageWebTaskInfoValidator> page = messageWebTaskInfoRepository.page(pageParams);

        return ResponseDataUtil.buildSuccess(page);

    }

    /**
     * 统计发送数量
     *
     * @param qo
     * @return
     */
    public ResponseData<Map<String, Object>> countSum(MessageWebTaskInfoValidator qo){

        Map<String, Object> map = messageWebTaskInfoRepository.countSum(qo);
        return ResponseDataUtil.buildSuccess(map);
    }
}
