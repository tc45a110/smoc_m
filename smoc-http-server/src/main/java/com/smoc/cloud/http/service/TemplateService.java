package com.smoc.cloud.http.service;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.smoc.cloud.common.gateway.utils.ValidatorUtil;
import com.smoc.cloud.common.http.server.message.request.TemplateAddRequestParams;
import com.smoc.cloud.common.http.server.message.request.TemplateStatusRequestParams;
import com.smoc.cloud.common.http.server.multimedia.request.MultimediaTemplateAddParams;
import com.smoc.cloud.common.http.server.multimedia.request.MultimediaTemplateModel;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.template.AccountTemplateInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.http.entity.AccountTemplateInfo;
import com.smoc.cloud.http.repository.AccountTemplateInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class TemplateService {

    @Autowired
    private SequenceService sequenceService;

    @Resource
    private AccountTemplateInfoRepository accountTemplateInfoRepository;

    public ResponseData<Map<String, String>> addTemplate(TemplateAddRequestParams params) {

        //可以处理前提逻辑
        //获取模板ID
        String templateId = "TEMP" + sequenceService.findSequence("TEMPLATE");

        AccountTemplateInfo entity = new AccountTemplateInfo();
        entity.setTemplateId(templateId);
        entity.setBusinessAccount(params.getAccount());
        entity.setTemplateType("TEXT_SMS");
        entity.setTemplateAgreementType("HTTP");


        entity.setTemplateStatus("2");
        entity.setCheckBy("API");
        entity.setCreatedTime(DateTimeUtils.getNowDateTime());

        //组织响应结果
        Map<String, String> result = new HashMap<>();
        result.put("orderNo", params.getOrderNo());
        result.put("templateId", templateId);
        return ResponseDataUtil.buildSuccess(result);

    }

    public ResponseData<Map<String, String>> addMultimediaTemplate(MultimediaTemplateAddParams params) {

        //获取模板ID
        String templateId = "TEMP" + sequenceService.findSequence("TEMPLATE");

        List<MultimediaTemplateModel> items = params.getItems();
        if (null == items || items.size() < 1) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR);
        }

        for (MultimediaTemplateModel model : items) {
            if (!ValidatorUtil.validate(model)) {
                return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), ValidatorUtil.validateMessage(model));
            }
        }

        //组织响应结果
        Map<String, String> result = new HashMap<>();
        result.put("orderNo", params.getOrderNo());
        result.put("templateId", templateId);
        return ResponseDataUtil.buildSuccess(result);
    }

    public ResponseData<Map<String, String>> addInterTemplate(TemplateAddRequestParams params) {

        //获取模板ID
        String templateId = "TEMP" + sequenceService.findSequence("TEMPLATE");

        //组织响应结果
        Map<String, String> result = new HashMap<>();
        result.put("orderNo", params.getOrderNo());
        result.put("templateId", templateId);
        return ResponseDataUtil.buildSuccess(result);
    }

    public ResponseData<Map<String, String>> getTemplateStatus(TemplateStatusRequestParams params) {

        //可以处理前提逻辑
        //获取模板ID
        log.info("[获取普通状态]：{}", new Gson().toJson(params));

        //组织响应结果
        Map<String, String> result = new HashMap<>();
        result.put("orderNo", params.getOrderNo());
        result.put("templateId", params.getTemplateId());
        result.put("templateStatus", "0");
        result.put("statusDesc", "等待审核");
        return ResponseDataUtil.buildSuccess(result);

    }

    /**
     * 保存或修改
     *
     * @param entity
     * @return
     */
    @Async
    @Transactional
    public void save(AccountTemplateInfo entity) {
        //记录日志
        log.info("[模板管理][API接口添加模板]数据:{}", JSON.toJSONString(entity));
        accountTemplateInfoRepository.saveAndFlush(entity);
    }
}
