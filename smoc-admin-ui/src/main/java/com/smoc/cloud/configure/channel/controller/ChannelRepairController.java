package com.smoc.cloud.configure.channel.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.*;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.channel.service.ChannelRepairService;
import com.smoc.cloud.configure.channel.service.ChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 通道补发管理
 **/
@Slf4j
@RestController
@RequestMapping("/configure/channel/repair")
public class ChannelRepairController {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private ChannelRepairService channelRepairService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    /**
     * 通道补发管理列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_repair/channel_repair_list");

        ///初始化数据
        PageParams<ConfigChannelRepairValidator> params = new PageParams<ConfigChannelRepairValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        ConfigChannelRepairValidator configChannelRepairValidator = new ConfigChannelRepairValidator();
        params.setParams(configChannelRepairValidator);

        //查询
        ResponseData<PageList<ConfigChannelRepairValidator>> data = channelRepairService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("configChannelRepairValidator", configChannelRepairValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }

    /**
     * 通道补发分页查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute ConfigChannelRepairValidator configChannelRepairValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("configure/channel/channel_repair/channel_repair_list");

        //分页查询
        pageParams.setParams(configChannelRepairValidator);

        ResponseData<PageList<ConfigChannelRepairValidator>> data = channelRepairService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("configChannelRepairValidator", configChannelRepairValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }

    /**
     * 失败补发页面
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView detail(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_repair/channel_repair_edit");

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

        //根据运营商、业务类型、信息分类查询符合要求的备用通道
        ConfigChannelRepairValidator configChannelRepairValidator = new ConfigChannelRepairValidator();
        configChannelRepairValidator.setBusinessType(data.getData().getBusinessType());
        configChannelRepairValidator.setChannelId(data.getData().getChannelId());
        configChannelRepairValidator.setFlag("CHANNEL");
        ResponseData<List<ConfigChannelRepairValidator>> channelList = channelRepairService.findSpareChannel(configChannelRepairValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(channelList.getCode())) {
            view.addObject("error", channelList.getCode() + ":" + channelList.getMessage());
            return view;
        }

        //初始化数据
        ConfigChannelRepairRuleValidator configChannelRepairRuleValidator = new ConfigChannelRepairRuleValidator();
        configChannelRepairRuleValidator.setChannelId(data.getData().getChannelId());
        configChannelRepairRuleValidator.setBusinessType("CHANNEL");

        //查询已经存在的备用通道
        ResponseData<List<ConfigChannelRepairRuleValidator>> channelRepairList = channelRepairService.findChannelRepairByChannelId(configChannelRepairRuleValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(channelRepairList.getCode())) {
            view.addObject("error", channelRepairList.getCode() + ":" + channelRepairList.getMessage());
            return view;
        }

        view.addObject("channelList", channelList.getData());
        view.addObject("channelRepairList", channelRepairList.getData());
        view.addObject("channelBasicInfoValidator", data.getData());
        view.addObject("configChannelRepairRuleValidator", configChannelRepairRuleValidator);

        return view;
    }

    /**
     * 保存
     *
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated ConfigChannelRepairRuleValidator configChannelRepairRuleValidator, @PathVariable String op, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_repair/channel_repair_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(configChannelRepairRuleValidator.getChannelId());
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询通道是否存在
        ResponseData<ChannelBasicInfoValidator> data = channelService.findById(configChannelRepairRuleValidator.getChannelId());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作
        configChannelRepairRuleValidator.setBusinessId(configChannelRepairRuleValidator.getChannelId());
        configChannelRepairRuleValidator.setId(UUID.uuid32());
        configChannelRepairRuleValidator.setBusinessType("CHANNEL");
        configChannelRepairRuleValidator.setCreatedBy(user.getRealName());
        configChannelRepairRuleValidator.setRepairStatus("1");
        ResponseData repairData = channelRepairService.save(configChannelRepairRuleValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(repairData.getCode())) {
            view.addObject("error", repairData.getCode() + ":" + repairData.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(repairData.getCode())) {
            systemUserLogService.logsAsync("CHANNEL_BASE", configChannelRepairRuleValidator.getChannelId(), "add".equals(op) ? user.getRealName() : user.getRealName(), op, "add".equals(op) ? "添加失败补发通道" : "修改失败补发通道", JSON.toJSONString(configChannelRepairRuleValidator));
        }

        //记录日志
        log.info("[失败补发通道管理][补发配置][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(configChannelRepairRuleValidator));

        view.setView(new RedirectView("/configure/channel/repair/edit/"+configChannelRepairRuleValidator.getChannelId(), true, false));
        return view;
    }

    /**
     * 删除信息
     *
     * @return
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ModelAndView deleteById(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/contract/customer_contract_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询信息
        ResponseData<ConfigChannelRepairRuleValidator> infoDate = channelRepairService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(infoDate.getCode())) {
            view.addObject("error", infoDate.getCode() + ":" + infoDate.getMessage());
            return view;
        }

        //删除操作
        ResponseData data = channelRepairService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("CHANNEL_BASE", infoDate.getData().getChannelId(), user.getRealName(), "delete", "删除失败补发通道" , JSON.toJSONString(infoDate.getData()));
        }

        //记录日志
        log.info("[失败补发通道管理][补发配置][{}]数据:{}", "delete", user.getUserName(), JSON.toJSONString(infoDate.getData()));

        view.setView(new RedirectView("/configure/channel/repair/edit/"+infoDate.getData().getChannelId(), true, false));
        return view;
    }
}
