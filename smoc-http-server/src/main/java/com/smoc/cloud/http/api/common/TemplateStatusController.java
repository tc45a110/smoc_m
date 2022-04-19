package com.smoc.cloud.http.api.common;

import com.google.gson.Gson;
import com.smoc.cloud.common.gateway.utils.ValidatorUtil;
import com.smoc.cloud.common.http.server.message.request.TemplateStatusRequestParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.http.service.TemplateService;
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
 * 模板状态查询
 */
@Slf4j
@RestController
@RequestMapping("template")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class TemplateStatusController {


    @Autowired
    private TemplateService templateService;

    /**
     * 查询模板状态
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/getTemplateStatus", method = RequestMethod.POST)
    public ResponseData<Map<String, String>> getTemplateStatus(@RequestBody TemplateStatusRequestParams params) {

        log.info("[获取普通短信模板状态]：{}", new Gson().toJson(params));

        if (!ValidatorUtil.validate(params)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), ValidatorUtil.validateMessage(params));
        }

        return templateService.getTemplateStatus(params);
    }
}
