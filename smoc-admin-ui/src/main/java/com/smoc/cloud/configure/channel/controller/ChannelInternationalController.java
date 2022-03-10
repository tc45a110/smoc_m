package com.smoc.cloud.configure.channel.controller;

import com.smoc.cloud.admin.security.remote.service.SysUserService;
import com.smoc.cloud.common.auth.qo.Users;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.channel.service.ChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 国际通道管理
 **/
@Slf4j
@RestController
@RequestMapping("/configure/channel/international")
public class ChannelInternationalController {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 国际通道管理列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/international/channel_international_list");

        ///初始化数据
        PageParams<ChannelBasicInfoQo> params = new PageParams<ChannelBasicInfoQo>();
        params.setPageSize(8);
        params.setCurrentPage(1);
        ChannelBasicInfoQo channelBasicInfoQo = new ChannelBasicInfoQo();
        channelBasicInfoQo.setCarrier("INTL");
        params.setParams(channelBasicInfoQo);

        //查询
        ResponseData<PageList<ChannelBasicInfoQo>> data = channelService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询销售
        List<Users> list = sysUserService.salesList();
        Map<String, Users> salesMap = new HashMap<>();
        if(!StringUtils.isEmpty(list) && list.size()>0){
            salesMap = list.stream().collect(Collectors.toMap(Users::getId, Function.identity()));
        }

        view.addObject("channelBasicInfoQo", channelBasicInfoQo);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("salesMap", salesMap);

        return view;
    }

    /**
     * 国际通道分页查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute ChannelBasicInfoQo channelBasicInfoQo, PageParams pageParams) {
        ModelAndView view = new ModelAndView("configure/channel/international/channel_international_list");

        //分页查询
        channelBasicInfoQo.setCarrier("INTL");
        pageParams.setParams(channelBasicInfoQo);

        ResponseData<PageList<ChannelBasicInfoQo>> data = channelService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询销售
        List<Users> list = sysUserService.salesList();
        Map<String, Users> salesMap = new HashMap<>();
        if(!StringUtils.isEmpty(list) && list.size()>0){
            salesMap = list.stream().collect(Collectors.toMap(Users::getId, Function.identity()));
        }

        view.addObject("channelBasicInfoQo", channelBasicInfoQo);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("salesMap", salesMap);

        return view;
    }

    /**
     * 国际通道维护中心
     *
     * @return
     */
    @RequestMapping(value = "/edit/center/{id}", method = RequestMethod.GET)
    public ModelAndView edit_center(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/international/channel_international_edit_center");
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
     * 国际通道基本信息编辑
     *
     * @return
     */
    @RequestMapping(value = "/edit/base/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/international/channel_international_edit_base");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询销售人员
        view.addObject("salesList", sysUserService.salesList());

        /**
         * id为base是添加功能
         */
        if ("base".equals(id)) {
            //初始化数据
            ChannelBasicInfoValidator channelBasicInfoValidator = new ChannelBasicInfoValidator();
            channelBasicInfoValidator.setCarrier("INTL");
            channelBasicInfoValidator.setBusinessType("INTERNATIONAL_SMS");
            channelBasicInfoValidator.setChannelStatus("002");//默认编辑中
            channelBasicInfoValidator.setReportEnable("1");//有无报告：有
            channelBasicInfoValidator.setChannelProcess("1000");//配置进度
            channelBasicInfoValidator.setChannelRunStatus("1");//正常
            channelBasicInfoValidator.setBusinessAreaType("INTL");//国际
            channelBasicInfoValidator.setPriceStyle("AREA_PRICE");//区域计价
            channelBasicInfoValidator.setSignType("0");//签名方式:国际没有，默认给个值
            channelBasicInfoValidator.setTransferEnable("0");//携号转网:国际默认值
            channelBasicInfoValidator.setSpecificProvder("INTL");//具体供应商

            //op操作标记，add表示添加，edit表示修改
            view.addObject("op", "add");
            view.addObject("channelBasicInfoValidator", channelBasicInfoValidator);

            return view;
        }

        /**
         * 修改:查询数据
         */
        ResponseData<ChannelBasicInfoValidator> data = channelService.findChannelById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "edit");
        view.addObject("channelBasicInfoValidator", data.getData());

        return view;
    }
}
