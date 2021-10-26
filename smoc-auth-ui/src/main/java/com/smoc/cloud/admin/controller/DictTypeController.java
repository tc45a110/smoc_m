package com.smoc.cloud.admin.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.DictTypeService;
import com.smoc.cloud.admin.service.SystemService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.auth.validator.DictTypeValidator;
import com.smoc.cloud.common.auth.validator.SystemValidator;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.utils.UUID;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公用字典接口
 * 2019/5/21 17:16
 **/
@Slf4j
@RestController
@RequestMapping("dictType")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class DictTypeController {

    @Autowired
    private SystemService systemService;

    @Autowired
    private DictTypeService dictTypeService;

    /**
     * 查询
     *
     * @param
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {

        ModelAndView view = new ModelAndView("dict/dict_type_list");

        //查询数据
        ResponseData<List<DictTypeValidator>> data = dictTypeService.list();
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }
        view.addObject("list", data.getData());

        return view;
    }

    /**
     * 根据dictType查询
     *
     * @param
     */
    @RequestMapping(value = "/getDictTree", method = RequestMethod.GET)
    public List<Nodes> getDictTree() {
        List<Nodes> data = dictTypeService.getDictTree();
        return data;
    }

    /**
     * 添加
     *
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView add() {

        ModelAndView view = new ModelAndView("dict/dict_type_edit");

        //初始化数据
        DictTypeValidator dictTypeValidator = new DictTypeValidator();
        dictTypeValidator.setId(UUID.uuid32());
        dictTypeValidator.setActive(1);
        dictTypeValidator.setCreateDate(new Date());
        view.addObject("dictTypeValidator", dictTypeValidator);
        view.addObject("op", "add");

        //加载系统信息
        ResponseData<Iterable<SystemValidator>> systemData = systemService.list();
        if (!ResponseCode.SUCCESS.getCode().equals(systemData.getCode())) {
            view.addObject("error", systemData.getCode() + ":" + systemData.getMessage());
            return view;
        }
        view.addObject("systemList", systemData.getData());

        //处理复选框选中
        Map<String, Boolean> systemMap = new HashMap<>();
        view.addObject("systemMap", systemMap);

        return view;
    }

    /**
     * 修改
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id) {

        ModelAndView view = new ModelAndView("dict/dict_type_edit");
        view.addObject("op", "edit");

        //根据id查询
        ResponseData<DictTypeValidator> data = dictTypeService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }
        view.addObject("dictTypeValidator", data.getData());

        //加载系统信息
        ResponseData<Iterable<SystemValidator>> systemData = systemService.list();
        if (!ResponseCode.SUCCESS.getCode().equals(systemData.getCode())) {
            view.addObject("error", systemData.getCode() + ":" + systemData.getMessage());
            return view;
        }
        view.addObject("systemList", systemData.getData());


        //处理复选框选中
        Map<String, Boolean> systemMap = new HashMap<>();
        DictTypeValidator dictTypeValidator = data.getData();
        if (!StringUtils.isEmpty(dictTypeValidator.getDictTypeSystem())) {
            String[] system = dictTypeValidator.getDictTypeSystem().split(",");
            for (int i = 0; i < system.length; i++) {
                systemMap.put(system[i], true);
            }
        }
        view.addObject("systemMap", systemMap);


        return view;
    }

    /**
     * 添加、修改
     *
     * @param dictTypeValidator
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated DictTypeValidator dictTypeValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("dict/dict_type_edit");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("dictTypeValidator", dictTypeValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            dictTypeValidator.setCreateDate(new Date());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            dictTypeValidator.setEditDate(new Date());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //记录日志
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        log.info("[字典类型管理][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(JSON.toJSONString(dictTypeValidator)));
        //保存数据
        ResponseData data = dictTypeService.save(dictTypeValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/dictType/list", true, false));
        return view;
    }


    /**
     * 根据ID 删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ModelAndView deleteById(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("dict/dict_type_list");

        //记录日志
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        log.info("[字典类型管理][delete][{}]数据:{}", user.getUserName(), id);
        //删除数据
        ResponseData data = dictTypeService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/dictType/list", true, false));
        return view;
    }

}
