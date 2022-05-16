package com.smoc.cloud.intellect.remote.controller;

import com.google.gson.Gson;
import com.smoc.cloud.intellect.remote.request.PushMessageShowReport;
import com.smoc.cloud.intellect.remote.request.PushTemplateStatusReport;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 接受回执报告
 */
@Slf4j
@RestController
@RequestMapping("/AimService/v1")
public class IntelligenceController {

    /**
     * 统推送智能短信模板审核状态回执报告
     *
     * @return
     */
    @RequestMapping(value = "/TemplateManage/aimTplAuditRpt", method = RequestMethod.POST)
    public Map<String, Object> aimTplAuditRpt(@RequestBody PushTemplateStatusReport pushTemplateStatusReport, HttpServletRequest request) {

        log.info("[模板审核状态回执]:{}", new Gson().toJson(pushTemplateStatusReport));
        Map<String, Object> result = new HashMap<>();
        result.put("subCode", 0);
        result.put("message", "成功");
        return result;

    }

    /**
     * 推送智能短信终端展示状态回执
     *
     * @return
     */
    @RequestMapping(value = "/AimManage/aimMsgShowRpt", method = RequestMethod.POST)
    public Map<String, Object> aimMsgShowRpt(@RequestBody PushMessageShowReport pushMessageShowReport, HttpServletRequest request) {

        log.info("[模板审核状态回执]:{}", new Gson().toJson(pushMessageShowReport));
        Map<String, Object> result = new HashMap<>();
        result.put("subCode", 0);
        result.put("message", "成功");
        return result;

    }
}
