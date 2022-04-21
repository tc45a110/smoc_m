package com.smoc.cloud.message.controller;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.message.MessageAccountValidator;
import com.smoc.cloud.common.smoc.message.model.MessageTaskDetail;
import com.smoc.cloud.common.smoc.message.model.StatisticMessageSend;
import com.smoc.cloud.common.smoc.message.MessageWebTaskInfoValidator;
import com.smoc.cloud.common.smoc.message.model.StatisticMessageSendData;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.message.service.MessageWebTaskInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * web任务单
 */
@Slf4j
@RestController
@RequestMapping("message/web/task")
public class MessageWebTaskInfoController {

    @Autowired
    private MessageWebTaskInfoService messageWebTaskInfoService;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseData<PageList<MessageWebTaskInfoValidator>> page(@RequestBody PageParams<MessageWebTaskInfoValidator> pageParams) {

        return messageWebTaskInfoService.page(pageParams);
    }

    /**
     * 统计发送数量
     *
     * @param qo
     * @return
     */
    @RequestMapping(value = "/count", method = RequestMethod.POST)
    public ResponseData<Map<String, Object>> count(@RequestBody MessageWebTaskInfoValidator qo) {

        return messageWebTaskInfoService.countSum(qo);
    }

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseData findById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData data = messageWebTaskInfoService.findById(id);
        return data;
    }

    /**
     * 添加、修改
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody MessageWebTaskInfoValidator messageWebTaskInfoValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(messageWebTaskInfoValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(messageWebTaskInfoValidator));
        }

        //保存操作
        ResponseData data = messageWebTaskInfoService.save(messageWebTaskInfoValidator, op);

        return data;
    }

    /**
     * 根据ID 删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ResponseData deleteById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        return messageWebTaskInfoService.deleteById(id);
    }

    /**
     * 发送短信
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/sendMessageById/{id}", method = RequestMethod.GET)
    public ResponseData sendMessageById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        return messageWebTaskInfoService.sendMessageById(id);
    }

    /**
     * 查询企业发送量
     * @param messageAccountValidator
     * @return
     */
    @RequestMapping(value = "/statisticMessageSendCount", method = RequestMethod.POST)
    public ResponseData<StatisticMessageSend> statisticMessageSendCount(@RequestBody MessageAccountValidator messageAccountValidator) {

        return messageWebTaskInfoService.statisticMessageSendCount(messageAccountValidator);
    }

    /**
     * 统计短信提交发送量
     * @param messageWebTaskInfoValidator
     * @return
     */
    @RequestMapping(value = "/statisticSubmitMessageSendCount", method = RequestMethod.POST)
    public ResponseData<StatisticMessageSend> statisticSubmitMessageSendCount(@RequestBody MessageWebTaskInfoValidator messageWebTaskInfoValidator) {

        return messageWebTaskInfoService.statisticSubmitMessageSendCount(messageWebTaskInfoValidator);
    }

}
