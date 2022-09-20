package com.smoc.cloud.complaint.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseContractInfoValidator;
import com.smoc.cloud.common.smoc.message.MessageChannelComplaintValidator;
import com.smoc.cloud.common.smoc.message.MessageComplaintInfoValidator;
import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.complaint.service.ComplaintService;
import com.smoc.cloud.customer.service.EnterpriseContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;


/**
 * 投诉管理信息接口
 **/
@Slf4j
@RestController
@RequestMapping("complaint")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public PageList<MessageComplaintInfoValidator> page(@RequestBody PageParams<MessageComplaintInfoValidator> pageParams) {

        return complaintService.page(pageParams);
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

        ResponseData data = complaintService.findById(id);
        return data;
    }

    /**
     * 添加、修改
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody MessageComplaintInfoValidator messageComplaintInfoValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(messageComplaintInfoValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(messageComplaintInfoValidator));
        }

        //保存操作
        ResponseData data = complaintService.save(messageComplaintInfoValidator, op);

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

        return complaintService.deleteById(id);
    }

    /**
     * 批量导入投诉
     * @param messageComplaintInfoValidator
     * @return
     */
    @RequestMapping(value = "/batchSave", method = RequestMethod.POST)
    public ResponseData batchSave(@RequestBody MessageComplaintInfoValidator messageComplaintInfoValidator) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(messageComplaintInfoValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(messageComplaintInfoValidator));
        }

        //保存操作
        ResponseData data = complaintService.batchSave(messageComplaintInfoValidator);

        return data;
    }

    /**
     * 查询通道投诉排行
     * @param messageChannelComplaintValidator
     * @return
     */
    @RequestMapping(value = "/channelComplaintRanking", method = RequestMethod.POST)
    public ResponseData<List<MessageChannelComplaintValidator>> channelComplaintRanking(@RequestBody MessageChannelComplaintValidator messageChannelComplaintValidator) {

        return complaintService.channelComplaintRanking(messageChannelComplaintValidator);
    }

    /**
     * 根据投诉手机号查询10天内的下发记录
     * @param detail
     * @return
     */
    @RequestMapping(value = "/sendMessageList", method = RequestMethod.POST)
    public ResponseData<List<MessageDetailInfoValidator>> sendMessageList(@RequestBody MessageDetailInfoValidator detail) {

        return complaintService.sendMessageList(detail);
    }

}
