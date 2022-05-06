package com.smoc.cloud.configure.channel.controller;

import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelSpareChannelValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.channel.service.ConfigChannelSpareChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;


/**
 * 备用通道参数
 **/
@Slf4j
@RestController
@RequestMapping("configure/channel/spare")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class ConfigChannelSpareChannelController {

    @Autowired
    private ConfigChannelSpareChannelService configChannelSpareChannelService;

    /**
     * 根据id获取
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

        ResponseData data = configChannelSpareChannelService.findById(id);
        return data;
    }

    /**
     * 根据通道id获取备用通道
     * @param id
     * @return
     */
    @RequestMapping(value = "/findByChannelId/{id}", method = RequestMethod.GET)
    public ResponseData findByChannelId(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData data = configChannelSpareChannelService.findByChannelId(id);
        return data;
    }

    /**
     * 添加、修改通道接口参数
     * @param op 操作标记，add表示添加，edit表示修改
     * @returns
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody ConfigChannelSpareChannelValidator configChannelSpareChannelValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(configChannelSpareChannelValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(configChannelSpareChannelValidator));
        }

        //保存操作
        ResponseData data = configChannelSpareChannelService.save(configChannelSpareChannelValidator, op);

        return data;
    }

    /**
     * 根据原通道属性查询符合要求的备用通道
     * @returns
     */
    @RequestMapping(value = "/findSpareChannel", method = RequestMethod.POST)
    public ResponseData<List<ConfigChannelSpareChannelValidator>> findSpareChannel(@RequestBody ChannelBasicInfoValidator channelBasicInfoValidator) {

        return configChannelSpareChannelService.findSpareChannel(channelBasicInfoValidator);
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

        return configChannelSpareChannelService.deleteById(id);
    }
}
