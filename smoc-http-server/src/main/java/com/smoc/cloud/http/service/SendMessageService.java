package com.smoc.cloud.http.service;

import com.smoc.cloud.common.http.server.message.request.SendMessageByTemplateRequestParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.http.entity.AccountTemplateInfo;
import com.smoc.cloud.http.redis.IdGeneratorFactory;
import com.smoc.cloud.http.repository.AccountTemplateInfoRepository;
import com.smoc.cloud.http.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class SendMessageService {

    @Autowired
    private IdGeneratorFactory idGeneratorFactory;

    @Autowired
    private AccountRateLimiter accountRateLimiter;

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SystemHttpApiRequestService systemHttpApiRequestService;

    @Resource
    private AccountTemplateInfoRepository accountTemplateInfoRepository;

    @Autowired
    private SendMessageAsyncService sendMessageAsyncService;

    /**
     * 发送普通短信
     *
     * @param params
     * @return
     */
    public ResponseData<Map<String, String>> sendMessageByTemplate(SendMessageByTemplateRequestParams params) {

        //判断模版是否存在
        Optional<AccountTemplateInfo> optional = accountTemplateInfoRepository.findAccountTemplateInfoByBusinessAccountAndTemplateIdAndTemplateAgreementType(params.getAccount(), params.getTemplateId(), "HTTP");
        if (!optional.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_TEMPLATE_NOT_EXIST_ERROR.getCode(), ResponseCode.PARAM_TEMPLATE_NOT_EXIST_ERROR.getMessage());
        }
        //判断模板是否通过审核
        if (!"2".equals(optional.get().getTemplateStatus())) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_TEMPLATE_STATUS_ERROR.getCode(), ResponseCode.PARAM_TEMPLATE_STATUS_ERROR.getMessage());
        }

        //判断发送内容非空
        if (null == params.getContent() || params.getContent().length < 1) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_MOBILE_ERROR.getCode(), ResponseCode.PARAM_MOBILE_ERROR.getMessage());
        }

        //批次发送数量
        Integer length = params.getContent().length;
        if (1000 < length) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_MOBILE_NUM_ERROR.getCode(), ResponseCode.PARAM_MOBILE_NUM_ERROR.getMessage());
        }

        //流控、限流
        Boolean limiter = accountRateLimiter.limiter(params.getAccount(), length);
        if (!limiter) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_MOBILE_LIMITER_ERROR.getCode(), ResponseCode.PARAM_MOBILE_LIMITER_ERROR.getMessage());
        }

        AccountTemplateInfo accountTemplateInfo = optional.get();
        sendMessageAsyncService.sendMessageByTemplate(length, params, accountTemplateInfo);
//        log.info("[普通短信]:{}", new Gson().toJson(accountTemplateInfo));
        Map<String, String> result = new HashMap<>();
        result.put("orderNo", params.getOrderNo());
        result.put("templateId", params.getTemplateId());
        result.put("mobiles", length + "");
        return ResponseDataUtil.buildSuccess(result);
    }


    /**
     * 发送多媒体短信
     *
     * @param params
     * @return
     */
    public ResponseData<Map<String, String>> sendMultimediaMessageByTemplate(SendMessageByTemplateRequestParams params) {

        //判断模版是否存在
        Optional<AccountTemplateInfo> optional = accountTemplateInfoRepository.findAccountTemplateInfoByBusinessAccountAndTemplateIdAndTemplateAgreementType(params.getAccount(), params.getTemplateId(), "HTTP");
        if (!optional.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_TEMPLATE_NOT_EXIST_ERROR.getCode(), ResponseCode.PARAM_TEMPLATE_NOT_EXIST_ERROR.getMessage());
        }
        //判断模板是否通过审核
        if (!"2".equals(optional.get().getTemplateStatus())) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_TEMPLATE_STATUS_ERROR.getCode(), ResponseCode.PARAM_TEMPLATE_STATUS_ERROR.getMessage());
        }

        //判断发送内容非空
        if (null == params.getContent() || params.getContent().length < 1) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_MOBILE_ERROR.getCode(), ResponseCode.PARAM_MOBILE_ERROR.getMessage());
        }

        //批次发送数量
        Integer length = params.getContent().length;
        if (1000 < length) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_MOBILE_NUM_ERROR.getCode(), ResponseCode.PARAM_MOBILE_NUM_ERROR.getMessage());
        }

        //流控、限流
        Boolean limiter = accountRateLimiter.limiter(params.getAccount(), length);
        if (!limiter) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_MOBILE_LIMITER_ERROR.getCode(), ResponseCode.PARAM_MOBILE_LIMITER_ERROR.getMessage());
        }

        AccountTemplateInfo accountTemplateInfo = optional.get();
        //msgId
        String messageId = idGeneratorFactory.getTaskId();
        sendMessageAsyncService.sendMultimediaMessageByTemplate(length, params, accountTemplateInfo,messageId);
        Map<String, String> result = new HashMap<>();
        result.put("orderNo", params.getOrderNo());
        result.put("templateId", params.getTemplateId());
        result.put("msgId", messageId);
        result.put("mobiles", length + "");
        return ResponseDataUtil.buildSuccess(result);
    }



    /**
     * 发送国际短信
     *
     * @param params
     * @return
     */
    public ResponseData<Map<String, String>> sendInterMessageByTemplate(SendMessageByTemplateRequestParams params) {

        //判断模版是否存在
        Optional<AccountTemplateInfo> optional = accountTemplateInfoRepository.findAccountTemplateInfoByBusinessAccountAndTemplateIdAndTemplateAgreementType(params.getAccount(), params.getTemplateId(), "HTTP");
        if (!optional.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_TEMPLATE_NOT_EXIST_ERROR.getCode(), ResponseCode.PARAM_TEMPLATE_NOT_EXIST_ERROR.getMessage());
        }
        //判断模板是否通过审核
        if (!"2".equals(optional.get().getTemplateStatus())) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_TEMPLATE_STATUS_ERROR.getCode(), ResponseCode.PARAM_TEMPLATE_STATUS_ERROR.getMessage());
        }

        //判断发送内容非空
        if (null == params.getContent() || params.getContent().length < 1) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_MOBILE_ERROR.getCode(), ResponseCode.PARAM_MOBILE_ERROR.getMessage());
        }


        //批次发送数量
        Integer length = params.getContent().length;
        if (1000 < length) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_MOBILE_NUM_ERROR.getCode(), ResponseCode.PARAM_MOBILE_NUM_ERROR.getMessage());
        }

        //流控、限流
        Boolean limiter = accountRateLimiter.limiter(params.getAccount(), length);
        if (!limiter) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_MOBILE_LIMITER_ERROR.getCode(), ResponseCode.PARAM_MOBILE_LIMITER_ERROR.getMessage());
        }

        AccountTemplateInfo accountTemplateInfo = optional.get();
        sendMessageAsyncService.sendInterMessageByTemplate(length, params, accountTemplateInfo);
//        log.info("[普通短信]:{}", new Gson().toJson(accountTemplateInfo));
        Map<String, String> result = new HashMap<>();
        result.put("orderNo", params.getOrderNo());
        result.put("template", params.getTemplateId());
        result.put("mobiles", length + "");
        return ResponseDataUtil.buildSuccess(result);
    }

}
