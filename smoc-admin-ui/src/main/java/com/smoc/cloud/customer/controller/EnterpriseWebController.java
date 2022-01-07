package com.smoc.cloud.customer.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseWebAccountInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.EnterpriseWebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@Slf4j
@Controller
@RequestMapping("/enterprise/web")
public class EnterpriseWebController {

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
            enterpriseWebAccountInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            enterpriseWebAccountInfoValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            enterpriseWebAccountInfoValidator.setUpdatedTime(new Date());
            enterpriseWebAccountInfoValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }


        //保存数据
        ResponseData data = enterpriseWebService.save(enterpriseWebAccountInfoValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("ENTERPRISE_WEB", enterpriseWebAccountInfoValidator.getEnterpriseId(), "add".equals(op) ? enterpriseWebAccountInfoValidator.getCreatedBy() : enterpriseWebAccountInfoValidator.getUpdatedBy(), op, "add".equals(op) ? "添加企业WEB登录账号" : "修改企业WEB登录账号", JSON.toJSONString(enterpriseWebAccountInfoValidator));
        }


        //记录日志
        log.info("[企业接入][企业WEB登录账号][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(enterpriseWebAccountInfoValidator));

        view.setView(new RedirectView("/enterprise/center/"+enterpriseWebAccountInfoValidator.getEnterpriseId(), true, false));
        return view;

    }

}
