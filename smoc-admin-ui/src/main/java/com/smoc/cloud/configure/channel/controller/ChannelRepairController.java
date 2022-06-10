package com.smoc.cloud.configure.channel.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.*;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.channel.service.ChannelRepairService;
import com.smoc.cloud.configure.channel.service.ChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
        ResponseData<List<ConfigChannelRepairValidator>> channelList = channelRepairService.findSpareChannel(data.getData());
        if (!ResponseCode.SUCCESS.getCode().equals(channelList.getCode())) {
            view.addObject("error", channelList.getCode() + ":" + channelList.getMessage());
            return view;
        }

        //初始化备用通道
        ResponseData<Map<String,ConfigChannelRepairRuleValidator>> map = channelRepairService.editRepairRule(data.getData().getChannelId());
        if (!ResponseCode.SUCCESS.getCode().equals(map.getCode())) {
            view.addObject("error", map.getCode() + ":" + map.getMessage());
            return view;
        }
        ConfigChannelRepairValidator configChannelRepairValidator = new ConfigChannelRepairValidator();
        configChannelRepairValidator.setChannelId(data.getData().getChannelId());

        view.addObject("channelList", channelList.getData());
        view.addObject("channelBasicInfoValidator", data.getData());
        view.addObject("repairRuleList", map.getData());
        view.addObject("configChannelRepairValidator", configChannelRepairValidator);

        return view;
    }

    /**
     * 保存
     *
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated ConfigChannelRepairValidator configChannelRepairValidator, @PathVariable String op, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_repair/channel_repair_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(configChannelRepairValidator.getChannelId());
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询通道是否存在
        ResponseData<ChannelBasicInfoValidator> data = channelService.findById(configChannelRepairValidator.getChannelId());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        List<ConfigChannelRepairRuleValidator> repairList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            ConfigChannelRepairRuleValidator configChannelRepairRuleValidator = new ConfigChannelRepairRuleValidator();
            if(!StringUtils.isEmpty(request.getParameter("channelRepairId"+i))){
                configChannelRepairRuleValidator.setChannelRepairId(request.getParameter("channelRepairId"+i));
                configChannelRepairRuleValidator.setRepairCode(request.getParameter("repairCode"+i));
                configChannelRepairRuleValidator.setChannelId(configChannelRepairValidator.getChannelId());
                configChannelRepairRuleValidator.setBusinessId(configChannelRepairValidator.getChannelId());
                configChannelRepairRuleValidator.setBusinessType("CHANNEL");
                configChannelRepairRuleValidator.setCreatedBy(user.getRealName());
                configChannelRepairRuleValidator.setRepairStatus("1");
                repairList.add(configChannelRepairRuleValidator);
            }
        }

        //如果为空，直接返回
        if(StringUtils.isEmpty(repairList) || repairList.size()<=0){
            view.setView(new RedirectView("/configure/channel/repair/list", true, false));
            return view;
        }

        //保存数据
        configChannelRepairValidator.setChannelId(configChannelRepairValidator.getChannelId());
        configChannelRepairValidator.setBusinessType("CHANNEL");
        configChannelRepairValidator.setRepairList(repairList);
        ResponseData repairData = channelRepairService.save(configChannelRepairValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(repairData.getCode())) {
            view.addObject("error", repairData.getCode() + ":" + repairData.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(repairData.getCode())) {
            systemUserLogService.logsAsync("CHANNEL_BASE", configChannelRepairValidator.getChannelId(), "add".equals(op) ? user.getRealName() : user.getRealName(), op, "add".equals(op) ? "添加失败补发通道" : "修改失败补发通道", JSON.toJSONString(configChannelRepairValidator));
        }

        //记录日志
        log.info("[失败补发通道管理][补发配置][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(configChannelRepairValidator));

        view.setView(new RedirectView("/configure/channel/repair/list", true, false));
        return view;
    }
}
