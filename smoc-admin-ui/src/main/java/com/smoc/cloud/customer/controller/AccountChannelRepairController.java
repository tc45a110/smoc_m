package com.smoc.cloud.customer.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelRepairRuleValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelRepairValidator;
import com.smoc.cloud.common.smoc.customer.qo.AccountChannelRepairQo;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * EC业务账号失败补发管理
 */
@Slf4j
@RestController
@RequestMapping("/account/channel/repair")
public class AccountChannelRepairController {

    @Autowired
    private BusinessAccountService businessAccountService;

    @Autowired
    private AccountChannelRepairService accountChannelRepairService;

    /**
     * 业务账号列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("customer/account/account_channel_repair/repair_list");

        //初始化数据
        PageParams<AccountChannelRepairQo> params = new PageParams<AccountChannelRepairQo>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        AccountChannelRepairQo accountChannelRepairQo = new AccountChannelRepairQo();
        params.setParams(accountChannelRepairQo);

        //查询
        ResponseData<PageList<AccountChannelRepairQo>> data = accountChannelRepairService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("accountChannelRepairQo", accountChannelRepairQo);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 业务账号列表查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute AccountChannelRepairQo accountChannelRepairQo, PageParams pageParams) {
        ModelAndView view = new ModelAndView("customer/account/account_channel_repair/repair_list");

        //分页查询
        pageParams.setParams(accountChannelRepairQo);

        ResponseData<PageList<AccountChannelRepairQo>> data = accountChannelRepairService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("accountChannelRepairQo", accountChannelRepairQo);
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

        ModelAndView view = new ModelAndView("customer/account/account_channel_repair/repair_edit");

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
        ResponseData<AccountBasicInfoValidator> data = businessAccountService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }
/*
        //根据运营商、业务类型、信息分类查询符合要求的备用通道
        ResponseData<List<ConfigChannelRepairValidator>> channelList = accountChannelRepairService.findSpareChannel(data.getData());
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
        view.addObject("configChannelRepairRuleValidator", configChannelRepairRuleValidator);*/

        return view;
    }
}
