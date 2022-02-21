package com.smoc.cloud.message.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.message.MessageDailyStatisticValidator;
import com.smoc.cloud.common.smoc.template.MessageWebTaskInfoValidator;
import com.smoc.cloud.message.remote.MessageWebTaskInfoFeignClient;
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
    private MessageWebTaskInfoFeignClient messageWebTaskInfoFeignClient;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<MessageWebTaskInfoValidator>> page(PageParams<MessageWebTaskInfoValidator> pageParams) {

        try {

            ResponseData<PageList<MessageWebTaskInfoValidator>> page = messageWebTaskInfoFeignClient.page(pageParams);
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
    public ResponseData<Map<String, Object>> count(MessageWebTaskInfoValidator qo) {
        try {
            ResponseData<Map<String, Object>> count = messageWebTaskInfoFeignClient.count(qo);
            return count;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
