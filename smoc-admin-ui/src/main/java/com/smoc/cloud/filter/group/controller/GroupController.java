package com.smoc.cloud.filter.group.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.properties.SystemProperties;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.filter.FilterGroupListValidator;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.filter.group.service.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 分类管理
 **/
@Slf4j
@RestController
@RequestMapping("/filter/group")
public class GroupController {

    @Autowired
    private SystemProperties systemProperties;

    @Autowired
    private GroupService groupService;

    /**
     * 树形
     * @param parentId
     * @param request
     * @return
     */
    @RequestMapping(value = "/tree/{parentId}", method = RequestMethod.GET)
    public List<Nodes> treeByParentId(@PathVariable String parentId, HttpServletRequest request) {

        if("smoc_white".equals(parentId)){
            return whiteGroupNodes(parentId);
        }

        if("smoc_black".equals(parentId)){
            return blackGroupNodes(parentId);
        }

        return new ArrayList<Nodes>();
    }

    private List<Nodes> blackGroupNodes(String enterprise) {
        List<Nodes> groupNodes = new ArrayList<Nodes>();

        //跟节点
        Nodes tmp = new Nodes();
        tmp.setId("root");
        tmp.setHref("0");
        tmp.setLazyLoad(false);
        tmp.setSvcType("smoc_black");
        tmp.setText(systemProperties.getCompanyName());
        Map<String, Object> stateMap = new HashMap<String, Object>();
        stateMap.put("selected", true);
        tmp.setState(stateMap);

        ResponseData<List<Nodes>> nodes = groupService.getGroupByParentId(enterprise,tmp.getId());
        tmp.setNodes(nodes.getData());

        groupNodes.add(tmp);

        return groupNodes;
    }

    private List<Nodes> whiteGroupNodes(String enterprise) {
        List<Nodes> groupNodes = new ArrayList<Nodes>();

        //跟节点
        Nodes tmp = new Nodes();
        tmp.setId("root");
        tmp.setHref("0");
        tmp.setLazyLoad(false);
        tmp.setSvcType("smoc_white");
        tmp.setText(systemProperties.getCompanyName());
        Map<String, Object> stateMap = new HashMap<String, Object>();
        stateMap.put("selected", true);
        tmp.setState(stateMap);

        ResponseData<List<Nodes>> nodes = groupService.getGroupByParentId(enterprise,tmp.getId());
        tmp.setNodes(nodes.getData());

        groupNodes.add(tmp);

        return groupNodes;
    }


    /**
     * 分类列表
     *
     * @return
     */
    @RequestMapping(value = "/list/{parentId}/{svcType}", method = RequestMethod.GET)
    public ModelAndView listByParentId(@PathVariable String parentId,@PathVariable String svcType,HttpServletRequest request) {
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("filter/group/group_list");
        view.addObject("parentId", parentId);
        view.addObject("svcType", svcType);

        //根据父id查询群组
        ResponseData<List<FilterGroupListValidator>> data = groupService.findByParentId(svcType,parentId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("list", data.getData());
        return view;
    }


    /**
     * 添加分类
     *
     * @return
     */
    @RequestMapping(value = "/add/{id}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/group/group_edit");

        FilterGroupListValidator filterGroupListValidator = new FilterGroupListValidator();
        filterGroupListValidator.setId(UUID.uuid32());
        filterGroupListValidator.setGroupId(filterGroupListValidator.getId());
        filterGroupListValidator.setEnterpriseId(id);
        filterGroupListValidator.setParentId("root");//父id
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
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/group/group_edit");

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
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated FilterGroupListValidator filterGroupListValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("filter/group/group_edit");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("filterGroupListValidator", filterGroupListValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            filterGroupListValidator.setCreatedTime(new Date());
            filterGroupListValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            ResponseData<FilterGroupListValidator> data = groupService.findById(filterGroupListValidator.getId());
            filterGroupListValidator.setCreatedTime(data.getData().getCreatedTime());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //记录日志
        log.info("[群组管理][{}][{}]数据:{}",op, user.getUserName(), JSON.toJSONString(filterGroupListValidator));

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
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET,produces = {"text/html;charset=UTF-8;"})
    public String deleteById(@PathVariable String id, HttpServletRequest request) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator);
        }

        //查询删除数据信息
        ResponseData<FilterGroupListValidator> filterGroupListValidator = groupService.findById(id);

        //记录日志
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");
        log.info("[群组管理][delete][{}]数据::{}", user.getUserName(),JSON.toJSONString(filterGroupListValidator));
        //删除操作
        ResponseData data = groupService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return data.getCode() + ":" + data.getMessage();
        }

        return "1";

    }
}
