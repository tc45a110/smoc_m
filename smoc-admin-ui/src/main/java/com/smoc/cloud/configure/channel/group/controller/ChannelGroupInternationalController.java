package com.smoc.cloud.configure.channel.group.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelGroupInfoValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.channel.group.service.ChannelGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 国际通道组管理
 **/
@Slf4j
@RestController
@RequestMapping("/configure/channel/group/international")
public class ChannelGroupInternationalController {

    @Autowired
    private ChannelGroupService channelGroupService;


    /**
     * 通道组管理列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/international/channel_group_international_list");

        ///初始化数据
        PageParams<ChannelGroupInfoValidator> params = new PageParams<ChannelGroupInfoValidator>();
        params.setPageSize(8);
        params.setCurrentPage(1);
        ChannelGroupInfoValidator channelGroupInfoValidator = new ChannelGroupInfoValidator();
        channelGroupInfoValidator.setCarrier("INTERNATIONAL");
        params.setParams(channelGroupInfoValidator);

        //查询
        ResponseData<PageList<ChannelGroupInfoValidator>> data = channelGroupService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("channelGroupInfoValidator", channelGroupInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }

    /**
     * 通道组分页查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute ChannelGroupInfoValidator channelGroupInfoValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("configure/channel/international/channel_group_international_list");

        //分页查询
        channelGroupInfoValidator.setCarrier("INTERNATIONAL");
        pageParams.setParams(channelGroupInfoValidator);

        ResponseData<PageList<ChannelGroupInfoValidator>> data = channelGroupService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("channelGroupInfoValidator", channelGroupInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;

    }

    /**
     * 通道组维护中心
     *
     * @return
     */
    @RequestMapping(value = "/edit/center/{id}", method = RequestMethod.GET)
    public ModelAndView edit_center(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/international/channel_group_international_edit_center");

        view.addObject("id", id);

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        return view;

    }

    /**
     * 通道组基本信息维护
     *
     * @return
     */
    @RequestMapping(value = "/edit/base/{id}", method = RequestMethod.GET)
    public ModelAndView base(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/international/channel_group_international_edit_base");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        /**
         * id为base是添加功能
         */
        if ("base".equals(id)) {
            ChannelGroupInfoValidator channelGroupInfoValidator = new ChannelGroupInfoValidator();
            channelGroupInfoValidator.setCarrier("INTERNATIONAL");
            channelGroupInfoValidator.setBusinessType("INTERNATIONAL_SMS");
            channelGroupInfoValidator.setChannelGroupStatus("1");//状态
            channelGroupInfoValidator.setChannelGroupProcess("10");//配置进度

            //op操作标记，add表示添加，edit表示修改
            view.addObject("op", "add");
            view.addObject("channelGroupInfoValidator", channelGroupInfoValidator);
            return view;
        }

        /**
         * 修改:查询数据
         */
        ResponseData<ChannelGroupInfoValidator> data = channelGroupService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "edit");
        view.addObject("channelGroupInfoValidator", data.getData());

        return view;
    }

}
