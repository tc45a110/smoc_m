package com.smoc.cloud.message.service;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.message.MessageAccountValidator;
import com.smoc.cloud.common.smoc.message.model.StatisticMessageSend;
import com.smoc.cloud.common.smoc.template.MessageWebTaskInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.customer.entity.EnterpriseDocumentInfo;
import com.smoc.cloud.message.entity.MessageWebTaskInfo;
import com.smoc.cloud.message.repository.MessageWebTaskInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * web任务单
 */
@Slf4j
@Service
public class MessageWebTaskInfoService {

    @Resource
    private MessageWebTaskInfoRepository messageWebTaskInfoRepository;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<MessageWebTaskInfoValidator>> page(PageParams<MessageWebTaskInfoValidator> pageParams) {

        PageList<MessageWebTaskInfoValidator> page = messageWebTaskInfoRepository.page(pageParams);

        return ResponseDataUtil.buildSuccess(page);

    }

    /**
     * 统计发送数量
     *
     * @param qo
     * @return
     */
    public ResponseData<Map<String, Object>> countSum(MessageWebTaskInfoValidator qo) {

        Map<String, Object> map = messageWebTaskInfoRepository.countSum(qo);
        return ResponseDataUtil.buildSuccess(map);
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData findById(String id) {
        Optional<MessageWebTaskInfo> data = messageWebTaskInfoRepository.findById(id);

        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        MessageWebTaskInfo entity = data.get();
        MessageWebTaskInfoValidator messageWebTaskInfoValidator = new MessageWebTaskInfoValidator();
        BeanUtils.copyProperties(entity, messageWebTaskInfoValidator);

        //转换日期
        messageWebTaskInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));

        return ResponseDataUtil.buildSuccess(messageWebTaskInfoValidator);
    }

    /**
     * 保存或修改
     *
     * @param messageWebTaskInfoValidator
     * @param op                          操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData<MessageWebTaskInfo> save(MessageWebTaskInfoValidator messageWebTaskInfoValidator, String op) {

        //转MeipMessageTaskList存放对象
        MessageWebTaskInfo entity = new MessageWebTaskInfo();

        BeanUtils.copyProperties(messageWebTaskInfoValidator, entity);

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(messageWebTaskInfoValidator.getCreatedTime()));

        //记录日志
        log.info("[短信群发][{}]数据:{}", op, JSON.toJSONString(entity));

        messageWebTaskInfoRepository.saveAndFlush(entity);
        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Transactional
    public ResponseData<MessageWebTaskInfo> deleteById(String id) {

        MessageWebTaskInfo data = messageWebTaskInfoRepository.findById(id).get();
        //记录日志
        log.info("[短信群发][delete]数据:{}", JSON.toJSONString(data));
        messageWebTaskInfoRepository.deleteById(id);

        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 发送短信
     *
     * @param id
     * @return
     */
    @Transactional
    public ResponseData sendMessageById(String id) {
        MessageWebTaskInfo data = messageWebTaskInfoRepository.findById(id).get();
        //记录日志
        log.info("[短信群发][send]数据:{}", JSON.toJSONString(data));

        messageWebTaskInfoRepository.sendMessageById(id, "01", DateTimeUtils.getDateTimeFormat(new Date()));

        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 查询企业发送量
     * @param messageAccountValidator
     * @return
     */
    public ResponseData<StatisticMessageSend> statisticMessageSendCount(MessageAccountValidator messageAccountValidator) {
        StatisticMessageSend data = messageWebTaskInfoRepository.statisticMessageSendCount(messageAccountValidator);
        return ResponseDataUtil.buildSuccess(data);
    }
}
