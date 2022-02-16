package com.smoc.cloud.configure.channel.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.Dict;
import com.smoc.cloud.common.auth.qo.DictType;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelInterfaceValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelPriceValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.channel.service.ChannelInterfaceService;
import com.smoc.cloud.configure.channel.service.ChannelService;
import com.smoc.cloud.sequence.service.SequenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 通道接口参数管理
 **/
@Slf4j
@Controller
@RequestMapping("/configure/channel")
public class ChannelInterfaceController {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private ChannelInterfaceService channelInterfaceService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    /**
     * 通道接口信息维护
     *
     * @return
     */
    @RequestMapping(value = "/edit/interface/{id}", method = RequestMethod.GET)
    public ModelAndView detail(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_edit_interface");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "add");

        //查询通道数据是否存在
        ResponseData<ChannelBasicInfoValidator> data = channelService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询通道接口参数
        ResponseData<ChannelInterfaceValidator> channelInterfaceData = channelInterfaceService.findChannelInterfaceByChannelId(data.getData().getChannelId());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //有值代表是修改
        if (!StringUtils.isEmpty(channelInterfaceData.getData())) {
            view.addObject("op", "edit");
            view.addObject("channelInterfaceValidator", channelInterfaceData.getData());
            return view;
        }

        //添加：初始化数据
        ChannelInterfaceValidator channelInterfaceValidator = new ChannelInterfaceValidator();
        channelInterfaceValidator.setId(UUID.uuid32());
        channelInterfaceValidator.setChannelId(data.getData().getChannelId());

        view.addObject("channelInterfaceValidator", channelInterfaceValidator);

        return view;
    }

    /**
     * 通道接口信息维护保存
     *
     * @return
     */
    @RequestMapping(value = "/interfaceSave/{op}", method = RequestMethod.POST)
    public ModelAndView interfaceSave(@ModelAttribute @Validated ChannelInterfaceValidator channelInterfaceValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_edit_interface");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("channelInterfaceValidator", channelInterfaceValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            channelInterfaceValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            channelInterfaceValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            channelInterfaceValidator.setUpdatedTime(new Date());
            channelInterfaceValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //保存数据
        ResponseData data = channelInterfaceService.interfaceSave(channelInterfaceValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("CHANNEL_BASE", channelInterfaceValidator.getChannelId(), "add".equals(op) ? channelInterfaceValidator.getCreatedBy() : channelInterfaceValidator.getUpdatedBy(), op, "add".equals(op) ? "添加通道接口参数" : "修改通道接口参数", JSON.toJSONString(channelInterfaceValidator));
        }

        //记录日志
        log.info("[通道管理][通道接口参数][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(channelInterfaceValidator));

        view.addObject("channelInterfaceValidator", channelInterfaceValidator);
        view.addObject("op", "edit");

        return view;
    }

}
