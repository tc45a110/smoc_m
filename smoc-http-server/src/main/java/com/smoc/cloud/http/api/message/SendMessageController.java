package com.smoc.cloud.http.api.message;

import com.google.gson.Gson;
import com.smoc.cloud.common.gateway.utils.ValidatorUtil;
import com.smoc.cloud.common.http.server.message.request.SendMessageByTemplateRequestParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.http.service.SendMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

/**
 * 根据模板发送普通消息
 */
@Slf4j
@RestController
@RequestMapping("message")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class SendMessageController {

    @Autowired
    private SendMessageService sendMessageService;

    /**
     * 根据模板发送普通消息
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/sendMessageByTemplate", method = RequestMethod.POST)
    public ResponseData<Map<String, String>> sendMessageByTemplate(@RequestBody SendMessageByTemplateRequestParams params) {

        log.info("[发送普通模板短信]：{}", new Gson().toJson(params));

        if (!ValidatorUtil.validate(params)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), ValidatorUtil.validateMessage(params));
        }

        return sendMessageService.sendMessageByTemplate(params);
    }
}
