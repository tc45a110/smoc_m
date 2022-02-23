package com.smoc.cloud.configure.channel.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelChangeValidator;
import com.smoc.cloud.common.smoc.customer.validator.AccountChannelInfoValidator;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountValidator;
import com.smoc.cloud.configure.channel.service.ChannelService;
import com.smoc.cloud.configure.channel.service.ConfigChannelChangeItemService;
import com.smoc.cloud.configure.channel.service.ConfigChannelChangeService;
import com.smoc.cloud.customer.service.AccountChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 业务账号通道挂起
 */
@Slf4j
@Controller
@RequestMapping("/configure/channel/suspend")
public class AccountChannelSuspendController {


    @Autowired
    private ChannelService channelService;

    @Autowired
    private AccountChannelService accountChannelService;

    @Autowired
    private ConfigChannelChangeService configChannelChangeService;

    @Autowired
    private ConfigChannelChangeItemService configChannelChangeItemService;

    /**
     * 业务账号通道挂起列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {

        ModelAndView view = new ModelAndView("configure/account_channel_change/account_channel_suspend_list");
        //初始化数据
        PageParams<ConfigChannelChangeValidator> params = new PageParams<ConfigChannelChangeValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        ConfigChannelChangeValidator configChannelChangeValidator = new ConfigChannelChangeValidator();
        configChannelChangeValidator.setChangeType("SUSPEND");
        params.setParams(configChannelChangeValidator);

        //查询
        ResponseData<PageList<ConfigChannelChangeValidator>> data = configChannelChangeService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }
        view.addObject("configChannelChangeValidator", configChannelChangeValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 业务账号通道挂起分页查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute ConfigChannelChangeValidator configChannelChangeValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("configure/account_channel_change/account_channel_suspend_list");

        configChannelChangeValidator.setChangeType("SUSPEND");
        //分页查询
        pageParams.setParams(configChannelChangeValidator);

        ResponseData<PageList<ConfigChannelChangeValidator>> data = configChannelChangeService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("configChannelChangeValidator", configChannelChangeValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;

    }

    /**
     * 业务账号通道挂起明细中心
     *
     * @return
     */
    @RequestMapping(value = "/view/center/{id}", method = RequestMethod.GET)
    public ModelAndView view_center(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/account_channel_change/account_channel_suspend_view_center");

        return view;

    }

    /**
     * 业务账号通道挂起明细
     *
     * @return
     */
    @RequestMapping(value = "/view/base/{id}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/account_channel_change/account_channel_suspend_view");

        return view;

    }

    /**
     * 业务账号通道挂起 添加
     *
     * @return
     */
    @RequestMapping(value = "/add/{channelId}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String channelId, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/account_channel_change/account_channel_suspend_edit");

        //查询通道信息
        PageParams<ChannelBasicInfoQo> params = new PageParams<ChannelBasicInfoQo>();
        params.setPageSize(8);
        params.setCurrentPage(1);
        ChannelBasicInfoQo channelBasicInfoQo = new ChannelBasicInfoQo();
        channelBasicInfoQo.setChannelId(channelId);
        params.setParams(channelBasicInfoQo);

        //查询
        ResponseData<PageList<ChannelBasicInfoQo>> data = channelService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询使用改通道的 业务账号信息
        AccountChannelInfoValidator accountChannelInfoValidator = new AccountChannelInfoValidator();
        //通道ID
        accountChannelInfoValidator.setChannelId(channelId);
        ResponseData<List<AccountChannelInfoValidator>> accountChannelData = accountChannelService.channelDetail(accountChannelInfoValidator);





        view.addObject("channel",data.getData().getList().get(0));
        return view;

    }

    /**
     * 业务账号通道挂起维护
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/account_channel_change/account_channel_suspend_edit");

        return view;

    }


}
