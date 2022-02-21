package com.smoc.cloud.message.controller;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.template.MessageWebTaskInfoValidator;
import com.smoc.cloud.message.service.MessageWebTaskInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * web任务单
 */
@Slf4j
@RestController
@RequestMapping("message/web/task")
public class MessageWebTaskInfoController {

    @Autowired
    private MessageWebTaskInfoService messageWebTaskInfoService;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseData<PageList<MessageWebTaskInfoValidator>> page(@RequestBody PageParams<MessageWebTaskInfoValidator> pageParams) {

        return messageWebTaskInfoService.page(pageParams);
    }

    /**
     * 统计发送数量
     *
     * @param qo
     * @return
     */
    @RequestMapping(value = "/count", method = RequestMethod.POST)
    public ResponseData<Map<String, Object>> count(@RequestBody MessageWebTaskInfoValidator qo) {

        return messageWebTaskInfoService.countSum(qo);
    }
}
