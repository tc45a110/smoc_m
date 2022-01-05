package com.smoc.cloud.configure.channel.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SysUserService;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.Dict;
import com.smoc.cloud.common.auth.qo.DictType;
import com.smoc.cloud.common.auth.qo.Users;
import com.smoc.cloud.common.auth.validator.UserValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelInterfaceValidator;
import com.smoc.cloud.common.smoc.utils.ChannelUtils;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.channel.service.ChannelInterfaceService;
import com.smoc.cloud.configure.channel.service.ChannelService;
import com.smoc.cloud.sequence.service.SequenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 通道管理
 **/
@Slf4j
@RestController
@RequestMapping("/configure/channel")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    @Autowired
    private ChannelInterfaceService channelInterfaceService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 通道管理列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_list");

        ///初始化数据
        PageParams<ChannelBasicInfoQo> params = new PageParams<ChannelBasicInfoQo>();
        params.setPageSize(8);
        params.setCurrentPage(1);
        ChannelBasicInfoQo channelBasicInfoQo = new ChannelBasicInfoQo();
        params.setParams(channelBasicInfoQo);

        //查询
        ResponseData<PageList<ChannelBasicInfoQo>> data = channelService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("channelBasicInfoQo", channelBasicInfoQo);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }

    /**
     * 通道分页查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute ChannelBasicInfoQo channelBasicInfoQo, PageParams pageParams) {
        ModelAndView view = new ModelAndView("configure/channel/channel_list");

        //分页查询
        pageParams.setParams(channelBasicInfoQo);

        ResponseData<PageList<ChannelBasicInfoQo>> data = channelService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("channelBasicInfoQo", channelBasicInfoQo);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }

    /**
     * 通道维护中心
     *
     * @return
     */
    @RequestMapping(value = "/edit/center/{id}", method = RequestMethod.GET)
    public ModelAndView edit_center(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_edit_center");
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
     * 通道基本信息编辑
     *
     * @return
     */
    @RequestMapping(value = "/edit/base/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_edit_base");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询销售人员
        view.addObject("salesList", findSalesList());

        /**
         * id为base是添加功能
         */
        if ("base".equals(id)) {
            //初始化数据
            ChannelBasicInfoValidator channelBasicInfoValidator = new ChannelBasicInfoValidator();
            channelBasicInfoValidator.setChannelStatus("002");//默认编辑中
            channelBasicInfoValidator.setReportEnable("1");//有无报告：有
            channelBasicInfoValidator.setSignType("1");//签名方式：通道自签名
            channelBasicInfoValidator.setUpMessageEnable("1");//有无上行：有
            channelBasicInfoValidator.setTransferEnable("0");//携号转网:否
            channelBasicInfoValidator.setBusinessAreaType("COUNTRY");//区域范围默认全国
            channelBasicInfoValidator.setPriceStyle("UNIFIED_PRICE");//默认统一计价
            channelBasicInfoValidator.setChannelProcess("1000");//配置进度
            channelBasicInfoValidator.setChannelRunStatus("1");//正常

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

    /**
     * 通道基本信息提交
     *
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated ChannelBasicInfoValidator channelBasicInfoValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("configure/channel/channel_edit_base");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //参数验证
        result = paramsValidator(channelBasicInfoValidator, result);
        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("channelBasicInfoValidator", channelBasicInfoValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            //生成通道ID:根据业务类型查询序列
            Integer seq = sequenceService.findSequence(channelBasicInfoValidator.getBusinessType());
            channelBasicInfoValidator.setChannelId("CHID" + seq);
            channelBasicInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            channelBasicInfoValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            channelBasicInfoValidator.setUpdatedTime(new Date());
            channelBasicInfoValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //保存数据
        ResponseData data = channelService.save(channelBasicInfoValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("CHANNEL_BASE", channelBasicInfoValidator.getChannelId(), "add".equals(op) ? channelBasicInfoValidator.getCreatedBy() : channelBasicInfoValidator.getUpdatedBy(), op, "add".equals(op) ? "添加通道" : "修改通道", JSON.toJSONString(channelBasicInfoValidator));
        }

        //记录日志
        log.info("[通道管理][通道基本信息][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(channelBasicInfoValidator));

        //保存成功之后，重新加载页面，iframe刷新标识，只有add才会刷新
        if ("add".equals(op)) {
            view.addObject("flag", "flag");
        } else {
            view.addObject("salesList", findSalesList());
        }

        ResponseData<ChannelBasicInfoValidator> channelValidator = channelService.findChannelById(channelBasicInfoValidator.getChannelId());

        view.addObject("channelBasicInfoValidator", channelValidator.getData());
        view.addObject("op", "edit");

        return view;
    }

    //通道基本信息：参数验证
    private BindingResult paramsValidator(ChannelBasicInfoValidator channelBasicInfoValidator, BindingResult result) {
        //参数验证:支持携号转网，那携号转网方式不能为空
        if ("1".equals(channelBasicInfoValidator.getTransferEnable()) && StringUtils.isEmpty(channelBasicInfoValidator.getTransferType())) {
            FieldError err = new FieldError("携号转网方式", "transferType", "携号转网方式不能为空");
            result.addError(err);
        }
        //参数验证:如果通道区域范围是分省，那业务区域不能为空
        if ("PROVINCE".equals(channelBasicInfoValidator.getBusinessAreaType()) && StringUtils.isEmpty(channelBasicInfoValidator.getProvince())) {
            FieldError err = new FieldError("业务区域", "supportAreaCodes", "业务区域不能为空");
            result.addError(err);
        }
        //参数验证:如果通道区域范围是国际，那业务区域不能为空
        if ("INTERNATIONAL".equals(channelBasicInfoValidator.getBusinessAreaType()) && StringUtils.isEmpty(channelBasicInfoValidator.getSupportAreaCodes())) {
            FieldError err = new FieldError("业务区域", "supportAreaCodes", "业务区域不能为空");
            result.addError(err);
        }
        //参数验证:如果通道区域范围是全国，那计价方式必须为统一计价
        if("COUNTRY".equals(channelBasicInfoValidator.getBusinessAreaType()) && !"UNIFIED_PRICE".equals(channelBasicInfoValidator.getPriceStyle())){
            FieldError err = new FieldError("计价方式", "priceStyle", "请选择统一计价");
            result.addError(err);
        }
        //参数验证:如果计价方式为统一计价，那资费不能为空
        if ("UNIFIED_PRICE".equals(channelBasicInfoValidator.getPriceStyle()) && StringUtils.isEmpty(channelBasicInfoValidator.getChannelPrice())) {
            FieldError err = new FieldError("资费", "channelPrice", "资费不能为空");
            result.addError(err);
        }

        //如果通道是正常状态并且通道进度未完善，
        if ("001".equals(channelBasicInfoValidator.getChannelStatus()) && !"1101".equals(channelBasicInfoValidator.getChannelProcess())) {
            FieldError err = new FieldError("通道状态", "channelStatus", "正常状态下需要完善通道配置信息");
            result.addError(err);
        }

        return result;
    }

    //查询销售人员
    private List<Users> findSalesList() {
        PageParams<Users> params = new PageParams<Users>();
        params.setPageSize(100);
        params.setCurrentPage(1);
        Users u = new Users();
        u.setActive(1);
        u.setType(3);
        params.setParams(u);
        PageList<Users> list = sysUserService.page(params);
        return list.getList();
    }

    /**
     * 查询通道区域范围
     *
     * @param channelId
     * @param request
     * @return
     */
    @RequestMapping(value = "/findChannelBusinessArea/{channelId}", produces = "text/html;charset=utf-8", method = RequestMethod.GET)
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

        //国际
        String dictType = "";
        if ("INTERNATIONAL".equals(data.getData().getBusinessAreaType())) {
            dictType = "internationalCountry";
        } else {
            dictType = "provices";
        }

        String areaType = "业务区域";
        String supportAreaCodes = data.getData().getSupportAreaCodes();
        //如果默认值为ALL并且屏蔽省份为空
        if ("ALL".equals(data.getData().getSupportAreaCodes())) {
            if (StringUtils.isEmpty(data.getData().getMaskProvince())) {
                return "无屏蔽省份";
            } else {
                areaType = "屏蔽省份";
                supportAreaCodes = data.getData().getMaskProvince();
            }
        }

        //取字典数据
        ServletContext context = request.getServletContext();
        Map<String, DictType> dictMap = (Map<String, DictType>) context.getAttribute("dict");

        return areaType + "：" + ChannelUtils.getAreaProvince(dictMap,dictType,supportAreaCodes);
    }

    /**
     * 通道过滤信息维护
     *
     * @return
     */
    @RequestMapping(value = "/edit/filter/{id}", method = RequestMethod.GET)
    public ModelAndView filter(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_edit_filter");

        return view;

    }

    /**
     * 通道扩展参数
     *
     * @return
     */
    @RequestMapping(value = "/edit/extend/{id}", method = RequestMethod.GET)
    public ModelAndView extend(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_edit_extend_param");

        Map<String, DictType> dictMap = (Map<String, DictType>) request.getSession().getServletContext().getAttribute("dict");
        DictType dictType = dictMap.get("channelExtendField");
        List<Dict> dictList = dictType.getDict();

        view.addObject("channelExtendFields", dictList);

        return view;

    }

    /**
     * 补发通道设置
     *
     * @return
     */
    @RequestMapping(value = "/edit/spare/{id}", method = RequestMethod.GET)
    public ModelAndView spare(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_edit_spare");

        return view;

    }

    /**
     * 通道详情中心
     *
     * @return
     */
    @RequestMapping(value = "/view/center/{id}", method = RequestMethod.GET)
    public ModelAndView view_center(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_view_center");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return view;
        }

        view.addObject("id", id);

        return view;
    }

    /**
     * 通道详情
     *
     * @return
     */
    @RequestMapping(value = "/view/base/{id}", method = RequestMethod.GET)
    public ModelAndView view_base(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_view_base");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return view;
        }

        //查询通道基本信息
        ResponseData<ChannelBasicInfoValidator> data = channelService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询对接销售名称
        String realName = "";
        if (!StringUtils.isEmpty(data.getData().getChannelAccessSales())) {
            ResponseData<UserValidator> userData = sysUserService.userProfile(data.getData().getChannelAccessSales());
            if (ResponseCode.SUCCESS.getCode().equals(userData.getCode()) && !StringUtils.isEmpty(userData.getData())) {
                realName = userData.getData().getBaseUserExtendsValidator().getRealName();
            }
        }

        //查询通道接口参数
        ChannelInterfaceValidator channelInterfaceValidator = new ChannelInterfaceValidator();
        ResponseData<ChannelInterfaceValidator> channelInterfaceData = channelInterfaceService.findChannelInterfaceByChannelId(id);
        if (ResponseCode.SUCCESS.getCode().equals(channelInterfaceData.getCode()) && !StringUtils.isEmpty(channelInterfaceData.getData())) {
            channelInterfaceValidator = channelInterfaceData.getData();
        }

        view.addObject("channelBasicInfoValidator", data.getData());
        view.addObject("channelInterfaceValidator", channelInterfaceValidator);
        view.addObject("realName", realName);

        return view;

    }

}
