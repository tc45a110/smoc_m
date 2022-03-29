package com.smoc.cloud.message.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.message.MessageMoInfoValidator;
import com.smoc.cloud.message.service.MessagMoInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

/**
 * 短信上行
 */
@Slf4j
@RestController
@RequestMapping("message/mo")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class MessageMoInfoController {

    @Autowired
    private MessagMoInfoService messagMoInfoService;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseData<PageList<MessageMoInfoValidator>> page(@RequestBody PageParams<MessageMoInfoValidator> pageParams) {

        return messagMoInfoService.page(pageParams);
    }

}
