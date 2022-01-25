package com.smoc.cloud.customer.controller;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.customer.qo.AccountChannelInfoQo;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.AccountChannelInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.customer.service.AccountChannelService;
import com.smoc.cloud.customer.service.BusinessAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * EC业务账号通道管理
 */
@Slf4j
@Controller
@RequestMapping("/account")
public class AccountChannelController {

    @Autowired
    private BusinessAccountService businessAccountService;

    @Autowired
    private AccountChannelService accountChannelService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    /**
     * 业务账号通道配置
     *
     * @return
     */
    @RequestMapping(value = "/edit/channel/{accountId}", method = RequestMethod.GET)
    public ModelAndView edit_channel(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_edit_channel");

        //查询业务账号
        ResponseData<AccountBasicInfoValidator> data = businessAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        AccountChannelInfoQo accountChannelInfoQo = new AccountChannelInfoQo();
        accountChannelInfoQo.setAccountId(data.getData().getAccountId());
        accountChannelInfoQo.setCarrier(data.getData().getCarrier());

        //查询配置的业务账号通道
        ResponseData<Map<String, AccountChannelInfoQo>> channelData = accountChannelService.findAccountChannelConfig(accountChannelInfoQo);
        if (!ResponseCode.SUCCESS.getCode().equals(channelData.getCode())) {
            view.addObject("error", channelData.getCode() + ":" + channelData.getMessage());
            return view;
        }

        //初始化查询条件
        ChannelBasicInfoQo channelBasicInfoQo = new ChannelBasicInfoQo();
        channelBasicInfoQo.setAccountId(data.getData().getAccountId());

        view.addObject("channelMap", channelData.getData());
        view.addObject("accountBasicInfoValidator", data.getData());
        view.addObject("channelBasicInfoQo", channelBasicInfoQo);
        view.addObject("accountChannelInfoValidator", new AccountChannelInfoValidator());
        return view;

    }

    /**
     * 检索通道
     * @param accountId
     * @param carrier
     * @param request
     * @return
     */
    @RequestMapping(value = "/channel/findChannelList/{accountId}/{carrier}", method = RequestMethod.GET)
    public ModelAndView findChannelList(@PathVariable String accountId, @PathVariable String carrier, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_edit_channel");

        //查询业务账号
        ResponseData<AccountBasicInfoValidator> data = businessAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        /**
         * 查询配置的业务账号通道
         */
        AccountChannelInfoQo accountChannelInfoQo = new AccountChannelInfoQo();
        accountChannelInfoQo.setAccountId(data.getData().getAccountId());
        accountChannelInfoQo.setCarrier(data.getData().getCarrier());
        ResponseData<Map<String, AccountChannelInfoQo>> channelData = accountChannelService.findAccountChannelConfig(accountChannelInfoQo);
        if (!ResponseCode.SUCCESS.getCode().equals(channelData.getCode())) {
            view.addObject("error", channelData.getCode() + ":" + channelData.getMessage());
            return view;
        }

        /**
         * 检索通道列表
         */
        ChannelBasicInfoQo channelBasicInfoQo = new ChannelBasicInfoQo();
        channelBasicInfoQo.setAccountId(data.getData().getAccountId());
        channelBasicInfoQo.setCarrier(carrier);
        channelBasicInfoQo.setBusinessType(data.getData().getBusinessType());
        channelBasicInfoQo.setInfoType(data.getData().getInfoType());
        channelBasicInfoQo.setChannelStatus("001");//正常
        ResponseData<List<ChannelBasicInfoQo>> listDate = accountChannelService.findChannelList(channelBasicInfoQo);
        if (!ResponseCode.SUCCESS.getCode().equals(listDate.getCode())) {
            view.addObject("error", listDate.getCode() + ":" + listDate.getMessage());
        }


        AccountChannelInfoValidator accountChannelInfoValidator = new AccountChannelInfoValidator();
        accountChannelInfoValidator.setCarrier(carrier);
        accountChannelInfoValidator.setAccountId(data.getData().getAccountId());

        view.addObject("channelMap", channelData.getData());
        view.addObject("list", listDate.getData());
        view.addObject("accountBasicInfoValidator", data.getData());
        view.addObject("channelBasicInfoQo", channelBasicInfoQo);
        view.addObject("accountChannelInfoValidator", accountChannelInfoValidator);

        return view;

    }


    /**
     * 检索通道-查询
     * @param channelBasicInfoQo
     * @param request
     * @return
     */
    @RequestMapping(value = "/channel/channelPage", method = RequestMethod.POST)
    public ModelAndView channelPage(@ModelAttribute ChannelBasicInfoQo channelBasicInfoQo, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_edit_channel");

        //查询业务账号
        ResponseData<AccountBasicInfoValidator> data = businessAccountService.findById(channelBasicInfoQo.getAccountId());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        /**
         * 查询配置的业务账号通道
         */
        AccountChannelInfoQo accountChannelInfoQo = new AccountChannelInfoQo();
        accountChannelInfoQo.setAccountId(data.getData().getAccountId());
        accountChannelInfoQo.setCarrier(data.getData().getCarrier());
        ResponseData<Map<String, AccountChannelInfoQo>> channelData = accountChannelService.findAccountChannelConfig(accountChannelInfoQo);
        if (!ResponseCode.SUCCESS.getCode().equals(channelData.getCode())) {
            view.addObject("error", channelData.getCode() + ":" + channelData.getMessage());
            return view;
        }

        /**
         * 检索通道列表
         */
        channelBasicInfoQo.setChannelStatus("001");//正常
        ResponseData<List<ChannelBasicInfoQo>> listDate = accountChannelService.findChannelList(channelBasicInfoQo);
        if (!ResponseCode.SUCCESS.getCode().equals(listDate.getCode())) {
            view.addObject("error", listDate.getCode() + ":" + listDate.getMessage());
        }

        AccountChannelInfoValidator accountChannelInfoValidator = new AccountChannelInfoValidator();
        accountChannelInfoValidator.setCarrier(channelBasicInfoQo.getCarrier());
        accountChannelInfoValidator.setAccountId(data.getData().getAccountId());

        view.addObject("channelMap", channelData.getData());
        view.addObject("list", listDate.getData());
        view.addObject("accountBasicInfoValidator", data.getData());
        view.addObject("channelBasicInfoQo", channelBasicInfoQo);
        view.addObject("accountChannelInfoValidator", accountChannelInfoValidator);

        return view;

    }

    /**
     * 业务账号通道保存
     * @param accountChannelInfoValidator
     * @param request
     * @return
     */
    @RequestMapping(value = "/channel/save", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute AccountChannelInfoValidator accountChannelInfoValidator, BindingResult result, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_edit_channel");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("accountChannelInfoValidator", accountChannelInfoValidator);
            return view;
        }

        //查询账号下得运营商是否配置过通道
        ResponseData<AccountChannelInfoValidator> infoData = accountChannelService.findByAccountIdAndCarrier(accountChannelInfoValidator.getAccountId(),accountChannelInfoValidator.getCarrier());
        if (ResponseCode.SUCCESS.getCode().equals(infoData.getCode()) && !StringUtils.isEmpty(infoData.getData())) {
            view.addObject("error", "该业务账号下的运营商已配置过通道");
            return view;
        }

        //查询业务账号
        ResponseData<AccountBasicInfoValidator> data = businessAccountService.findById(accountChannelInfoValidator.getAccountId());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存账号通道
        String op= "add";
        accountChannelInfoValidator.setId(UUID.uuid32());
        accountChannelInfoValidator.setConfigType("1");
        accountChannelInfoValidator.setChannelPriority("1");//等级
        accountChannelInfoValidator.setChannelSource("1");//优先级
        accountChannelInfoValidator.setChannelStatus("1");
        accountChannelInfoValidator.setCreatedTime(new Date());
        accountChannelInfoValidator.setCreatedBy(user.getRealName());
        ResponseData resData = accountChannelService.save(accountChannelInfoValidator,op);
        if (!ResponseCode.SUCCESS.getCode().equals(resData.getCode())) {
            view.addObject("error", resData.getCode() + ":" + resData.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("ACCOUNT_CHANNEL", accountChannelInfoValidator.getAccountId(), accountChannelInfoValidator.getCreatedBy() , op,  "添加业务账号通道配置", JSON.toJSONString(accountChannelInfoValidator));
        }

        //记录日志
        log.info("[EC业务账号管理][业务账号通道配置][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(accountChannelInfoValidator));

        view.setView(new RedirectView("/account/channel/findChannelList/"+accountChannelInfoValidator.getAccountId()+"/"+accountChannelInfoValidator.getCarrier(), true, false));

        return view;

    }


}
