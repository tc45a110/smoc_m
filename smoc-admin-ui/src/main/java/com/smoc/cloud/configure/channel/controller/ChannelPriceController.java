package com.smoc.cloud.configure.channel.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelPriceValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.channel.service.ChannelPriceService;
import com.smoc.cloud.configure.channel.service.ChannelService;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 通道价格管理
 **/
@Slf4j
@Controller
@RequestMapping("/configure/channel")
public class ChannelPriceController {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private ChannelPriceService channelPriceService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    /**
     * 计价设置
     *
     * @return
     */
    @RequestMapping(value = "/edit/price/{id}", method = RequestMethod.GET)
    public ModelAndView price(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_edit_price");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询数据
        ResponseData<ChannelBasicInfoValidator> data = channelService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        ChannelBasicInfoValidator channelBasicInfoValidator = data.getData();

        String supportAreaCodes = "";
        //分省
        if ("PROVINCE".equals(channelBasicInfoValidator.getBusinessAreaType())) {
            supportAreaCodes = channelBasicInfoValidator.getProvince();
        }
        //国际
        if ("INTERNATIONAL".equals(channelBasicInfoValidator.getBusinessAreaType())) {
            supportAreaCodes = channelBasicInfoValidator.getSupportAreaCodes();
        }

        //如果不是全国，才会去查区域价格数据
        ChannelPriceValidator channelPriceValidator = new ChannelPriceValidator();
        if (!"COUNTRY".equals(channelBasicInfoValidator.getBusinessAreaType())) {
            channelPriceValidator.setChannelId(channelBasicInfoValidator.getChannelId());
            channelPriceValidator.setAreaCode(supportAreaCodes);

            //根据通道id和区域查询价格
            ResponseData<List<ChannelPriceValidator>> list = channelPriceService.findByChannelIdAndAreaCode(channelPriceValidator);
            if (!StringUtils.isEmpty(list)) {
                view.addObject("op", "edit");
            } else {
                view.addObject("op", "add");
            }

            //根据通道id和区域编号封装数据
            ResponseData<Map<String, BigDecimal>> map = channelPriceService.editChannelPrice(channelPriceValidator);

            view.addObject("list", map.getData());
            view.addObject("supportAreaCodes", channelBasicInfoValidator.getBusinessAreaType());

        }

        view.addObject("channelPriceValidator", channelPriceValidator);

        return view;
    }

    /**
     * 区域计价保存
     *
     * @return
     */
    @RequestMapping(value = "/priceSave/{op}", method = RequestMethod.POST)
    public ModelAndView priceSave(@ModelAttribute @Validated ChannelPriceValidator channelPriceValidator, @PathVariable String op, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_edit_price");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(channelPriceValidator.getChannelId());
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询通道是否存在
        ResponseData<ChannelBasicInfoValidator> data = channelService.findById(channelPriceValidator.getChannelId());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        String supportAreaCodes = "";
        //分省
        if ("PROVINCE".equals(data.getData().getBusinessAreaType())) {
            supportAreaCodes = data.getData().getProvince();
        }
        //国际
        if ("INTERNATIONAL".equals(data.getData().getBusinessAreaType())) {
            supportAreaCodes = data.getData().getSupportAreaCodes();
        }

        //如果区域为空直接返回
        if (StringUtils.isEmpty(supportAreaCodes)) {
            return view;
        }

        //封装区域价格数据
        List<ChannelPriceValidator> list = new ArrayList<>();
        String[] areaCodes = supportAreaCodes.split(",");
        for (int i = 0; i < areaCodes.length; i++) {
            ChannelPriceValidator channelPrice = new ChannelPriceValidator();
            channelPrice.setAreaCode(areaCodes[i]);
            channelPrice.setChannelPrice(new BigDecimal(request.getParameter(areaCodes[i])));
            list.add(channelPrice);
        }

        //保存数据
        channelPriceValidator.setPrices(list);
        channelPriceValidator.setPriceStyle(data.getData().getPriceStyle());
        ResponseData pricedata = channelPriceService.savePrice(channelPriceValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(pricedata.getCode())) {
            view.addObject("error", pricedata.getCode() + ":" + pricedata.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(pricedata.getCode())) {
            systemUserLogService.logsAsync("CHANNEL_PRICE", channelPriceValidator.getChannelId(), "add".equals(op) ? user.getRealName() : user.getRealName(), op, "add".equals(op) ? "添加区域计价配置" : "修改区域计价配置", JSON.toJSONString(channelPriceValidator));
        }

        //记录日志
        log.info("[通道管理][区域计价配置][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(channelPriceValidator));

        //根据通道id和区域编号查询通道价格
        channelPriceValidator.setAreaCode(supportAreaCodes);
        ResponseData<Map<String, BigDecimal>> map = channelPriceService.editChannelPrice(channelPriceValidator);
        view.addObject("list", map.getData());
        view.addObject("supportAreaCodes", data.getData().getBusinessAreaType());

        view.addObject("channelPriceValidator", channelPriceValidator);
        view.addObject("op", "edit");

        return view;
    }


}
