package com.smoc.cloud.message.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.message.MessageWebTaskInfoValidator;
import com.smoc.cloud.common.smoc.message.model.MessageFormat;
import com.smoc.cloud.common.smoc.utils.Int64UUID;
import com.smoc.cloud.common.smoc.utils.MessageContentUtil;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.customer.entity.AccountBasicInfo;
import com.smoc.cloud.customer.repository.BusinessAccountRepository;
import com.smoc.cloud.message.entity.MessageWebTaskInfo;
import com.smoc.cloud.message.properties.MessageProperties;
import com.smoc.cloud.message.repository.MessageWebTaskInfoRepository;
import com.smoc.cloud.template.entity.AccountTemplateInfo;
import com.smoc.cloud.template.repository.AccountTemplateInfoRepository;
import com.smoc.cloud.utils.IdGeneratorFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * web任务单
 */
@Slf4j
@Service
public class MessageSendWebTaskInfoService {

    @Resource
    private MessageWebTaskInfoRepository messageWebTaskInfoRepository;

    @Resource
    private AccountTemplateInfoRepository accountTemplateInfoRepository;

    @Resource
    private BusinessAccountRepository businessAccountRepository;

    @Resource
    private MessageProperties messageProperties;

    @Resource
    private MessageWebTaskInfoService messageWebTaskInfoService;

    @Resource
    private IdGeneratorFactory idGeneratorFactory;

    /**
     * 发送短信
     *
     * @param messageWebTaskInfoValidator
     * @param op                          操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData<MessageWebTaskInfo> sendMessag(MessageWebTaskInfoValidator messageWebTaskInfoValidator, String op) {

        //批次发送数量
        Integer phoneCount = messageWebTaskInfoValidator.getMobiles().size();
        if (StringUtils.isEmpty(messageWebTaskInfoValidator.getMobiles()) || messageWebTaskInfoValidator.getMobiles().size() <= 0) {
            return ResponseDataUtil.buildError("发送号码不能为空或号码无效！");
        }

        //判断发送数量
        if (phoneCount > messageProperties.getMobileCountLimit()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_MOBILE_NUM_ERROR.getCode(), "单批次发送量超过"+messageProperties.getMobileCountLimit()+"条!");
        }

        //查询模板
        Optional<AccountTemplateInfo> optional = accountTemplateInfoRepository.findById(messageWebTaskInfoValidator.getTemplateId());
        if (!optional.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_TEMPLATE_NOT_EXIST_ERROR.getCode(), ResponseCode.PARAM_TEMPLATE_NOT_EXIST_ERROR.getMessage());
        }

        //判断模板是否通过审核
        if (!"2".equals(optional.get().getTemplateStatus())) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_TEMPLATE_STATUS_ERROR.getCode(), ResponseCode.PARAM_TEMPLATE_STATUS_ERROR.getMessage());
        }

        //查询账号
        Optional<AccountBasicInfo> accountBasicInfo = businessAccountRepository.findById(messageWebTaskInfoValidator.getBusinessAccount());
        if (!accountBasicInfo.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        //账号与模板类型是否匹配
        if (!accountBasicInfo.get().getInfoType().equals(optional.get().getInfoType())) {
            return ResponseDataUtil.buildError("账号与模板类型不匹配！");
        }

        //模版内容
        String templateContent = optional.get().getTemplateContent();
        String submitTime = DateTimeUtils.getDateFormat(new Date(), "yyyy-MM-dd HH:mm:ss");

        messageWebTaskInfoValidator.setId(idGeneratorFactory.getTaskId());

        //该批次发送短信总数量
        Integer messageCount = 0;
        List<MessageFormat> messages = new ArrayList<>();

        for (int i = 0; i < phoneCount; i++) {

            MessageFormat messageFormat = new MessageFormat();

            //普通短信
            if("TEXT_SMS".equals(optional.get().getTemplateType())){
                //普通模版短信
                if ("1".equals(optional.get().getTemplateFlag())) {
                    messageFormat.setPhoneNumber(messageWebTaskInfoValidator.getMobiles().get(i));
                    messageFormat.setMessageContent(templateContent);
                    messageCount += MessageContentUtil.splitSMSContentNumber(templateContent);
                }

                //变量模版短信
                if ("2".equals(optional.get().getTemplateFlag())) {
                    String[] content = messageWebTaskInfoValidator.getMobiles().get(i).split("\\|");
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
            }

            //多媒体短信
            if("MULTI_SMS".equals(optional.get().getTemplateType())){
                //普通模版短信
                if ("1".equals(optional.get().getTemplateFlag())) {
                    messageFormat.setPhoneNumber(messageWebTaskInfoValidator.getMobiles().get(i));
                }

                //变量模版短信
                if ("2".equals(optional.get().getTemplateFlag())) {
                    String[] content = messageWebTaskInfoValidator.getMobiles().get(i).split("\\|");
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
            if("INTERNATIONAL_SMS".equals(optional.get().getTemplateType())){
                //普通模版短信
                if ("1".equals(optional.get().getTemplateFlag())) {
                    messageFormat.setPhoneNumber(messageWebTaskInfoValidator.getMobiles().get(i));
                    messageFormat.setMessageContent(templateContent);
                    messageCount += MessageContentUtil.splitInternationalSMSContentNumber(templateContent);
                }

                //变量模版短信
                if ("2".equals(optional.get().getTemplateFlag())) {
                    String[] content = messageWebTaskInfoValidator.getMobiles().get(i).split("\\|");
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
            }

            messageFormat.setAccountId(messageWebTaskInfoValidator.getBusinessAccount());
            messageFormat.setMessageId(messageWebTaskInfoValidator.getId());
            messageFormat.setTemplateId(messageWebTaskInfoValidator.getTemplateId());
            messageFormat.setAccountSrcId("");
            messageFormat.setAccountBusinessCode("");
            messageFormat.setPhoneNumberNumber(phoneCount);
            messageFormat.setMessageContentNumber(1);
            messageFormat.setSubmitTime(submitTime);
            messageFormat.setMessageFormat("");
            messageFormat.setProtocol("WEB");
            messageFormat.setReportFlag(1);
            messageFormat.setCreateTime(new Date());

            messages.add(messageFormat);
        }

        MessageWebTaskInfo entity = new MessageWebTaskInfo();
        BeanUtils.copyProperties(messageWebTaskInfoValidator, entity);

        entity.setSplitNumber(messageCount);
        entity.setSuccessNumber(messageCount);
        entity.setSubmitNumber(phoneCount);
        entity.setInfoType(accountBasicInfo.get().getInfoType());
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(messageWebTaskInfoValidator.getCreatedTime()));

        //记录日志
        log.info("[短信群发][{}]数据:{}", op, JSON.toJSONString(entity));

        //定时发送
        if("2".equals(entity.getSendType())){
            entity.setSendStatus("05");//待发送
        }

        //保存任务单
        messageWebTaskInfoRepository.saveAndFlush(entity);

        //及时发送
        if("1".equals(entity.getSendType())){
            //异步 批量保存短消息
            messageWebTaskInfoService.saveMessageBatch(messages, messageCount, phoneCount);
        }


        return ResponseDataUtil.buildSuccess();
    }

}
