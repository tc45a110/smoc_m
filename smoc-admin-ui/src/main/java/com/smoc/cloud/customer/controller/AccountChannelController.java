package com.smoc.cloud.customer.controller;


import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.customer.qo.AccountChannelInfoQo;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.customer.service.AccountChannelService;
import com.smoc.cloud.customer.service.BusinessAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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
        accountChannelInfoQo.setAccountChannelType(data.getData().getAccountChannelType());

        //查询配置的业务账号通道
        ResponseData<Map<String, AccountChannelInfoQo>> channelData = accountChannelService.findAccountChannelConfig(accountChannelInfoQo);
        if (!ResponseCode.SUCCESS.getCode().equals(channelData.getCode())) {
            view.addObject("error", channelData.getCode() + ":" + channelData.getMessage());
            return view;
        }

        view.addObject("channelMap", channelData.getData());
        view.addObject("accountBasicInfoValidator", data.getData());
        view.addObject("channelBasicInfoQo", new ChannelBasicInfoQo());
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
        accountChannelInfoQo.setAccountChannelType(data.getData().getAccountChannelType());
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

        view.addObject("channelMap", channelData.getData());
        view.addObject("list", listDate.getData());
        view.addObject("accountBasicInfoValidator", data.getData());
        view.addObject("channelBasicInfoQo", channelBasicInfoQo);

        return view;

    }


}
