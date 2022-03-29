package com.smoc.cloud.book.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.book.service.GroupService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.filter.FilterGroupListValidator;
import com.smoc.cloud.common.smoc.utils.SysFilterUtil;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * 组管理
 **/
@Slf4j
@RestController
@RequestMapping("/book")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @RequestMapping(value = "/group/main", method = RequestMethod.GET)
    public ModelAndView main(HttpServletRequest request) {
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("book/group_main");

        return view;
    }

    @RequestMapping(value = "/group/list", method = RequestMethod.GET)
    public ModelAndView listByParentId(HttpServletRequest request) {
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("book/group_list");

        //根据父id查询群组
        ResponseData<List<FilterGroupListValidator>> data = groupService.findByParentId(user.getOrganization(),user.getOrganization());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("parentId", user.getOrganization());
        view.addObject("list", data.getData());
        return view;
    }

    /**
     * 树形
     * @param request
     * @return
     */
    @RequestMapping(value = "/group/tree", method = RequestMethod.GET)
    public List<Nodes> treeByParentId(HttpServletRequest request) {
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        List<Nodes> groupNodes = new ArrayList<Nodes>();

        //跟节点
        Nodes tmp = new Nodes();
        tmp.setId(user.getOrganization());
        tmp.setHref("0");
        tmp.setLazyLoad(false);
        tmp.setSvcType(user.getOrganization());
        tmp.setSystem("root");
        tmp.setText(user.getCorporation());
        Map<String, Object> stateMap = new HashMap<String, Object>();
        stateMap.put("selected", true);
        tmp.setState(stateMap);

        ResponseData<List<Nodes>> nodes = groupService.getGroupByParentId(user.getOrganization(),tmp.getId());
        tmp.setNodes(nodes.getData());

        groupNodes.add(tmp);

        return groupNodes;
    }


    /**
     * 添加分类
     *
     * @return
     */
    @RequestMapping(value = "/group/add", method = RequestMethod.GET)
    public ModelAndView add(HttpServletRequest request) {
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("book/group_edit");

        FilterGroupListValidator filterGroupListValidator = new FilterGroupListValidator();
        filterGroupListValidator.setId(UUID.uuid32());
        filterGroupListValidator.setGroupId(filterGroupListValidator.getId());
        filterGroupListValidator.setEnterpriseId(user.getOrganization());
        filterGroupListValidator.setParentId(user.getOrganization());//父id
        filterGroupListValidator.setIsLeaf("1");//是子节点
        filterGroupListValidator.setStatus("1");//有效

        view.addObject("op", "add");
        view.addObject("filterGroupListValidator", filterGroupListValidator);

        return view;

    }

    /**
     * 编辑分类
     *
     * @return
     */
    @RequestMapping(value = "/group/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("book/group_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询数据
        ResponseData<FilterGroupListValidator> data = groupService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询是否是自己企业的通讯录
        if(!user.getOrganization().equals(data.getData().getEnterpriseId())){
            view.addObject("error", "不能进行修改操作！");
            return view;
        }

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "edit");
        view.addObject("filterGroupListValidator", data.getData());

        return view;
    }

    /**
     * 保存组
     * @param filterGroupListValidator
     * @param result
     * @param op
     * @param request
     * @return
     */
    @RequestMapping(value = "/group/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated FilterGroupListValidator filterGroupListValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("book/group_edit");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("filterGroupListValidator", filterGroupListValidator);
            view.addObject("op", op);
            return view;
        }

        //查询是否是自己企业的通讯录
        if(!user.getOrganization().equals(filterGroupListValidator.getEnterpriseId())){
            view.addObject("error", "不能进行修改操作！");
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            filterGroupListValidator.setCreatedTime(new Date());
            filterGroupListValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            ResponseData<FilterGroupListValidator> data = groupService.findById(filterGroupListValidator.getId());
            filterGroupListValidator.setCreatedTime(data.getData().getCreatedTime());
            filterGroupListValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //记录日志
        log.info("[通讯录组管理][{}][{}]数据:{}",op, user.getUserName(), JSON.toJSONString(filterGroupListValidator));

        //保存操作
        ResponseData data = groupService.save(filterGroupListValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("flag", "flag");

        return view;
    }

    /**
     * 删除信息
     *
     * @return
     */
    @RequestMapping(value = "/group/deleteById/{id}", method = RequestMethod.GET,produces = {"text/html;charset=UTF-8;"})
    public String deleteById(@PathVariable String id, HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator);
        }

        //查询删除数据信息
        ResponseData<FilterGroupListValidator> filterGroupListValidator = groupService.findById(id);

        //查询是否是自己企业的通讯录
        if(!user.getOrganization().equals(filterGroupListValidator.getData().getEnterpriseId())){
            return "不能进行修改操作！";
        }

        //记录日志
        log.info("[群组管理][delete][{}]数据::{}", user.getUserName(),JSON.toJSONString(filterGroupListValidator));
        //删除操作
        ResponseData data = groupService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return data.getCode() + ":" + data.getMessage();
        }

        return "1";

    }

}
