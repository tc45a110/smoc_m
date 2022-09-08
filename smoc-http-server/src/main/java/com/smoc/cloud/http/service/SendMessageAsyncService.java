package com.smoc.cloud.http.service;

import com.google.gson.Gson;
import com.smoc.cloud.common.http.server.message.request.SendMessageByTemplateRequestParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.http.entity.AccountTemplateInfo;
import com.smoc.cloud.http.entity.MessageFormat;
import com.smoc.cloud.http.entity.MessageHttpsTaskInfo;
import com.smoc.cloud.http.filters.filter.full_filter.FullFilterParamsFilter;
import com.smoc.cloud.http.filters.request.model.RequestFullParams;
import com.smoc.cloud.http.redis.IdGeneratorFactory;
import com.smoc.cloud.http.repository.AccountTemplateInfoRepository;
import com.smoc.cloud.http.repository.MessageRepository;
import com.smoc.cloud.http.utils.MessageContentUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
public class SendMessageAsyncService {

    @Autowired
    private AccountRateLimiter accountRateLimiter;

    @Autowired
    private IdGeneratorFactory idGeneratorFactory;
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SystemHttpApiRequestService systemHttpApiRequestService;

    @Resource
    private AccountTemplateInfoRepository accountTemplateInfoRepository;

    @Autowired
    private FullFilterParamsFilter fullFilterParamsFilter;


    /**
     * 修改为异步，把处理过程、发送过程单独拉出来
     *
     * @param length
     * @param params
     * @param accountTemplateInfo
     */
    public ResponseData sendMessageByTemplate(Integer length, SendMessageByTemplateRequestParams params, AccountTemplateInfo accountTemplateInfo, String messageId) {
        List<MessageFormat> messages = new ArrayList<>();
        //模版内容
        String templateContent = accountTemplateInfo.getTemplateContent();
        String submitTime = DateTimeUtils.getDateFormat(new Date(), "yyyy-MM-dd HH:mm:ss");

        //该批次发送短信总数量
        Integer messageCount = 0;
        //该批次手机号总量
        Integer phoneCount = 0;
//
//        log.info("[组织短信]1：{}", System.currentTimeMillis());
        //log.info("[组织短信]2：{}", System.currentTimeMillis());
        for (int i = 0; i < length; i++) {

            String item = params.getContent()[i];
            if (StringUtils.isEmpty(item)) {
                continue;
            }

            phoneCount++;
            MessageFormat messageFormat = new MessageFormat();
            //log.info("[组织短信@1]：{}", System.currentTimeMillis());
            //普通模版短信
            if ("1".equals(accountTemplateInfo.getTemplateFlag())) {
                String phoneNumber = item.split("\\|")[0];
                messageFormat.setPhoneNumber(phoneNumber);
                messageFormat.setMessageContent(templateContent);
                messageCount += MessageContentUtil.splitSMSContentNumber(templateContent);
            }
            //log.info("[组织短信@2]：{}", System.currentTimeMillis());
            //变量模版短信
            if ("2".equals(accountTemplateInfo.getTemplateFlag())) {
                String[] content = item.split("\\|");
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
            //log.info("[组织短信@3]：{}", System.currentTimeMillis());
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

            /**
             * 过滤服务
             */
            RequestFullParams fullParams = new RequestFullParams();
            fullParams.setAccount(messageFormat.getAccountId());
            fullParams.setPhone(messageFormat.getPhoneNumber());
            fullParams.setMessage(messageFormat.getMessageContent());
            ResponseData responseData = fullFilterParamsFilter.filter(fullParams);
            if (!ResponseCode.SUCCESS.getCode().equals(responseData.getCode())) {
                return responseData;
            }
            log.info("[http短信日志]：{}", new Gson().toJson(messages));
        }
        //异步 批量保存短消息
        this.saveMessageBatch(messageId, messages, messageCount, phoneCount, templateContent, params.getTemplateId(), params.getAccount(), params.getExtNumber());
        return ResponseDataUtil.buildSuccess();
    }


    /**
     * 修改为异步，把处理过程、发送过程单独拉出来
     *
     * @param length
     * @param params
     * @param accountTemplateInfo
     */
    public ResponseData sendMultimediaMessageByTemplate(Integer length, SendMessageByTemplateRequestParams params, AccountTemplateInfo accountTemplateInfo, String messageId) {
        List<MessageFormat> messages = new ArrayList<>();
        //多媒体模板内容
        //String multimediaMessage = optional.get().getMmAttchment();
        String submitTime = DateTimeUtils.getDateFormat(new Date(), "yyyy-MM-dd HH:mm:ss");

        //该批次发送短信总数量
        Integer messageCount = 0;
        //该批次手机号总量
        Integer phoneCount = 0;
//        log.info("[组织短信]1：{}", System.currentTimeMillis());


        for (int i = 0; i < length; i++) {

            if (StringUtils.isEmpty(params.getContent()[i])) {
                continue;
            }

            MessageFormat messageFormat = new MessageFormat();
            //普通模版短信
            if ("1".equals(accountTemplateInfo.getTemplateFlag())) {
                String phoneNumber = params.getContent()[i].split("\\|")[0];
                messageFormat.setPhoneNumber(phoneNumber);
            }

            //变量模版短信
            if ("2".equals(accountTemplateInfo.getTemplateFlag())) {
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

            /**
             * 过滤服务
             */
            RequestFullParams fullParams = new RequestFullParams();
            fullParams.setAccount(messageFormat.getAccountId());
            fullParams.setPhone(messageFormat.getPhoneNumber());
            fullParams.setMessage("abcdefg");
            ResponseData responseData = fullFilterParamsFilter.filter(fullParams);
            if (!ResponseCode.SUCCESS.getCode().equals(responseData.getCode())) {
                return responseData;
            }

            log.info("[http多媒体短信日志]：{}", new Gson().toJson(messages));
        }

        //异步 批量保存短消息
        this.saveMessageBatch(messageId, messages, messageCount, phoneCount, "", params.getTemplateId(), params.getAccount(), params.getExtNumber());
//        log.info("[组织短信]3：{}", System.currentTimeMillis());
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 修改为异步，把处理过程、发送过程单独拉出来
     *
     * @param length
     * @param params
     * @param accountTemplateInfo
     */

    public void sendInterMessageByTemplate(Integer length, SendMessageByTemplateRequestParams params, AccountTemplateInfo accountTemplateInfo, String messageId) {
        List<MessageFormat> messages = new ArrayList<>();
        //模版内容
        String templateContent = accountTemplateInfo.getTemplateContent();
        String submitTime = DateTimeUtils.getDateFormat(new Date(), "yyyy-MM-dd HH:mm:ss");

        //该批次发送短信总数量
        Integer messageCount = 0;
        //该批次手机号总量
        Integer phoneCount = 0;
//        log.info("[组织短信]1：{}", System.currentTimeMillis());

        for (int i = 0; i < length; i++) {

            if (StringUtils.isEmpty(params.getContent()[i])) {
                continue;
            }

            phoneCount++;
            MessageFormat messageFormat = new MessageFormat();
            //普通模版短信
            if ("1".equals(accountTemplateInfo.getTemplateFlag())) {
                String phoneNumber = params.getContent()[i].split("\\|")[0];
                messageFormat.setPhoneNumber(phoneNumber);
                messageFormat.setMessageContent(templateContent);
                messageCount += MessageContentUtil.splitInternationalSMSContentNumber(templateContent);
            }

            //变量模版短信
            if ("2".equals(accountTemplateInfo.getTemplateFlag())) {
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
            log.info("[http国际短信日志]：{}", new Gson().toJson(messages));
        }

        //异步 批量保存短消息
        this.saveMessageBatch(messageId, messages, messageCount, phoneCount, templateContent, params.getTemplateId(), params.getAccount(), params.getExtNumber());
//        log.info("[组织短信]3：{}", System.currentTimeMillis());
    }


    /**
     * 异步 批量保存 待发短信
     *
     * @param messageId    任务id
     * @param messages     短消息
     * @param messageCount 发送短信数量
     * @param phoneCount   发送手机号数量
     */

    @Async("threadPoolTaskExecutor")
    public void saveMessageBatch(String messageId, List<MessageFormat> messages, Integer messageCount, Integer phoneCount, String templateContent, String templateId, String account, String extNumber) {
        log.info("[http国际短信日志]：{}", new Gson().toJson(messages));
        //发送消息
        if (phoneCount > 0) {
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
