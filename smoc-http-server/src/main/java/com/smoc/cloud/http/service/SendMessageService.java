package com.smoc.cloud.http.service;

import com.smoc.cloud.common.http.server.message.request.SendMessageByTemplateRequestParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.http.entity.AccountTemplateInfo;
import com.smoc.cloud.http.entity.MessageFormat;
import com.smoc.cloud.http.entity.MessageHttpsTaskInfo;
import com.smoc.cloud.http.repository.AccountTemplateInfoRepository;
import com.smoc.cloud.http.repository.MessageRepository;
import com.smoc.cloud.http.utils.Int64UUID;
import com.smoc.cloud.http.utils.MessageContentUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
public class SendMessageService {

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

        List<MessageFormat> messages = new ArrayList<>();
        //批次发送数量
        Integer length = params.getContent().length;
        if (1000 < length) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_MOBILE_NUM_ERROR.getCode(), ResponseCode.PARAM_MOBILE_NUM_ERROR.getMessage());
        }

        //模版内容
        String templateContent = optional.get().getTemplateContent();
        String submitTime = DateTimeUtils.getDateFormat(new Date(), "yyyy-MM-dd HH:mm:ss");

        //该批次发送短信总数量
        Integer messageCount = 0;
        //该批次手机号总量
        Integer phoneCount = 0;


        log.info("[组织短信]1：{}", System.currentTimeMillis());
        //获取模板ID
        String messageId = "TASK" + sequenceService.findSequence("TASK");
        log.info("[组织短信]2：{}", System.currentTimeMillis());
        for (int i = 0; i < length; i++) {

            if (StringUtils.isEmpty(params.getContent()[i])) {
                continue;
            }

            phoneCount++;
            MessageFormat messageFormat = new MessageFormat();
            log.info("[组织短信@1]：{}", System.currentTimeMillis());
            //普通模版短信
            if ("1".equals(optional.get().getTemplateFlag())) {
                String phoneNumber = params.getContent()[i].split("\\|")[0];
                messageFormat.setPhoneNumber(phoneNumber);
                messageFormat.setMessageContent(templateContent);
                messageCount += MessageContentUtil.splitSMSContentNumber(templateContent);
            }
            log.info("[组织短信@2]：{}", System.currentTimeMillis());
            //变量模版短信
            if ("2".equals(optional.get().getTemplateFlag())) {
                String[] content = params.getContent()[i].split("\\|");
                messageFormat.setPhoneNumber(content[0]);

                //替换占位符
                Map<String, String> paramMap = new HashMap<>();
                for (int j = 1; j < content.length; j++) {
                    paramMap.put(j + "", content[j]);
                }
                StrSubstitutor strSubstitutor = new StrSubstitutor(paramMap);
                String messageContent = strSubstitutor.replace(templateContent);
                messageCount += MessageContentUtil.splitSMSContentNumber(messageContent);
                messageFormat.setMessageContent(messageContent);
            }
            log.info("[组织短信@3]：{}", System.currentTimeMillis());

            messageFormat.setId(Int64UUID.random());
            messageFormat.setAccountId(params.getAccount());
            messageFormat.setMessageId(messageId);
            messageFormat.setTemplateId(params.getTemplateId());
            messageFormat.setAccountSrcId(params.getExtNumber());
            messageFormat.setAccountBusinessCode(params.getBusiness());
            messageFormat.setPhoneNumberNumber(length);
            messageFormat.setMessageContentNumber(1);
            messageFormat.setSubmitTime(submitTime);
            messageFormat.setMessageFormat("UTF-8");
            messageFormat.setProtocol("HTTP");
            messageFormat.setReportFlag(1);
            messageFormat.setCreateTime(new Date());

            messages.add(messageFormat);
        }

        log.info("[组织短信]3：{}", System.currentTimeMillis());

        log.info("messageCount：{}",messageCount);
        //流控、限流
        Boolean limiter = accountRateLimiter.limiter(params.getAccount(), messageCount);
        if (!limiter) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_MOBILE_LIMITER_ERROR.getCode(), ResponseCode.PARAM_MOBILE_LIMITER_ERROR.getMessage());
        }

        log.info("messageCount：{}",messageCount);
        //异步 批量保存短消息
        this.saveMessageBatch(messageId,messages, messageCount, phoneCount, templateContent, params.getTemplateId(), params.getAccount(), params.getExtNumber());

        //log.info("[普通短信]:{}", new Gson().toJson(messages));
        Map<String, String> result = new HashMap<>();
        result.put("orderNo", params.getOrderNo());
        result.put("template", params.getTemplateId());
        result.put("mobiles", phoneCount + "");
        result.put("messages", messageCount + "");
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

        List<MessageFormat> messages = new ArrayList<>();
        //批次发送数量
        Integer length = params.getContent().length;
        if (1000 < length) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_MOBILE_NUM_ERROR.getCode(), ResponseCode.PARAM_MOBILE_NUM_ERROR.getMessage());
        }

        //多媒体模板内容
        //String multimediaMessage = optional.get().getMmAttchment();
        String submitTime = DateTimeUtils.getDateFormat(new Date(), "yyyy-MM-dd HH:mm:ss");

        //该批次发送短信总数量
        Integer messageCount = 0;
        //该批次手机号总量
        Integer phoneCount = 0;

        //获取模板ID
        String messageId = "TASK" + sequenceService.findSequence("TASK");

        for (int i = 0; i < length; i++) {

            if (StringUtils.isEmpty(params.getContent()[i])) {
                continue;
            }


            MessageFormat messageFormat = new MessageFormat();
            //普通模版短信
            if ("1".equals(optional.get().getTemplateFlag())) {
                String phoneNumber = params.getContent()[i].split("\\|")[0];
                messageFormat.setPhoneNumber(phoneNumber);
            }

            //变量模版短信
            if ("2".equals(optional.get().getTemplateFlag())) {
                String[] content = params.getContent()[i].split("\\|");
                messageFormat.setPhoneNumber(content[0]);
                String multimedia = "";
                if (content.length > 1) {
                    for (int t = 1; t < content.length; t++) {
                        if (StringUtils.isEmpty(multimedia)) {
                            multimedia = content[t];
                        } else {
                            multimedia += "|" + content[t];
                        }
                    }
                }
                messageFormat.setMessageContent(multimedia);
            }

            phoneCount++;
            messageCount++;

            messageFormat.setId(Int64UUID.random());
            messageFormat.setAccountId(params.getAccount());
            messageFormat.setMessageId(messageId);
            messageFormat.setTemplateId(params.getTemplateId());
            messageFormat.setAccountSrcId(params.getExtNumber());
            messageFormat.setAccountBusinessCode(params.getBusiness());
            messageFormat.setPhoneNumberNumber(length);
            messageFormat.setMessageContentNumber(1);
            messageFormat.setSubmitTime(submitTime);
            messageFormat.setMessageFormat("UTF-8");
            messageFormat.setProtocol("HTTP");
            messageFormat.setReportFlag(1);
            messageFormat.setCreateTime(new Date());

            messages.add(messageFormat);
        }

        //流控、限流
        Boolean limiter = accountRateLimiter.limiter(params.getAccount(), messageCount);
        if (!limiter) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_MOBILE_LIMITER_ERROR.getCode(), ResponseCode.PARAM_MOBILE_LIMITER_ERROR.getMessage());
        }

        //异步 批量保存短消息
        this.saveMessageBatch(messageId,messages, messageCount, phoneCount, "", params.getTemplateId(), params.getAccount(), params.getExtNumber());
        Map<String, String> result = new HashMap<>();
        result.put("orderNo", params.getOrderNo());
        result.put("template", params.getTemplateId());
        result.put("mobiles", phoneCount + "");
        result.put("messages", messageCount + "");
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

        List<MessageFormat> messages = new ArrayList<>();
        //批次发送数量
        Integer length = params.getContent().length;
        if (1000 < length) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_MOBILE_NUM_ERROR.getCode(), ResponseCode.PARAM_MOBILE_NUM_ERROR.getMessage());
        }

        //模版内容
        String templateContent = optional.get().getTemplateContent();
        String submitTime = DateTimeUtils.getDateFormat(new Date(), "yyyy-MM-dd HH:mm:ss");

        //该批次发送短信总数量
        Integer messageCount = 0;
        //该批次手机号总量
        Integer phoneCount = 0;

        //获取模板ID
        String messageId = "TASK" + sequenceService.findSequence("TASK");

        for (int i = 0; i < length; i++) {

            if (StringUtils.isEmpty(params.getContent()[i])) {
                continue;
            }

            phoneCount++;
            MessageFormat messageFormat = new MessageFormat();
            //普通模版短信
            if ("1".equals(optional.get().getTemplateFlag())) {
                String phoneNumber = params.getContent()[i].split("\\|")[0];
                messageFormat.setPhoneNumber(phoneNumber);
                messageFormat.setMessageContent(templateContent);
                messageCount += MessageContentUtil.splitInternationalSMSContentNumber(templateContent);
            }

            //变量模版短信
            if ("2".equals(optional.get().getTemplateFlag())) {
                String[] content = params.getContent()[i].split("\\|");
                messageFormat.setPhoneNumber(content[0]);

                //替换占位符
                Map<String, String> paramMap = new HashMap<>();
                for (int j = 1; j < content.length; j++) {
                    paramMap.put(j + "", content[j]);
                }
                StrSubstitutor strSubstitutor = new StrSubstitutor(paramMap);
                String messageContent = strSubstitutor.replace(templateContent);
                messageCount += MessageContentUtil.splitInternationalSMSContentNumber(messageContent);
                messageFormat.setMessageContent(messageContent);
            }

            messageFormat.setId(Int64UUID.random());
            messageFormat.setAccountId(params.getAccount());
            messageFormat.setMessageId(messageId);
            messageFormat.setTemplateId(params.getTemplateId());
            messageFormat.setAccountSrcId(params.getExtNumber());
            messageFormat.setAccountBusinessCode(params.getBusiness());
            messageFormat.setPhoneNumberNumber(length);
            messageFormat.setMessageContentNumber(1);
            messageFormat.setSubmitTime(submitTime);
            messageFormat.setMessageFormat("UTF-8");
            messageFormat.setProtocol("HTTP");
            messageFormat.setReportFlag(1);
            messageFormat.setCreateTime(new Date());

            messages.add(messageFormat);
        }

        //流控、限流
        Boolean limiter = accountRateLimiter.limiter(params.getAccount(), messageCount);
        if (!limiter) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_MOBILE_LIMITER_ERROR.getCode(), ResponseCode.PARAM_MOBILE_LIMITER_ERROR.getMessage());
        }

        //异步 批量保存短消息
        this.saveMessageBatch(messageId,messages, messageCount, phoneCount, templateContent, params.getTemplateId(), params.getAccount(), params.getExtNumber());
        Map<String, String> result = new HashMap<>();
        result.put("orderNo", params.getOrderNo());
        result.put("template", params.getTemplateId());
        result.put("mobiles", phoneCount + "");
        result.put("messages", messageCount + "");
        return ResponseDataUtil.buildSuccess(result);
    }

    /**
     * 异步 批量保存 待发短信
     * @param messageId 任务id
     * @param messages     短消息
     * @param messageCount 发送短信数量
     * @param phoneCount   发送手机号数量
     */
    @Async
    @Transactional
    public void saveMessageBatch(String messageId,List<MessageFormat> messages, Integer messageCount, Integer phoneCount, String templateContent, String templateId, String account, String extNumber) {

        if (phoneCount > 0) {
            //发送消息
            messageRepository.saveMessageBatch(messages, messageCount, phoneCount);
        }

        //保存订单任务
        if (phoneCount > 1) {

            MessageHttpsTaskInfo taskInfo = new MessageHttpsTaskInfo();
            taskInfo.setId(messageId);
            taskInfo.setTemplateId(templateId);
            taskInfo.setBusinessAccount(account);
            taskInfo.setMessageContent(templateContent);
            taskInfo.setExpandNumber(extNumber);
            taskInfo.setSplitNumber(messageCount);
            taskInfo.setSubmitNumber(phoneCount);
            taskInfo.setCreatedBy("HTTP");
            messageRepository.saveBatchTask(taskInfo);
        }
    }
}
