package com.smoc.cloud.customer.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseWebAccountInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.EnterpriseService;
import com.smoc.cloud.customer.service.EnterpriseWebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@Slf4j
@Controller
@RequestMapping("/enterprise/web")
public class EnterpriseWebController {

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private EnterpriseWebService enterpriseWebService;

    @Autowired
    private SystemUserLogService systemUserLogService;


    /**
     * 保存WEB登录账号
     *
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated EnterpriseWebAccountInfoValidator enterpriseWebAccountInfoValidator, BindingResult result, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/enterprise/enterprise_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //查询企业信息
        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(enterpriseWebAccountInfoValidator.getEnterpriseId());
        if (!ResponseCode.SUCCESS.getCode().equals(enterpriseData.getCode())) {
            view.addObject("error", enterpriseData.getCode() + ":" + enterpriseData.getMessage());
        }
        //企业状态无效
        if(!"1".equals(enterpriseData.getData().getEnterpriseStatus())){
            view.addObject("error", "企业状态无效，无法进行操作！");
            return view;
        }

        String op = "add";
        if(!StringUtils.isEmpty(enterpriseWebAccountInfoValidator.getId())){
            op = "edit";
        }

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(enterpriseWebAccountInfoValidator));
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            enterpriseWebAccountInfoValidator.setId(UUID.uuid32());
            enterpriseWebAccountInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            enterpriseWebAccountInfoValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            enterpriseWebAccountInfoValidator.setUpdatedTime(new Date());
            enterpriseWebAccountInfoValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //add：添加 edit：重置密码
        ResponseData data = null;
        if("add".equals(op)){
            //保存数据
            enterpriseWebAccountInfoValidator.setAccountStatus("1");
            data = enterpriseWebService.save(enterpriseWebAccountInfoValidator, op);
        }else{
            data = enterpriseWebService.resetPassword(enterpriseWebAccountInfoValidator);
        }

        if (!StringUtils.isEmpty(data) && !ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("ENTERPRISE_INFO", enterpriseWebAccountInfoValidator.getEnterpriseId(), "add".equals(op) ? enterpriseWebAccountInfoValidator.getCreatedBy() : enterpriseWebAccountInfoValidator.getUpdatedBy(), op, "add".equals(op) ? "添加"+enterpriseWebAccountInfoValidator.getWebLoginName()+"WEB登录账号":"重置"+enterpriseWebAccountInfoValidator.getWebLoginName()+"WEB账号密码" , JSON.toJSONString(enterpriseWebAccountInfoValidator));
        }

        //记录日志
        log.info("[企业接入][{}][{}][{}]数据:{}", "add".equals(op) ? "添加"+enterpriseWebAccountInfoValidator.getWebLoginName()+"WEB登录账号":"重置"+enterpriseWebAccountInfoValidator.getWebLoginName()+"WEB账号密码",op, user.getUserName(), JSON.toJSONString(enterpriseWebAccountInfoValidator));

        view.setView(new RedirectView("/enterprise/center/"+enterpriseWebAccountInfoValidator.getEnterpriseId(), true, false));
        return view;

    }

    /**
     * 注销、启用账号
     *
     * @return
     */
    @RequestMapping(value = "/forbiddenWeb/{id}/{status}", method = RequestMethod.GET)
    public ModelAndView forbiddenWeb(@PathVariable String id,@PathVariable String status, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/enterprise/enterprise_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询账号
        ResponseData<EnterpriseWebAccountInfoValidator> data = enterpriseWebService.findById(id);
        if (!StringUtils.isEmpty(data) && !ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询企业信息
        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(data.getData().getEnterpriseId());
        if (!ResponseCode.SUCCESS.getCode().equals(enterpriseData.getCode())) {
            view.addObject("error", enterpriseData.getCode() + ":" + enterpriseData.getMessage());
        }
        //企业状态无效
        if(!"1".equals(enterpriseData.getData().getEnterpriseStatus())){
            view.addObject("error", "企业状态无效，无法进行操作！");
            return view;
        }

        //注销、启用账号
        ResponseData webData = enterpriseWebService.forbiddenWeb(id,status);

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(webData.getCode())) {
            systemUserLogService.logsAsync("ENTERPRISE_INFO", data.getData().getEnterpriseId(), user.getRealName(), "edit", "1".equals(status) ? "注销"+data.getData().getWebLoginName()+"WEB登录账号":"启用"+data.getData().getWebLoginName()+"WEB登录账号" , JSON.toJSONString(data.getData()));
        }

        //记录日志
        log.info("[企业接入][{}][{}][{}]数据:{}", "1".equals(status) ? "注销"+data.getData().getWebLoginName()+"WEB登录账号":"启用"+data.getData().getWebLoginName()+"WEB登录账号","edit" , user.getUserName(), JSON.toJSONString(data.getData()));

        view.setView(new RedirectView("/enterprise/center/"+data.getData().getEnterpriseId(), true, false));
        return view;

    }

}
