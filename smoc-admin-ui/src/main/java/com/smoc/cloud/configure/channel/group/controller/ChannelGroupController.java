package com.smoc.cloud.configure.channel.group.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SysUserService;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.DictType;
import com.smoc.cloud.common.auth.qo.Users;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelAccountInfoQo;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelGroupInfoValidator;
import com.smoc.cloud.common.smoc.utils.ChannelUtils;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.channel.group.service.ChannelGroupService;
import com.smoc.cloud.configure.channel.service.ChannelService;
import com.smoc.cloud.sequence.service.SequenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 通道组管理
 **/
@Slf4j
@RestController
@RequestMapping("/configure/channel/group")
public class ChannelGroupController {

    @Autowired
    private ChannelGroupService channelGroupService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private ChannelService channelService;

    /**
     * 通道组管理列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/group/channel_group_list");

        ///初始化数据
        PageParams<ChannelGroupInfoValidator> params = new PageParams<ChannelGroupInfoValidator>();
        params.setPageSize(8);
        params.setCurrentPage(1);
        ChannelGroupInfoValidator channelGroupInfoValidator = new ChannelGroupInfoValidator();
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
        ModelAndView view = new ModelAndView("configure/channel/group/channel_group_list");

        //分页查询
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

        ModelAndView view = new ModelAndView("configure/channel/group/channel_group_edit_center");

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

        ModelAndView view = new ModelAndView("configure/channel/group/channel_group_edit_base");

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

    /**
     * 通道组基本信息提交
     *
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated ChannelGroupInfoValidator channelGroupInfoValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("configure/channel/group/channel_group_edit_base");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        if("INTL".equals(channelGroupInfoValidator.getCarrier())){
            view.setViewName("configure/channel/international/channel_group_international_edit_base");
        }

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("channelGroupInfoValidator", channelGroupInfoValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            //封装通道ID前缀
            String prefixId = sequenceService.getPrefixId("CHANNEL_GROUP",channelGroupInfoValidator.getBusinessType());
            channelGroupInfoValidator.setChannelGroupId(prefixId);
            channelGroupInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            channelGroupInfoValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            channelGroupInfoValidator.setUpdatedTime(new Date());
            channelGroupInfoValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //保存数据
        ResponseData data = channelGroupService.save(channelGroupInfoValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("CHANNEL_GROUIP", channelGroupInfoValidator.getChannelGroupId(), "add".equals(op) ? channelGroupInfoValidator.getCreatedBy() : channelGroupInfoValidator.getUpdatedBy(), op, "add".equals(op) ? "添加通道组" : "修改通道组", JSON.toJSONString(channelGroupInfoValidator));
        }

        //记录日志
        log.info("[通道组管理][通道组基本信息][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(channelGroupInfoValidator));


        //保存成功之后，重新加载页面，iframe刷新标识，只有add才会刷新
        if ("add".equals(op)) {
            view.addObject("flag", "flag");
        }

        ResponseData<ChannelGroupInfoValidator> channelValidator = channelGroupService.findById(channelGroupInfoValidator.getChannelGroupId());

        view.addObject("channelGroupInfoValidator", channelValidator.getData());
        view.addObject("op", "edit");

        return view;
    }

    /**
     * 查看屏蔽省份
     *
     * @return
     */
    @RequestMapping(value = "/maskProvince/{id}", produces = "text/html;charset=utf-8", method = RequestMethod.GET)
    public String maskProvince(@PathVariable String id, HttpServletRequest request) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return MpmValidatorUtil.validateMessage(validator);
        }

        //查看通道组数据
        ResponseData<ChannelGroupInfoValidator> data = channelGroupService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return data.getMessage();
        }

        String maskProvince = data.getData().getMaskProvince();
        if(StringUtils.isEmpty(maskProvince)){
            return "屏蔽省份为空";
        }

        //取字典数据
        ServletContext context = request.getServletContext();
        Map<String, DictType> dictMap = (Map<String, DictType>) context.getAttribute("dict");

        return ChannelUtils.getAreaProvince(dictMap,"provices",maskProvince);
    }


    /**
     * 通道组详细中心
     *
     * @return
     */
    @RequestMapping(value = "/view/center/{id}", method = RequestMethod.GET)
    public ModelAndView view_center(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/group/channel_group_view_center");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查看通道组数据
        ResponseData<ChannelGroupInfoValidator> data = channelGroupService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("id", id);
        view.addObject("flag", data.getData().getCarrier());

        return view;

    }

    /**
     * 通道组基本详细
     *
     * @return
     */
    @RequestMapping(value = "/view/base/{id}", method = RequestMethod.GET)
    public ModelAndView view_base(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/group/channel_group_view_base");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查看通道组数据
        ResponseData<ChannelGroupInfoValidator> data = channelGroupService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询已配置的通道列表
        ChannelGroupInfoValidator channelGroupInfoValidator = new ChannelGroupInfoValidator();
        channelGroupInfoValidator.setChannelGroupId(id);
        ResponseData<List<ChannelBasicInfoQo>> channelData = channelGroupService.centerConfigChannelList(channelGroupInfoValidator);
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

        view.addObject("channelGroupInfoValidator", data.getData());
        view.addObject("channelList", channelData.getData());
        view.addObject("salesMap", salesMap);

        return view;

    }

    /**
     * 通道组使用明细列表
     *
     * @return
     */
    @RequestMapping(value = "/view/account/list/{channelId}", method = RequestMethod.GET)
    public ModelAndView accountList(@PathVariable String channelId, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/group/channel_group_view_account_list");

        ///初始化数据
        PageParams<ChannelAccountInfoQo> params = new PageParams<ChannelAccountInfoQo>();
        params.setPageSize(15);
        params.setCurrentPage(1);
        ChannelAccountInfoQo channelAccountInfoQo = new ChannelAccountInfoQo();
        channelAccountInfoQo.setChannelId(channelId);
        channelAccountInfoQo.setConfigType("ACCOUNT_CHANNEL_GROUP");
        params.setParams(channelAccountInfoQo);

        //查询
        ResponseData<PageList<ChannelAccountInfoQo>> data = channelService.channelAccountList(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("channelAccountInfoQo", channelAccountInfoQo);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }

    /**
     * 通道组使用明细分页查询
     *
     * @return
     */
    @RequestMapping(value = "/view/account/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute ChannelAccountInfoQo channelAccountInfoQo, PageParams pageParams) {
        ModelAndView view = new ModelAndView("configure/channel/group/channel_group_view_account_list");

        //分页查询
        channelAccountInfoQo.setConfigType("ACCOUNT_CHANNEL_GROUP");
        pageParams.setParams(channelAccountInfoQo);

        ResponseData<PageList<ChannelAccountInfoQo>> data = channelService.channelAccountList(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("channelAccountInfoQo", channelAccountInfoQo);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }

}
