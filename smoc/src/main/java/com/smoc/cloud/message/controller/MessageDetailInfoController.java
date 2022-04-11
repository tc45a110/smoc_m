package com.smoc.cloud.message.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import com.smoc.cloud.common.smoc.message.model.MessageTaskDetail;
import com.smoc.cloud.common.smoc.message.model.StatisticMessageSendData;
import com.smoc.cloud.message.service.MessageDetailInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

/**
 * 短信明细
 */
@Slf4j
@RestController
@RequestMapping("message/detail")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class MessageDetailInfoController {

    @Autowired
    private MessageDetailInfoService messageDetailInfoService;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseData<PageList<MessageDetailInfoValidator>> page(@RequestBody PageParams<MessageDetailInfoValidator> pageParams) {

        return messageDetailInfoService.page(pageParams);
    }

    /**
     * 统计自服务平台短信明细列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/servicerPage", method = RequestMethod.POST)
    public ResponseData<PageList<MessageDetailInfoValidator>> servicerPage(@RequestBody PageParams<MessageDetailInfoValidator> pageParams) {

        return messageDetailInfoService.servicerPage(pageParams);
    }

    /**
     * 查询自服务web短信明细列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/web/webTaskDetailList", method = RequestMethod.POST)
    public ResponseData<PageList<MessageTaskDetail>> webTaskDetailList(@RequestBody PageParams<MessageTaskDetail> pageParams) {

        return messageDetailInfoService.webTaskDetailList(pageParams);
    }

    /**
     * 查询自服务http短信明细列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/http/httpTaskDetailList", method = RequestMethod.POST)
    public ResponseData<PageList<MessageTaskDetail>> httpTaskDetailList(@RequestBody PageParams<MessageTaskDetail> pageParams) {

        return messageDetailInfoService.httpTaskDetailList(pageParams);
    }

    /**
     * 自服务平台单条短信发送记录
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/sendMessageList", method = RequestMethod.POST)
    public ResponseData<PageList<MessageDetailInfoValidator>> sendMessageList(@RequestBody PageParams<MessageDetailInfoValidator> pageParams) {

        return messageDetailInfoService.sendMessageList(pageParams);
    }

}
