package com.smoc.cloud.http.api.international;

import com.google.gson.Gson;
import com.smoc.cloud.common.gateway.utils.ValidatorUtil;
import com.smoc.cloud.common.http.server.message.request.TemplateAddRequestParams;
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
 * 国际短信，模板管理
 */
@Slf4j
@RestController
@RequestMapping("template")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class InterTemplateController {

    @Autowired
    private TemplateService templateService;

    /**
     * 添加模板
     * @param params
     * @return
     */
    @RequestMapping(value = "/addInterTemplate", method = RequestMethod.POST)
    public ResponseData<Map<String, String>> addInterTemplate(@RequestBody TemplateAddRequestParams params) {

        log.info("[创建国际短信模板]：{}", new Gson().toJson(params));

        if (!ValidatorUtil.validate(params)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), ValidatorUtil.validateMessage(params));
        }

        return templateService.addInterTemplate(params);
    }
}
