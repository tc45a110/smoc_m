package com.smoc.cloud.configure.channel.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.Dict;
import com.smoc.cloud.common.auth.qo.DictType;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelPriceValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.channel.service.ChannelPriceService;
import com.smoc.cloud.configure.channel.service.ChannelService;
import com.smoc.cloud.configure.channel.service.ExcelListen;
import com.smoc.cloud.configure.channel.service.ExcelPriceData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
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

        //如果是区域计价，才会去查区域价格数据
        ChannelPriceValidator channelPriceValidator = new ChannelPriceValidator();
        if ("AREA_PRICE".equals(channelBasicInfoValidator.getPriceStyle())) {
            channelPriceValidator.setChannelId(channelBasicInfoValidator.getChannelId());
            channelPriceValidator.setAreaCode(channelBasicInfoValidator.getSupportAreaCodes());

            //根据通道id和区域查询价格
            ResponseData<List<ChannelPriceValidator>> list = channelPriceService.findChannelPrice(channelPriceValidator);
            if (!StringUtils.isEmpty(list.getData())) {
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
            channelPrice.setCreatedBy(user.getRealName());
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
            systemUserLogService.logsAsync("CHANNEL_BASE", channelPriceValidator.getChannelId(), "add".equals(op) ? user.getRealName() : user.getRealName(), op, "add".equals(op) ? "添加区域计价配置" : "修改区域计价配置", JSON.toJSONString(channelPriceValidator));
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
     *
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
        if ("PROVINCE".equals(data.getData().getBusinessAreaType())) {
            DictType dictType = dictMap.get("provices");
            dictList = dictType.getDict();
        }
        //国际
        if ("INTL".equals(data.getData().getBusinessAreaType())) {
            DictType dictType = dictMap.get("internationalArea");
            dictList = dictType.getDict();
        }
        if (dictList.size() < 1) {
            return "数据为空或无效";
        }

        //根据通道id查询区域价格
        ChannelPriceValidator channelPriceValidator = new ChannelPriceValidator();
        channelPriceValidator.setChannelId(channelId);
        ResponseData<List<ChannelPriceValidator>> listData = channelPriceService.findChannelPrice(channelPriceValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(listData.getCode())) {
            return listData.getMessage();
        }

        if (StringUtils.isEmpty(listData.getData())) {
            return "未配置通道价格";
        }

        //封装区域价格数据
        StringBuilder channelPrices = new StringBuilder();
        List<ChannelPriceValidator> list = listData.getData();
        for (int a = 0; a < list.size(); a++) {
            ChannelPriceValidator channelPrice = list.get(a);
            String name = "";
            for (int i = 0; i < dictList.size(); i++) {
                Dict dict = dictList.get(i);
                if (channelPrice.getAreaCode().equals(dict.getFieldCode())) {
                    name += dict.getFieldName();
                    break;
                }
            }
            channelPrices.append(name + "：" + channelPrice.getChannelPrice() + "；");
            if (a != list.size() - 1) {
                channelPrices.append("@");
            }
        }

        return channelPrices.toString();
    }

    /**
     * 进入通道价格导入页
     *
     * @return
     */
    @RequestMapping(value = "/toImport/{channelId}", method = RequestMethod.GET)
    public ModelAndView importChannelPrice(@PathVariable String channelId) {

        ModelAndView view = new ModelAndView("configure/channel/channel_import_price");
        //查询通道是否存在
        ResponseData<ChannelBasicInfoValidator> data = channelService.findById(channelId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        String supportAreaCodes = data.getData().getSupportAreaCodes();
        //如果区域为空直接返回
        if (StringUtils.isEmpty(supportAreaCodes)) {
            view.addObject("error", "未配置业务区域");
            return view;
        }

        ChannelPriceValidator channelPriceValidator = new ChannelPriceValidator();
        channelPriceValidator.setChannelId(data.getData().getChannelId());
        channelPriceValidator.setPriceStyle("AREA_PRICE");

        view.addObject("channelPriceValidator", channelPriceValidator);
        return view;
    }

    /**
     * 通道价格导入
     *
     * @return
     */
    @RequestMapping(value = "/import/save", method = RequestMethod.POST)
    public ModelAndView saveChannelPrice(@ModelAttribute ChannelPriceValidator channelPriceValidator, @RequestPart("countriesFile") MultipartFile countriesFile, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_import_price");


        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //查询通道是否存在
        ResponseData<ChannelBasicInfoValidator> data = channelService.findById(channelPriceValidator.getChannelId());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //通道配置的国际
        String supportAreaCodes = data.getData().getSupportAreaCodes();
        //如果区域为空直接返回
        if (StringUtils.isEmpty(supportAreaCodes)) {
            view.addObject("error", "未配置业务区域");
            return view;
        }

        //读取国际短信 国家短信
        ServletContext context = request.getServletContext();
        Map<String, DictType> dictMap = (Map<String, DictType>) context.getAttribute("dict");
        Map<String, String> countries = new HashMap<>();
        String[] areas = supportAreaCodes.split(",");
        for (String area : areas) {
            countries.put(area, area);
        }

        ExcelListen excelListen = new ExcelListen();
        try {
            InputStream inputStream = countriesFile.getInputStream();
            String fileType = countriesFile.getOriginalFilename().substring(countriesFile.getOriginalFilename().lastIndexOf("."));
            if (!((".xlsx".equals(fileType) || ".xls".equals(fileType)))) {
                view.addObject("error", "文件类型错误！");
                return view;
            }

            EasyExcel.read(inputStream, ExcelPriceData.class, excelListen).sheet().doRead();

        } catch (Exception e) {
            e.printStackTrace();
            view.addObject("error", "文件导入错误！");
            return view;
        }

        if (null == excelListen || excelListen.result.size() < 1) {
            view.addObject("error", "导入数据为空！");
            return view;
        }

        //封装区域价格数据
        List<ChannelPriceValidator> list = new ArrayList<>();
        for (ExcelPriceData excelPriceData : excelListen.result) {
            ChannelPriceValidator channelPrice = new ChannelPriceValidator();
            //excel中国家编码
            String countryNo = excelPriceData.getCountriesNo();
            if (StringUtils.isEmpty(countryNo)) {
                continue;
            }
            //通道配置的国家编码
            String countriesCode = countries.get(countryNo);
            if (StringUtils.isEmpty(countriesCode)) {
                continue;
            }
            channelPrice.setAreaCode(countriesCode);

            String price = excelPriceData.getPrice();
            if (StringUtils.isEmpty(price)) {
                continue;
            }
            //校验价格为数字
            String reg = "^[0-9]+(.[0-9]+)?$";
            if (!price.matches(reg)) {
                continue;
            }
            channelPrice.setChannelPrice(new BigDecimal(price));

            channelPrice.setCreatedBy(user.getRealName());
            list.add(channelPrice);
        }

        log.info("[list]:{}",new Gson().toJson(list));
        //保存数据
        channelPriceValidator.setPrices(list);
        channelPriceValidator.setPriceStyle(data.getData().getPriceStyle());
        ResponseData priceData = channelPriceService.savePrice(channelPriceValidator, "edit");
        if (!ResponseCode.SUCCESS.getCode().equals(priceData.getCode())) {
            view.addObject("error", priceData.getCode() + ":" + priceData.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(priceData.getCode())) {
            systemUserLogService.logsAsync("CHANNEL_BASE", channelPriceValidator.getChannelId(), user.getRealName(), "edit", "修改区域计价配置", JSON.toJSONString(channelPriceValidator));
        }

        //记录日志
        log.info("[通道管理][区域计价导入][{}][{}]数据:{}", "edit", user.getUserName(), JSON.toJSONString(channelPriceValidator));

        view.setView(new RedirectView("/configure/channel/edit/price/" + channelPriceValidator.getChannelId(), true, false));
        return view;
    }
}
