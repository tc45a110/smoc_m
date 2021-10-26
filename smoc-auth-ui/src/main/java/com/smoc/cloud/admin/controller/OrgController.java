package com.smoc.cloud.admin.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.service.OrgService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.auth.validator.OrgValidator;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 用户
 * <p>
 * 2019/4/18 14:47
 **/
@Slf4j
@RestController
@RequestMapping("/org")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class OrgController {

    @Autowired
    private OrgService orgService;

    /**
     * 进入组织管理主页
     *
     * @return
     */
    @RequestMapping(value = "/orgMain", method = RequestMethod.GET)
    public ModelAndView listMain() {

        ModelAndView view = new ModelAndView("org/org_main");

        return view;
    }

    /**
     * 加载组织树状结构
     *
     * @return
     */
    @RequestMapping(value = "/getOrgByParentId/{parentId}", method = RequestMethod.GET)
    public List<Nodes> getOrgByParentId(@PathVariable String parentId) {

        ResponseData<List<Nodes>> nodes = orgService.getOrgByParentId(parentId);

        return nodes.getData();
    }

    /**
     * 组织列表
     *
     * @return
     */
    @RequestMapping(value = "/list/{parentId}/{orgType}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String parentId, @PathVariable Integer orgType) {

        ModelAndView view = new ModelAndView("org/org_list");
        view.addObject("orgType", orgType);
        view.addObject("parentId", parentId);

        if ("root".equals(parentId)) {
            return view;
        }

        //查询组织
        ResponseData<List<OrgValidator>> data = orgService.findByParentIdAndOrgType(parentId, orgType);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }
        view.addObject("list", data.getData());

        return view;
    }

    /**
     * 进入添加页面
     *
     * @return
     */
    @RequestMapping(value = "/add/{parentId}/{orgType}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String parentId, @PathVariable Integer orgType) {

        ModelAndView view = new ModelAndView("org/org_edit");

        //初始化参数
        OrgValidator orgValidator = new OrgValidator();
        orgValidator.setId(UUID.uuid32());
        orgValidator.setParentId(parentId);
        orgValidator.setOrgType(orgType);
        orgValidator.setIsLeaf(1);
        orgValidator.setActive(1);
        orgValidator.setCreateDate(new Date());
        
        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "add");
        view.addObject("orgValidator", orgValidator);

        return view;
    }

    /**
     * 进入修改页面
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id) {

        ModelAndView view = new ModelAndView("org/org_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询数据
        ResponseData<OrgValidator> data = orgService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "edit");
        view.addObject("orgValidator", data.getData());

        return view;
    }

    /**
     * 添加、修改信息
     *
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated OrgValidator orgValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("org/org_edit");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("orgValidator", orgValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            orgValidator.setCreateDate(new Date());
            orgValidator.setActive(1);
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            orgValidator.setEditDate(new Date());
            orgValidator.setActive(1);
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //记录日志
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");
        log.info("[组织管理][{}][{}]数据:{}",op, user.getUserName(),JSON.toJSONString(orgValidator));
        //保存操作
        ResponseData data = orgService.save(orgValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/org/list/" + orgValidator.getParentId() + "/" + orgValidator.getOrgType(), true, false));
        return view;
    }

    /**
     * 删除信息
     *
     * @return
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ModelAndView deleteById(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("org/org_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询删除数据信息
        ResponseData<OrgValidator> orgValidator = orgService.findById(id);

        //记录日志
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");
        log.info("[组织管理][delete][{}]数据::{}", user.getUserName(),id);
        //删除操作
        ResponseData data = orgService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/org/list/" + orgValidator.getData().getParentId() + "/" + orgValidator.getData().getOrgType(), true, false));
        return view;
    }

}
