package com.smoc.cloud.configure.channel.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelSpareChannelValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.channel.service.ChannelService;
import com.smoc.cloud.configure.channel.service.ChannelSpareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 补发通道管理
 **/
@Slf4j
@Controller
@RequestMapping("/configure/channel")
public class ChannelSpareController {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private ChannelSpareService channelSpareService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    /**
     * 补发通道设置
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/edit/spare/{id}", method = RequestMethod.GET)
    public ModelAndView detail(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_edit_spare");

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

        //根据原通道属性查询符合要求的备用通道
        ResponseData<List<ConfigChannelSpareChannelValidator>> channelList = channelSpareService.findSpareChannel(data.getData());
        if (!ResponseCode.SUCCESS.getCode().equals(channelList.getCode())) {
            view.addObject("error", channelList.getCode() + ":" + channelList.getMessage());
            return view;
        }
        view.addObject("channelList", channelList.getData());

        //查询备用通道信息
        ResponseData<ConfigChannelSpareChannelValidator> channelSapreData = channelSpareService.findByChannelId(data.getData().getChannelId());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //有值代表是修改
        if (!StringUtils.isEmpty(channelSapreData.getData())) {
            view.addObject("op", "edit");
            view.addObject("configChannelSpareChannelValidator", channelSapreData.getData());
            return view;
        }

        //添加：初始化数据
        ConfigChannelSpareChannelValidator configChannelSpareChannelValidator = new ConfigChannelSpareChannelValidator();
        configChannelSpareChannelValidator.setId(UUID.uuid32());
        configChannelSpareChannelValidator.setChannelId(data.getData().getChannelId());

        view.addObject("configChannelSpareChannelValidator", configChannelSpareChannelValidator);

        return view;
    }

    /**
     * 备用通道信息维护保存
     *
     * @return
     */
    @RequestMapping(value = "/spare/save/{op}", method = RequestMethod.POST)
    public ModelAndView interfaceSave(@ModelAttribute @Validated ConfigChannelSpareChannelValidator configChannelSpareChannelValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_edit_spare");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("configChannelSpareChannelValidator", configChannelSpareChannelValidator);
            view.addObject("op", op);
            return view;
        }

        //查询通道数据是否存在
        ResponseData<ChannelBasicInfoValidator> channelData = channelService.findById(configChannelSpareChannelValidator.getChannelId());
        if (!ResponseCode.SUCCESS.getCode().equals(channelData.getCode())) {
            view.addObject("error", channelData.getCode() + ":" + channelData.getMessage());
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            configChannelSpareChannelValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            configChannelSpareChannelValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            configChannelSpareChannelValidator.setUpdatedTime(new Date());
            configChannelSpareChannelValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //保存数据
        ResponseData data = channelSpareService.save(configChannelSpareChannelValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("CHANNEL_BASE", configChannelSpareChannelValidator.getChannelId(), "add".equals(op) ? configChannelSpareChannelValidator.getCreatedBy() : configChannelSpareChannelValidator.getUpdatedBy(), op, "add".equals(op) ? "添加备用通道" : "修改备用通道", JSON.toJSONString(configChannelSpareChannelValidator));
        }

        //记录日志
        log.info("[通道管理][备用通道配置][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(configChannelSpareChannelValidator));

        //根据原通道属性查询符合要求的备用通道
        ResponseData<List<ConfigChannelSpareChannelValidator>> channelList = channelSpareService.findSpareChannel(channelData.getData());
        if (!ResponseCode.SUCCESS.getCode().equals(channelList.getCode())) {
            view.addObject("error", channelList.getCode() + ":" + channelList.getMessage());
            return view;
        }

        view.addObject("configChannelSpareChannelValidator", configChannelSpareChannelValidator);
        view.addObject("op", "edit");
        view.addObject("channelList", channelList.getData());
        return view;
    }

    /**
     * 重置备用通道为空
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/spare/resetSpareChannel/{id}", method = RequestMethod.GET)
    public ModelAndView resetSpareChannel(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_edit_spare");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "add");

        //查询备用通道信息
        ResponseData<ConfigChannelSpareChannelValidator> channelSapreData = channelSpareService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(channelSapreData.getCode())) {
            view.addObject("error", channelSapreData.getCode() + ":" + channelSapreData.getMessage());
            return view;
        }

        //查询通道数据是否存在
        ResponseData<ChannelBasicInfoValidator> channeldata = channelService.findById(channelSapreData.getData().getChannelId());
        if (!ResponseCode.SUCCESS.getCode().equals(channeldata.getCode())) {
            view.addObject("error", channeldata.getCode() + ":" + channeldata.getMessage());
            return view;
        }

        //重置
        ResponseData data = channelSpareService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("CHANNEL_BASE", channeldata.getData().getChannelId(), user.getRealName(), "delete", "重置备用通道为空" , JSON.toJSONString(channelSapreData.getData()));
        }

        //记录日志
        log.info("[通道管理][备用通道配置][{}][{}]数据:{}", "delete", user.getUserName(), JSON.toJSONString(channelSapreData.getData()));

        //根据原通道属性查询符合要求的备用通道
        ResponseData<List<ConfigChannelSpareChannelValidator>> channelList = channelSpareService.findSpareChannel(channeldata.getData());
        if (!ResponseCode.SUCCESS.getCode().equals(channelList.getCode())) {
            view.addObject("error", channelList.getCode() + ":" + channelList.getMessage());
            return view;
        }
        view.addObject("channelList", channelList.getData());

        //添加：初始化数据
        ConfigChannelSpareChannelValidator configChannelSpareChannelValidator = new ConfigChannelSpareChannelValidator();
        configChannelSpareChannelValidator.setId(UUID.uuid32());
        configChannelSpareChannelValidator.setChannelId(channeldata.getData().getChannelId());

        view.addObject("configChannelSpareChannelValidator", configChannelSpareChannelValidator);

        return view;
    }
}
