package com.smoc.cloud.admin.security.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.properties.SystemProperties;
import com.smoc.cloud.admin.security.remote.service.DictService;
import com.smoc.cloud.admin.security.remote.service.DictTypeService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.validator.DictTypeValidator;
import com.smoc.cloud.common.auth.validator.DictValidator;
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
import java.util.List;

/**
 * 公用字典接口
 * 2019/5/21 17:16
 **/
@Slf4j
@RestController
@RequestMapping("dict")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class DictController {

    @Autowired
    private DictService dictService;

    @Autowired
    private DictTypeService dictTypeService;

    @Autowired
    private SystemProperties systemProperties;


    /**
     * 根据dictType查询
     *
     * @param
     */
    @RequestMapping(value = "/sysMain", method = RequestMethod.GET)
    public ModelAndView sysMain() {

        ModelAndView view = new ModelAndView("sys_dict/sys_dict_main");
        view.addObject("url", "/sysDictType/getDictTree");
        return view;
    }

    /**
     * 根据dictType查询
     *
     * @param
     */
    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public ModelAndView main() {

        ModelAndView view = new ModelAndView("sys_dict/sys_dict_main");
        view.addObject("url", "/dictType/getDictTree");
        return view;
    }

    /**
     * 根据dictType查询
     *
     * @param
     */
    @RequestMapping(value = "/listByDictType/{typeId}/{dictType}", method = RequestMethod.GET)
    public ModelAndView listByDictType(@PathVariable String typeId, @PathVariable String dictType) {

        ModelAndView view = new ModelAndView("sys_dict/sys_dict_list");
        view.addObject("dictType", dictType);
        view.addObject("typeId", typeId);

        if ("root".equals(dictType)) {
            return view;
        }

        //查询字典类别信息
        ResponseData<DictTypeValidator> dictTypeData = dictTypeService.findById(typeId);
        if (!ResponseCode.SUCCESS.getCode().equals(dictTypeData.getCode())) {
            view.addObject("error", dictTypeData.getCode() + ":" + dictTypeData.getMessage());
            return view;
        }
        view.addObject("dictTypeValidator", dictTypeData.getData());

        //查询数据
        ResponseData<List<DictValidator>> data = dictService.listByDictType(typeId, dictType);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }
        view.addObject("list", data.getData());

        return view;
    }

    /**
     * 添加
     *
     * @param dictType
     * @return
     */
    @RequestMapping(value = "/add/{typeId}/{dictType}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String typeId, @PathVariable String dictType) {

        ModelAndView view = new ModelAndView("sys_dict/sys_dict_edit");

        //初始化数据
        DictValidator dictValidator = new DictValidator();
        dictValidator.setId(UUID.uuid32());
        dictValidator.setActive(1);
        dictValidator.setCreateDate(new Date());
        dictValidator.setDictType(dictType);
        dictValidator.setTypeId(typeId);
        view.addObject("dictValidator", dictValidator);
        view.addObject("op", "add");

        //查询字典类别信息
        ResponseData<DictTypeValidator> dictTypeData = dictTypeService.findById(dictValidator.getTypeId());
        if (!ResponseCode.SUCCESS.getCode().equals(dictTypeData.getCode())) {
            view.addObject("error", dictTypeData.getCode() + ":" + dictTypeData.getMessage());
            return view;
        }
        view.addObject("dictTypeValidator", dictTypeData.getData());

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

        ModelAndView view = new ModelAndView("sys_dict/sys_dict_edit");
        view.addObject("op", "edit");

        //根据ID查询数据信息
        ResponseData<DictValidator> data = dictService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }
        view.addObject("dictValidator", data.getData());

        //查询字典类别信息
        ResponseData<DictTypeValidator> dictTypeData = dictTypeService.findById(data.getData().getTypeId());
        if (!ResponseCode.SUCCESS.getCode().equals(dictTypeData.getCode())) {
            view.addObject("error", dictTypeData.getCode() + ":" + dictTypeData.getMessage());
            return view;
        }
        view.addObject("dictTypeValidator", dictTypeData.getData());

        return view;
    }

    /**
     * 添加、修改
     *
     * @param dictValidator
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated DictValidator dictValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("sys_dict/sys_dict_edit");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("dictValidator", dictValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            dictValidator.setCreateDate(new Date());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            dictValidator.setEditDate(new Date());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //记录日志
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        log.info("[字典管理][{}][{}]数据:{}", op,user.getUserName(), JSON.toJSONString(dictValidator));

        //保存数据
        ResponseData data = dictService.save(dictValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/dict/listByDictType/" + dictValidator.getTypeId() + "/" + dictValidator.getDictType(), true, false));
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

        ModelAndView view = new ModelAndView("dict/dict_list");

        //根据ID查询数据信息
        ResponseData<DictValidator> data = dictService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //记录日志
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        log.info("[字典管理][delete][{}]数据:{}",user.getUserName(),id);
        //删除数据
        ResponseData deleteData = dictService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(deleteData.getCode())) {
            view.addObject("error", deleteData.getCode() + ":" + deleteData.getMessage());
            return view;
        }

        view.setView(new RedirectView("/dict/listByDictType/" + data.getData().getTypeId() + "/" + data.getData().getDictType(), true, false));
        return view;
    }

}
