package com.smoc.cloud.configure.channel.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.Dict;
import com.smoc.cloud.common.auth.qo.DictType;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.context.WebEngineContext;

import javax.servlet.ServletContext;
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
@RestController
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

        //如果不是全国，才会去查区域价格数据
        ChannelPriceValidator channelPriceValidator = new ChannelPriceValidator();
        if (!"COUNTRY".equals(channelBasicInfoValidator.getBusinessAreaType())) {
            channelPriceValidator.setChannelId(channelBasicInfoValidator.getChannelId());
            channelPriceValidator.setAreaCode(channelBasicInfoValidator.getSupportAreaCodes());

            //根据通道id和区域查询价格
            ResponseData<List<ChannelPriceValidator>> list = channelPriceService.findChannelPrice(channelPriceValidator);
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

        String supportAreaCodes = data.getData().getSupportAreaCodes();

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

    /**
     * 查询通道价格
     * @param channelId
     * @param request
     * @return
     */
    @RequestMapping(value = "/findChannelPrice/{channelId}", produces = "text/html;charset=utf-8", method = RequestMethod.GET)
    public String findChannelPrice(@PathVariable String channelId, HttpServletRequest request) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(channelId);
        if (!MpmValidatorUtil.validate(validator)) {
            return MpmValidatorUtil.validateMessage(validator);
        }

        //查询通道是否存在
        ResponseData<ChannelBasicInfoValidator> data = channelService.findById(channelId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return data.getMessage();
        }

        //取字典数据
        ServletContext context = request.getServletContext();
        Map<String, DictType> dictMap = (Map<String, DictType>) context.getAttribute("dict");
        List<Dict> dictList = new ArrayList<>();
        //分省
        if("PROVINCE".equals(data.getData().getBusinessAreaType())){
            DictType dictType  = dictMap.get("provices");
            dictList = dictType.getDict();
        }
        //国际
        if("INTERNATIONAL".equals(data.getData().getBusinessAreaType())){
            DictType dictType  = dictMap.get("internationalCountry");
            dictList = dictType.getDict();
        }
        if(dictList.size()<1){
            return "数据为空或无效";
        }

        //根据通道id查询区域价格
        ChannelPriceValidator channelPriceValidator = new ChannelPriceValidator();
        channelPriceValidator.setChannelId(channelId);
        ResponseData<List<ChannelPriceValidator>> listData = channelPriceService.findChannelPrice(channelPriceValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(listData.getCode())) {
            return listData.getMessage();
        }

        if(StringUtils.isEmpty(listData.getData())){
            return "未配置通道价格";
        }

        //封装区域价格数据
        StringBuilder channelPrices = new StringBuilder();
        for (ChannelPriceValidator channelPrice : listData.getData()) {
            String name = "";
            for (int i =0;i<dictList.size();i++) {
                Dict dict = dictList.get(i);
                if (channelPrice.getAreaCode().equals(dict.getFieldCode())) {
                    name += dict.getFieldName();
                    break;
                }
            }
            channelPrices.append(name+"："+channelPrice.getChannelPrice()+"；");
        }

        return channelPrices.toString();
    }
}
