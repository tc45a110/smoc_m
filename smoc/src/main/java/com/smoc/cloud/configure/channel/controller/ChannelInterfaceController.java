package com.smoc.cloud.configure.channel.controller;

import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelInterfaceValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.channel.service.ChannelInterfaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;


/**
 * 通道接口参数
 **/
@Slf4j
@RestController
@RequestMapping("configure/channel")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class ChannelInterfaceController {

    @Autowired
    private ChannelInterfaceService channelInterfaceService;

    /**
     * 根据通道id获取通道接口参数
     * @param id
     * @return
     */
    @RequestMapping(value = "/findChannelInterfaceByChannelId/{id}", method = RequestMethod.GET)
    public ResponseData findChannelInterfaceByChannelId(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData data = channelInterfaceService.findByChannelId(id);
        return data;
    }

    /**
     * 添加、修改通道接口参数
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/interfaceSave/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody ChannelInterfaceValidator channelInterfaceValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(channelInterfaceValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(channelInterfaceValidator));
        }

        //保存操作
        ResponseData data = channelInterfaceService.save(channelInterfaceValidator, op);

        return data;
    }

}
