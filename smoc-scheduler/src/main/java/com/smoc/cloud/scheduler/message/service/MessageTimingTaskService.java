package com.smoc.cloud.scheduler.message.service;

import com.smoc.cloud.common.smoc.message.model.MessageFormat;
import com.smoc.cloud.common.smoc.utils.Int64UUID;
import com.smoc.cloud.common.smoc.utils.MessageContentUtil;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.scheduler.message.service.model.MessageTimingModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 定时发送短信任务
 **/
@Slf4j
@Service
public class MessageTimingTaskService {

    @Resource
    private MessageTimingTaskRepository messageTimingTaskRepository;

    /**
     * 发送短信
     * @param messageTimingModel
     * @param mobileList
     */
    public void sendMessage(MessageTimingModel messageTimingModel, List<String> mobileList) {

        //更新任务为完成状态
        messageTimingTaskRepository.updataTaskStatus(messageTimingModel.getId(),"02");

        //异步添加明细
        saveMessageDetail(messageTimingModel,mobileList);

    }

    private void saveMessageDetail(MessageTimingModel messageTimingModel,List<String> phoneCount) {
        //该批次发送短信总数量
        Integer messageCount = 0;
        List<MessageFormat> messages = new ArrayList<>();
        String submitTime = DateTimeUtils.getDateFormat(new Date(), "yyyy-MM-dd HH:mm:ss");

        for (int i = 0; i < phoneCount.size(); i++) {

            MessageFormat messageFormat = new MessageFormat();

            //普通短信
            if("TEXT_SMS".equals(messageTimingModel.getBusinessType())){
                //普通模版短信
                if ("1".equals(messageTimingModel.getMessageType())) {
                    messageFormat.setPhoneNumber(phoneCount.get(i));
                    messageFormat.setMessageContent(messageTimingModel.getMessageContent());
                    messageCount += MessageContentUtil.splitSMSContentNumber(messageTimingModel.getMessageContent());
                }

                //变量模版短信
                if ("2".equals(messageTimingModel.getMessageType())) {
                    String[] content = phoneCount.get(i).split("\\|");
                    messageFormat.setPhoneNumber(content[0]);

                    //替换占位符
                    Map<String, String> paramMap = new HashMap<>();
                    for (int j = 1; j < content.length; j++) {
                        paramMap.put(j + "", content[j]);
                    }
                    StrSubstitutor strSubstitutor = new StrSubstitutor(paramMap);
                    String messageContent = strSubstitutor.replace(messageTimingModel.getMessageContent());
                    messageCount += MessageContentUtil.splitSMSContentNumber(messageContent);
                    messageFormat.setMessageContent(messageContent);
                }
            }

            //多媒体短信
            if("MULTI_SMS".equals(messageTimingModel.getBusinessType())){
                //普通模版短信
                if ("1".equals(messageTimingModel.getMessageType())) {
                    messageFormat.setPhoneNumber(phoneCount.get(i));
                }

                //变量模版短信
                if ("2".equals(messageTimingModel.getMessageType())) {
                    String[] content = phoneCount.get(i).split("\\|");
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

                messageCount++;
            }

            //国际短信
            if("INTERNATIONAL_SMS".equals(messageTimingModel.getBusinessType())){
                //普通模版短信
                if ("1".equals(messageTimingModel.getMessageType())) {
                    messageFormat.setPhoneNumber(phoneCount.get(i));
                    messageFormat.setMessageContent(messageTimingModel.getMessageContent());
                    messageCount += MessageContentUtil.splitInternationalSMSContentNumber(messageTimingModel.getMessageContent());
                }

                //变量模版短信
                if ("2".equals(messageTimingModel.getMessageType())) {
                    String[] content = phoneCount.get(i).split("\\|");
                    messageFormat.setPhoneNumber(content[0]);

                    //替换占位符
                    Map<String, String> paramMap = new HashMap<>();
                    for (int j = 1; j < content.length; j++) {
                        paramMap.put(j + "", content[j]);
                    }
                    StrSubstitutor strSubstitutor = new StrSubstitutor(paramMap);
                    String messageContent = strSubstitutor.replace(messageTimingModel.getMessageContent());
                    messageCount += MessageContentUtil.splitInternationalSMSContentNumber(messageContent);
                    messageFormat.setMessageContent(messageContent);
                }
            }

            messageFormat.setAccountId(messageTimingModel.getAccountId());
            messageFormat.setMessageId(messageTimingModel.getId());
            messageFormat.setTemplateId(messageTimingModel.getTemplateId());
            messageFormat.setAccountSrcId("");
            messageFormat.setAccountBusinessCode("");
            messageFormat.setPhoneNumberNumber(phoneCount.size());
            messageFormat.setMessageContentNumber(1);
            messageFormat.setSubmitTime(submitTime);
            messageFormat.setMessageFormat("");
            messageFormat.setProtocol("WEB");
            messageFormat.setReportFlag(1);
            messageFormat.setCreateTime(new Date());

            messages.add(messageFormat);
        }

        //异步 批量保存短消息
        saveMessageBatch(messages, messageCount, phoneCount,messageTimingModel.getId());
    }

    @Async
    public void saveMessageBatch(List<MessageFormat> messages, Integer messageCount, List<String> phoneCount, String taskId) {
        messageTimingTaskRepository.saveMessageBatch(messages,messageCount,phoneCount.size(),taskId);
    }

    //查询通讯录手机号
    public List<String> findByGroupIdAndStatus(String groupId, String status) {

        return messageTimingTaskRepository.findByGroupIdAndStatus(groupId,status);
    }
}
