package com.smoc.cloud.admin.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.service.ClientsService;
import com.smoc.cloud.admin.service.SystemService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.validator.ClientDetailsValidator;
import com.smoc.cloud.common.auth.validator.ResetClientSecretValidator;
import com.smoc.cloud.common.auth.validator.SystemValidator;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.utils.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户
 * 2019/4/18 14:47
 **/
@Slf4j
@Controller
@RequestMapping("/client")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class ClientController {

    @Autowired
    private SystemService systemService;

    @Autowired
    private ClientsService clientsService;


    /**
     * 客户端列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {

        ModelAndView view = new ModelAndView("client/client_list");

        ResponseData<List<ClientDetailsValidator>> data = clientsService.list();
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }
        view.addObject("list", data);

        //修改密码对象
        ResetClientSecretValidator resetClientSecretValidator = new ResetClientSecretValidator();
        view.addObject("resetClientSecretValidator", resetClientSecretValidator);

        return view;
    }

    /**
     * 添加客户端
     *
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView add() {

        ModelAndView view = new ModelAndView("client/client_edit");

        //初始化数据
        ClientDetailsValidator clientDetailsValidator = new ClientDetailsValidator();
        clientDetailsValidator.setClientId(UUID.uuid32().toUpperCase());
        clientDetailsValidator.setClientSecret(UUID.uuid32().toUpperCase());

        view.addObject("clientDetailsValidator", clientDetailsValidator);
        view.addObject("op", "add");

        //处理复选框
        Map<String, Boolean> checkMap = new HashMap<>();
        //checkMap.put("client_credentials", false);
        checkMap.put("refresh_token", false);
        checkMap.put("password", false);
        view.addObject("checkMap", checkMap);

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
     * 修改客户端
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id) {

        ModelAndView view = new ModelAndView("client/client_edit");
        view.addObject("op", "edit");

        //根据id查询
        ResponseData<ClientDetailsValidator> data = clientsService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }
        view.addObject("clientDetailsValidator", data.getData());

        //处理复选框
        Map<String, Boolean> checkMap = new HashMap<>();
        //checkMap.put("client_credentials", false);
        checkMap.put("refresh_token", false);
        checkMap.put("password", false);
        if (!StringUtils.isEmpty(data.getData().getAuthorizedGrantTypes())) {
            String[] checks = data.getData().getAuthorizedGrantTypes().split(",");
            for (int i = 0; i < checks.length; i++) {
                if (!checkMap.get(checks[i])) {
                    checkMap.put(checks[i], true);
                }
            }

        }
        view.addObject("checkMap", checkMap);

        //加载系统信息
        ResponseData<Iterable<SystemValidator>> systemData = systemService.list();
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }
        view.addObject("systemList", systemData.getData());

        //系统选中处理
        Map<String, Boolean> systemMap = new HashMap<>();
        if (!StringUtils.isEmpty(data.getData().getScope())) {
            String[] checks = data.getData().getScope().split(",");
            for (int i = 0; i < checks.length; i++) {
                systemMap.put(checks[i], true);
            }
        }
        view.addObject("systemMap", systemMap);

        return view;
    }

    /**
     * 添加、修改信息
     *
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated ClientDetailsValidator clientDetailsValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("client/client_edit");
        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("clientDetailsValidator", clientDetailsValidator);
            view.addObject("op", op);
            return view;
        }

        //校验op
        if (StringUtils.isEmpty(op) || !(!StringUtils.isEmpty(op) && ("add".equals(op) || "edit".equals(op)))) {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //记录日志
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        log.info("[客户端管理][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(clientDetailsValidator));

        //保存数据
        ResponseData data = clientsService.save(clientDetailsValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/client/list", true, false));
        return view;
    }


    /**
     * 删除信息
     *
     * @return
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ModelAndView deleteById(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("client/client_edit");

        //记录日志
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        log.info("[客户端管理][delete][{}]数据:{}", user.getUserName(), id);
        //删除数据
        ResponseData data = clientsService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/client/list", true, false));
        return view;
    }

    /**
     * 重置密码
     *
     * @return
     */
    @RequestMapping(value = "/resetClientSecret", method = RequestMethod.POST)
    public ModelAndView deleteById(@ModelAttribute @Validated ResetClientSecretValidator resetClientSecretValidator, BindingResult result, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("client/list");
        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("resetClientSecretValidator", resetClientSecretValidator);
            return view;
        }

        //记录日志
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        log.info("[客户端管理][resetClientSecret][{}]数据:{}", user.getUserName(), JSON.toJSONString(resetClientSecretValidator));
        //重置密码
        ResponseData data = clientsService.resetClientSecret(resetClientSecretValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/client/list", true, false));
        return view;
    }

}
