package com.smoc.cloud.configure.channel.group.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelGroupInfoValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelGroupConfigValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.channel.group.service.ChannelGroupService;
import com.smoc.cloud.sequence.service.SequenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

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
    private SequenceService sequenceService;

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

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("channelGroupInfoValidator", channelGroupInfoValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            //生成通道组ID:根据业务类型查询序列
            Integer seq = sequenceService.findSequence("CHANNEL_GROUP");
            channelGroupInfoValidator.setChannelGroupId("GRID" + seq);
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
            systemUserLogService.logsAsync("CHANNEL_GROUIP_BASE", channelGroupInfoValidator.getChannelGroupId(), "add".equals(op) ? channelGroupInfoValidator.getCreatedBy() : channelGroupInfoValidator.getUpdatedBy(), op, "add".equals(op) ? "添加通道组" : "修改通道组", JSON.toJSONString(channelGroupInfoValidator));
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
     * 通道组配置
     *
     * @return
     */
    @RequestMapping(value = "/edit/channel/{id}", method = RequestMethod.GET)
    public ModelAndView channelList(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/group/channel_group_edit_channel");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询通道组数据
        ResponseData<ChannelGroupInfoValidator> data = channelGroupService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        //查询已配置的通道

        /**
         * 查询通道列表
         */
        ChannelBasicInfoQo channelBasicInfoQo = new ChannelBasicInfoQo();
        channelBasicInfoQo.setCarrier(data.getData().getCarrier());
        channelBasicInfoQo.setBusinessType(data.getData().getBusinessType());
        channelBasicInfoQo.setInfoType(data.getData().getInfoType());
        channelBasicInfoQo.setChannelStatus("001");//正常
        ResponseData<List<ChannelBasicInfoQo>> listDate = channelGroupService.findChannelList(channelBasicInfoQo);
        if (!ResponseCode.SUCCESS.getCode().equals(listDate.getCode())) {
            view.addObject("error", listDate.getCode() + ":" + listDate.getMessage());
        }

        view.addObject("list", listDate.getData());
        view.addObject("channelBasicInfoQo", channelBasicInfoQo);
        view.addObject("channelGroupConfigValidator", new ChannelGroupConfigValidator());
        view.addObject("channelGroupId", id);

        return view;

    }

    /**
     * 通道组配置分页查询
     *
     * @return
     */
    @RequestMapping(value = "/edit/channelPage", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute ChannelBasicInfoQo channelBasicInfoQo, PageParams pageParams) {
        ModelAndView view = new ModelAndView("configure/channel/group/channel_group_edit_channel");

        //查询通道组数据
        ResponseData<ChannelGroupInfoValidator> data = channelGroupService.findById(channelBasicInfoQo.getChannelId());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        //查询已配置的通道

        //查询通道列表
        channelBasicInfoQo.setChannelStatus("001");//正常
        ResponseData<List<ChannelBasicInfoQo>> listDate = channelGroupService.findChannelList(channelBasicInfoQo);
        if (!ResponseCode.SUCCESS.getCode().equals(listDate.getCode())) {
            view.addObject("error", listDate.getCode() + ":" + listDate.getMessage());
        }

        view.addObject("list", listDate.getData());
        view.addObject("channelBasicInfoQo", channelBasicInfoQo);
        view.addObject("channelGroupConfigValidator", new ChannelGroupConfigValidator());
        view.addObject("channelGroupId", channelBasicInfoQo.getChannelId());
        return view;
    }

    /**
     * 保存通道组配置
     *
     * @return
     */
    @RequestMapping(value = "/saveChannelGroupConfig/{op}", method = RequestMethod.POST)
    public ModelAndView saveChannelGroupConfig(@ModelAttribute ChannelGroupConfigValidator channelGroupConfigValidator, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("configure/channel/group/channel_group_edit_channel");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //查询通道组数据
        ResponseData<ChannelGroupInfoValidator> responseData = channelGroupService.findById(channelGroupConfigValidator.getChannelGroupId());
        if (!ResponseCode.SUCCESS.getCode().equals(responseData.getCode())) {
            view.addObject("error", responseData.getCode() + ":" + responseData.getMessage());
        }

        /**
         * 保存通道组配置
         */
        channelGroupConfigValidator.setId(UUID.uuid32());
        channelGroupConfigValidator.setCreatedBy(user.getRealName());
        channelGroupConfigValidator.setCreatedTime(new Date());
        ResponseData data = channelGroupService.saveChannelGroupConfig(channelGroupConfigValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //记录日志
        log.info("[通道组管理][通道组配置][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(channelGroupConfigValidator));

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("CHANNEL_GROUIP_CONFIG", channelGroupConfigValidator.getChannelGroupId(), channelGroupConfigValidator.getCreatedBy(), op, "添加通道组配置" , JSON.toJSONString(channelGroupConfigValidator));
        }

        //查询已配置的通道

        /**
         * 查询通道列表
         */
        ChannelBasicInfoQo channelBasicInfoQo = new ChannelBasicInfoQo();
        channelBasicInfoQo.setCarrier(responseData.getData().getCarrier());
        channelBasicInfoQo.setBusinessType(responseData.getData().getBusinessType());
        channelBasicInfoQo.setInfoType(responseData.getData().getInfoType());
        channelBasicInfoQo.setChannelStatus("001");//正常
        ResponseData<List<ChannelBasicInfoQo>> listDate = channelGroupService.findChannelList(channelBasicInfoQo);
        if (!ResponseCode.SUCCESS.getCode().equals(listDate.getCode())) {
            view.addObject("error", listDate.getCode() + ":" + listDate.getMessage());
        }

        view.addObject("list", listDate.getData());
        view.addObject("channelBasicInfoQo", channelBasicInfoQo);
        view.addObject("channelGroupConfigValidator", new ChannelGroupConfigValidator());
        view.addObject("channelGroupId", channelGroupConfigValidator.getChannelGroupId());

        return view;
    }

    /**
     * 产品详细中心
     *
     * @return
     */
    @RequestMapping(value = "/view/center/{id}", method = RequestMethod.GET)
    public ModelAndView view_center(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/group/channel_group_view_center");

        return view;

    }

    /**
     * 产品基本详细
     *
     * @return
     */
    @RequestMapping(value = "/view/base/{id}", method = RequestMethod.GET)
    public ModelAndView view_base(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/group/channel_group_view_base");


        return view;

    }


}
