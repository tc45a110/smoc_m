package com.smoc.cloud.http.service;

import com.google.gson.Gson;
import com.smoc.cloud.common.http.server.message.request.SendMessageByTemplateRequestParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.http.entity.AccountTemplateInfo;
import com.smoc.cloud.http.entity.MessageFormat;
import com.smoc.cloud.http.repository.AccountTemplateInfoRepository;
import com.smoc.cloud.http.utils.Int64UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;

@Slf4j
@Service
public class SendMessageService {

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

        //判断模版
        Optional<AccountTemplateInfo> optional = accountTemplateInfoRepository.findAccountTemplateInfoByBusinessAccountAndTemplateIdAndTemplateAgreementType(params.getAccount(), params.getTemplateId(), "HTTP");
        if (!optional.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_TEMPLATE_NOT_EXIST_ERROR.getCode(), ResponseCode.PARAM_TEMPLATE_NOT_EXIST_ERROR.getMessage());
        }

        if (!"1".equals(optional.get().getTemplateStatus())) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_TEMPLATE_STATUS_ERROR.getCode(), ResponseCode.PARAM_TEMPLATE_STATUS_ERROR.getMessage());
        }

        //判断发送内容
        if (null == params.getContent() || params.getContent().length < 1) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_MOBILE_ERROR.getCode(), ResponseCode.PARAM_MOBILE_ERROR.getMessage());
        }

        List<MessageFormat> messages = new ArrayList<>();
        //批次发送数量
        Integer length = params.getContent().length;
        //模版内容
        String templateContent = optional.get().getTemplateContent();
        String submitTime = DateTimeUtils.getDateFormat(new Date(), "yyyy-MM-dd HH:mm:ss");

        //该批次发送短信总数量
        Integer messageCount = 0;
        //该批次手机号总量
        Integer phoneCount = 0;

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


                //计算短信条数
                //messageCount++;
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
                messageFormat.setMessageContent(messageContent);

                //计算短信条数
                //messageCount++;
            }

            messageFormat.setId(Int64UUID.random());
            messageFormat.setAccountId(params.getAccount());
            messageFormat.setMessageId(params.getOrderNo());
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

        //异步 批量保存短消息
        this.saveMessageBatch(messages, messageCount, phoneCount);

        log.info("[普通短信]:{}", new Gson().toJson(messages));
        Map<String, String> result = new HashMap<>();
        result.put("orderNo", params.getOrderNo());
        return ResponseDataUtil.buildSuccess(result);
    }

    public ResponseData<Map<String, String>> sendMultimediaMessageByTemplate(SendMessageByTemplateRequestParams params) {

        //判断发送内容
        if (null == params.getContent() || params.getContent().length < 1) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_MOBILE_ERROR.getCode(), ResponseCode.PARAM_MOBILE_ERROR.getMessage());
        }
        Integer size = 0;

        Map<String, String> result = new HashMap<>();
        result.put("orderNo", params.getOrderNo());
        return ResponseDataUtil.buildSuccess(result);
    }

    public ResponseData<Map<String, String>> sendInterMessageByTemplate(SendMessageByTemplateRequestParams params) {

        //判断发送内容
        if (null == params.getContent() || params.getContent().length < 1) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_MOBILE_ERROR.getCode(), ResponseCode.PARAM_MOBILE_ERROR.getMessage());
        }
        Integer size = 0;

        Map<String, String> result = new HashMap<>();
        result.put("orderNo", params.getOrderNo());
        return ResponseDataUtil.buildSuccess(result);
    }

    /**
     * 批量保存 待发短信
     *
     * @param messages     短消息
     * @param messageCount 短信数量
     * @param phoneCount   手机号数量
     */
    @Async
    @Transactional
    public void saveMessageBatch(List<MessageFormat> messages, Integer messageCount, Integer phoneCount) {

        if (phoneCount < 1) {
            return;
        }

    }
}
