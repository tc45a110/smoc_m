package com.smoc.cloud.configure.channel.group.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.qo.ConfigChannelGroupQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelGroupConfigValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelGroupInfoValidator;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.channel.group.service.ChannelGroupService;
import com.smoc.cloud.configure.channel.group.service.ConfigChannelGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 配置通道组管理
 **/
@Slf4j
@RestController
@RequestMapping("/configure/channel/group")
public class ConfigChannelGroupController {

    @Autowired
    private ChannelGroupService channelGroupService;

    @Autowired
    private ConfigChannelGroupService configChannelGroupService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    /**
     * 通道组配置
     *
     * @return
     */
    @RequestMapping(value = "/edit/channel/{id}", method = RequestMethod.GET)
    public ModelAndView channelList(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/group/channel_group_edit_channel");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询通道组数据
        ResponseData<ChannelGroupInfoValidator> data = channelGroupService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        //查询已配置的通道
        ConfigChannelGroupQo configChannelGroupQo = new ConfigChannelGroupQo();
        configChannelGroupQo.setChannelGroupId(id);
        ResponseData<List<ConfigChannelGroupQo>> configChannelGroupDate = configChannelGroupService.findConfigChannelGroupList(configChannelGroupQo);
        if (!ResponseCode.SUCCESS.getCode().equals(configChannelGroupDate.getCode())) {
            view.addObject("error", configChannelGroupDate.getCode() + ":" + configChannelGroupDate.getMessage());
        }

        /**
         * 查询通道列表
         */
        ChannelBasicInfoQo channelBasicInfoQo = new ChannelBasicInfoQo();
        channelBasicInfoQo.setChanneGroupId(id);
        channelBasicInfoQo.setCarrier(data.getData().getCarrier());
        channelBasicInfoQo.setBusinessType(data.getData().getBusinessType());
        channelBasicInfoQo.setInfoType(data.getData().getInfoType());
        channelBasicInfoQo.setChannelStatus("001");//正常
        ResponseData<List<ChannelBasicInfoQo>> listDate = configChannelGroupService.findChannelList(channelBasicInfoQo);
        if (!ResponseCode.SUCCESS.getCode().equals(listDate.getCode())) {
            view.addObject("error", listDate.getCode() + ":" + listDate.getMessage());
        }

        view.addObject("list", listDate.getData());
        view.addObject("configChannelGroupList", configChannelGroupDate.getData());
        view.addObject("channelBasicInfoQo", channelBasicInfoQo);
        view.addObject("channelGroupConfigValidator", new ChannelGroupConfigValidator());
        view.addObject("channelGroupId", id);

        return view;

    }

    /**
     * 通道组配置分页查询
     *
     * @return
     */
    @RequestMapping(value = "/edit/channelPage", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute ChannelBasicInfoQo channelBasicInfoQo, PageParams pageParams) {
        ModelAndView view = new ModelAndView("configure/channel/group/channel_group_edit_channel");

        //查询通道组数据
        ResponseData<ChannelGroupInfoValidator> data = channelGroupService.findById(channelBasicInfoQo.getChannelId());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        //查询已配置的通道
        ConfigChannelGroupQo configChannelGroupQo = new ConfigChannelGroupQo();
        configChannelGroupQo.setChannelGroupId(channelBasicInfoQo.getChannelId());
        ResponseData<List<ConfigChannelGroupQo>> configChannelGroupDate = configChannelGroupService.findConfigChannelGroupList(configChannelGroupQo);
        if (!ResponseCode.SUCCESS.getCode().equals(configChannelGroupDate.getCode())) {
            view.addObject("error", configChannelGroupDate.getCode() + ":" + configChannelGroupDate.getMessage());
        }

        //查询通道列表
        channelBasicInfoQo.setChannelStatus("001");//正常
        channelBasicInfoQo.setChanneGroupId(channelBasicInfoQo.getChannelId());
        ResponseData<List<ChannelBasicInfoQo>> listDate = configChannelGroupService.findChannelList(channelBasicInfoQo);
        if (!ResponseCode.SUCCESS.getCode().equals(listDate.getCode())) {
            view.addObject("error", listDate.getCode() + ":" + listDate.getMessage());
        }

        view.addObject("list", listDate.getData());
        view.addObject("configChannelGroupList", configChannelGroupDate.getData());
        view.addObject("channelBasicInfoQo", channelBasicInfoQo);
        view.addObject("channelGroupConfigValidator", new ChannelGroupConfigValidator());
        view.addObject("channelGroupId", channelBasicInfoQo.getChannelId());
        return view;
    }

    /**
     * 保存通道组配置
     *
     * @return
     */
    @RequestMapping(value = "/saveChannelGroupConfig/{op}", method = RequestMethod.POST)
    public ModelAndView saveChannelGroupConfig(@ModelAttribute ChannelGroupConfigValidator channelGroupConfigValidator, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("configure/channel/group/channel_group_edit_channel");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //查询通道组数据
        ResponseData<ChannelGroupInfoValidator> responseData = channelGroupService.findById(channelGroupConfigValidator.getChannelGroupId());
        if (!ResponseCode.SUCCESS.getCode().equals(responseData.getCode())) {
            view.addObject("error", responseData.getCode() + ":" + responseData.getMessage());
        }

        /**
         * 保存通道组配置
         */
        if(StringUtils.isEmpty(channelGroupConfigValidator.getId())){
            channelGroupConfigValidator.setId(UUID.uuid32());
        }else{
            op = "edit";
        }
        channelGroupConfigValidator.setCreatedBy(user.getRealName());
        channelGroupConfigValidator.setCreatedTime(new Date());
        ResponseData data = configChannelGroupService.saveChannelGroupConfig(channelGroupConfigValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //记录日志
        log.info("[通道组管理][通道组配置][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(channelGroupConfigValidator));

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("CHANNEL_GROUIP_CONFIG", channelGroupConfigValidator.getChannelGroupId(), channelGroupConfigValidator.getCreatedBy(), op, "add".equals(op) ? "添加通道组配置" : "修改通道组权重", JSON.toJSONString(channelGroupConfigValidator));
        }

        //查询已配置的通道
        ConfigChannelGroupQo configChannelGroupQo = new ConfigChannelGroupQo();
        configChannelGroupQo.setChannelGroupId(channelGroupConfigValidator.getChannelGroupId());
        ResponseData<List<ConfigChannelGroupQo>> configChannelGroupDate = configChannelGroupService.findConfigChannelGroupList(configChannelGroupQo);
        if (!ResponseCode.SUCCESS.getCode().equals(configChannelGroupDate.getCode())) {
            view.addObject("error", configChannelGroupDate.getCode() + ":" + configChannelGroupDate.getMessage());
        }

        /**
         * 查询通道列表
         */
        ChannelBasicInfoQo channelBasicInfoQo = new ChannelBasicInfoQo();
        channelBasicInfoQo.setChanneGroupId(responseData.getData().getChannelGroupId());
        channelBasicInfoQo.setCarrier(responseData.getData().getCarrier());
        channelBasicInfoQo.setBusinessType(responseData.getData().getBusinessType());
        channelBasicInfoQo.setInfoType(responseData.getData().getInfoType());
        channelBasicInfoQo.setChannelStatus("001");//正常
        ResponseData<List<ChannelBasicInfoQo>> listDate = configChannelGroupService.findChannelList(channelBasicInfoQo);
        if (!ResponseCode.SUCCESS.getCode().equals(listDate.getCode())) {
            view.addObject("error", listDate.getCode() + ":" + listDate.getMessage());
        }

        view.addObject("list", listDate.getData());
        view.addObject("configChannelGroupList", configChannelGroupDate.getData());
        view.addObject("channelBasicInfoQo", channelBasicInfoQo);
        view.addObject("channelGroupConfigValidator", new ChannelGroupConfigValidator());
        view.addObject("channelGroupId", channelGroupConfigValidator.getChannelGroupId());

        return view;
    }

    /**
     * 移除已配置通道
     *
     * @return
     */
    @RequestMapping(value = "/deleteConfigChannelById/{id}", method = RequestMethod.GET)
    public ModelAndView deleteConfigChannelById(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/group/channel_group_edit_channel");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询配置通道数据
        ResponseData<ConfigChannelGroupQo> responseData = configChannelGroupService.findConfigChannelById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(responseData.getCode())) {
            view.addObject("error", responseData.getCode() + ":" + responseData.getMessage());
        }

        //记录日志
        log.info("[通道组管理][移除通道组配置][delete][{}]数据:{}", user.getUserName(), JSON.toJSONString(id));

        //删除操作
        ResponseData data = configChannelGroupService.deleteConfigChannelById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("CHANNEL_GROUIP_CONFIG", responseData.getData().getChannelGroupId(), user.getRealName(), "delete", "移除通道组配置", JSON.toJSONString(responseData));
        }

        view.setView(new RedirectView("/configure/channel/group/edit/channel/" + responseData.getData().getChannelGroupId(), true, false));

        return view;

    }

    /**
     * 查看已配置通道数
     *
     * @return
     */
    @RequestMapping(value = "/configChannelNum/{id}", produces = "text/html;charset=utf-8", method = RequestMethod.GET)
    public String configChannelNum(@PathVariable String id, HttpServletRequest request) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return MpmValidatorUtil.validateMessage(validator);
        }

        //查看通道组数据
        ResponseData<ChannelGroupInfoValidator> data = channelGroupService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return data.getMessage();
        }

        //查询已配置的通道
        ConfigChannelGroupQo configChannelGroupQo = new ConfigChannelGroupQo();
        configChannelGroupQo.setChannelGroupId(id);
        ResponseData<List<ConfigChannelGroupQo>> configChannelGroupDate = configChannelGroupService.findConfigChannelGroupList(configChannelGroupQo);
        if (!ResponseCode.SUCCESS.getCode().equals(configChannelGroupDate.getCode())) {
            return configChannelGroupDate.getMessage();
        }

        List<ConfigChannelGroupQo> list = configChannelGroupDate.getData();
        if (StringUtils.isEmpty(list) || list.size() <= 0) {
            return "无配置通道";
        }

        //封装已配置的通道数据
        StringBuilder configChannelName = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            ConfigChannelGroupQo qo = list.get(i);
            configChannelName.append(qo.getChannelName() + " 优先级：" + qo.getChannelPriority() + "，权重：" + qo.getChannelWeight() + "；");
            if (i != list.size()-1) {
                configChannelName.append("@");
            }
        }

        return configChannelName.toString();
    }

}
