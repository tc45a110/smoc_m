package com.smoc.cloud.configure.channel.group.controller;

import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.qo.ConfigChannelGroupQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelGroupConfigValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.channel.group.service.ConfigChannelGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;


/**
 * 配置通道组接口
 **/
@Slf4j
@RestController
@RequestMapping("configure/channel/group")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class ConfigChannelGroupController {

    @Autowired
    private ConfigChannelGroupService configChannelGroupService;

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/findConfigChannelById/{id}", method = RequestMethod.GET)
    public ResponseData findConfigChannelById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData data = configChannelGroupService.findById(id);
        return data;
    }

    /**
     * 查询通道列表
     * @param channelBasicInfoQo
     * @return
     */
    @RequestMapping(value = "/findChannelList", method = RequestMethod.POST)
    public ResponseData<List<ChannelBasicInfoQo>> findChannelList(@RequestBody ChannelBasicInfoQo channelBasicInfoQo) {

        return configChannelGroupService.findChannelList(channelBasicInfoQo);
    }

    /**
     * 查询已配置的通道
     * @param configChannelGroupQo
     * @return
     */
    @RequestMapping(value = "/findConfigChannelGroupList", method = RequestMethod.POST)
    public ResponseData<List<ConfigChannelGroupQo>> findConfigChannelGroupList(@RequestBody ConfigChannelGroupQo configChannelGroupQo) {

        return configChannelGroupService.findConfigChannelGroupList(configChannelGroupQo);
    }

    /**
     * 保存通道组配置
     * @param channelGroupConfigValidator
     * @param op
     * @return
     */
    @RequestMapping(value = "/saveChannelGroupConfig/{op}", method = RequestMethod.POST)
    public ResponseData saveChannelGroupConfig(@RequestBody ChannelGroupConfigValidator channelGroupConfigValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(channelGroupConfigValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(channelGroupConfigValidator));
        }

        //保存操作
        ResponseData data = configChannelGroupService.saveChannelGroupConfig(channelGroupConfigValidator, op);

        return data;
    }

    /**
     * 根据id删除信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteConfigChannelById/{id}", method = RequestMethod.GET)
    public ResponseData deleteConfigChannelById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData data = configChannelGroupService.deleteById(id);
        return data;
    }

}
