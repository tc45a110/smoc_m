package com.smoc.cloud.intellect.callback.controller;

import com.google.gson.Gson;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.intelligence.IntellectCallbackShowReportValidator;
import com.smoc.cloud.common.smoc.intelligence.IntellectCallbackTemplateStatusReportValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.intellect.callback.request.PushMessageShowReport;
import com.smoc.cloud.intellect.callback.request.PushMessageShowReportItem;
import com.smoc.cloud.intellect.callback.request.PushTemplateStatusReport;
import com.smoc.cloud.intellect.callback.request.PushTemplateStatusReportItem;
import com.smoc.cloud.intellect.callback.service.IntellectCallbackShowReportService;
import com.smoc.cloud.intellect.callback.service.IntellectCallbackTemplateStatusReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 接受回执报告
 */
@Slf4j
@RestController
@RequestMapping("/AimService/v1")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class IntelligenceController {

    @Autowired
    private IntellectCallbackShowReportService intellectCallbackShowReportService;

    @Autowired
    private IntellectCallbackTemplateStatusReportService intellectCallbackTemplateStatusReportService;

    /**
     * 统推送智能短信模板审核状态回执报告
     *
     * @return
     */
    @RequestMapping(value = "/TemplateManage/aimTplAuditRpt", method = RequestMethod.POST)
    public Map<String, Object> aimTplAuditRpt(@RequestBody PushTemplateStatusReport pushTemplateStatusReport, HttpServletRequest request) {

        log.info("[模板审核状态回执]:{}", new Gson().toJson(pushTemplateStatusReport));
        PushTemplateStatusReportItem item = pushTemplateStatusReport.getMsgBody();

        IntellectCallbackTemplateStatusReportValidator validator = new IntellectCallbackTemplateStatusReportValidator();
        validator.setId(UUID.uuid32());
        validator.setOrderNo(pushTemplateStatusReport.getCustId());
        validator.setTplId(item.getTplId());
        validator.setBizId(item.getBizId());
        validator.setBizFlag(item.getBizFlag());
        validator.setTplState(item.getTplState());
        validator.setAuditState(item.getAuditState());
        validator.setAuditDesc(item.getAuditDesc());
        validator.setFactoryInfoList(new Gson().toJson(item.getFactoryInfoList()));
        validator.setState(item.getTplState());
        validator.setCreatedBy("CALLBACK");
        validator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));

        Map<String, Object> result = new HashMap<>();
        ResponseData responseData = intellectCallbackTemplateStatusReportService.save(validator);
        if (!ResponseCode.SUCCESS.getCode().equals(responseData.getCode())) {
            result.put("subCode", 1);
            result.put("message", responseData.getMessage());
            return result;
        }

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

        log.info("[终端展示状态回执]:{}", new Gson().toJson(pushMessageShowReport));
        PushMessageShowReportItem item = pushMessageShowReport.getMsgBody();

        IntellectCallbackShowReportValidator validator = new IntellectCallbackShowReportValidator();
        validator.setId(UUID.uuid32());
        validator.setOrderNo(pushMessageShowReport.getCustId());
        validator.setTplId(item.getTplId());
        validator.setCustFlag(item.getCustFlag());
        validator.setCustId(item.getCustId());
        validator.setAimUrl(item.getAimUrl());
        validator.setAimCode(item.getAimCode());
        validator.setExtData(item.getExtData());
        validator.setStatus(item.getStatus());
        validator.setDescribe(item.getDescribe());
        validator.setCreatedBy("CALLBACK");
        validator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));

        Map<String, Object> result = new HashMap<>();
        ResponseData responseData = intellectCallbackShowReportService.save(validator);
        if (!ResponseCode.SUCCESS.getCode().equals(responseData.getCode())) {
            result.put("subCode", 1);
            result.put("message", responseData.getMessage());
            return result;
        }

        result.put("subCode", 0);
        result.put("message", "成功");
        return result;

    }
}
