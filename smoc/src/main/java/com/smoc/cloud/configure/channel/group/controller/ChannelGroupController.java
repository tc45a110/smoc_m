package com.smoc.cloud.configure.channel.group.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelGroupInfoValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.channel.group.service.ChannelGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;


/**
 * 通道组接口
 **/
@Slf4j
@RestController
@RequestMapping("configure/channel/group")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class ChannelGroupController {

    @Autowired
    private ChannelGroupService channelGroupService;

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public PageList<ChannelGroupInfoValidator> page(@RequestBody PageParams<ChannelGroupInfoValidator> pageParams) {

        return channelGroupService.page(pageParams);
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

        ResponseData data = channelGroupService.findById(id);
        return data;
    }

    /**
     * 添加、修改
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody ChannelGroupInfoValidator channelGroupInfoValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(channelGroupInfoValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(channelGroupInfoValidator));
        }

        //保存操作
        ResponseData data = channelGroupService.save(channelGroupInfoValidator, op);

        return data;
    }

    /**
     * 通道组详情里已配置通道列表
     * @param channelGroupInfoValidator
     * @return
     */
    @RequestMapping(value = "/centerConfigChannelList", method = RequestMethod.POST)
    public ResponseData<List<ChannelBasicInfoQo>> centerConfigChannelList(@RequestBody ChannelGroupInfoValidator channelGroupInfoValidator) {

        return channelGroupService.centerConfigChannelList(channelGroupInfoValidator);
    }

}
