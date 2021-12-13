package com.smoc.cloud.configure.codenumber.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.CodeNumberInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.codenumber.service.CodeNumberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 码号管理
 **/
@Slf4j
@Controller
@RequestMapping("/configure/code/number")
public class CodeNumberController {

    @Autowired
    private CodeNumberService codeNumberService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    /**
     * 码号管理列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/code_number/code_number_list");

        //初始化数据
        PageParams<CodeNumberInfoValidator> params = new PageParams<CodeNumberInfoValidator>();
        params.setPageSize(8);
        params.setCurrentPage(1);
        CodeNumberInfoValidator codeNumberInfoValidator = new CodeNumberInfoValidator();
        params.setParams(codeNumberInfoValidator);

        //查询
        ResponseData<PageList<CodeNumberInfoValidator>> data = codeNumberService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("codeNumberInfoValidator", codeNumberInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;

    }

    /**
     * 码号分页查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute CodeNumberInfoValidator codeNumberInfoValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("configure/code_number/code_number_list");

        //分页查询
        pageParams.setParams(codeNumberInfoValidator);

        ResponseData<PageList<CodeNumberInfoValidator>> data = codeNumberService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("codeNumberInfoValidator", codeNumberInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;

    }

    /**
     * 新建码号
     *
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView add(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/code_number/code_number_edit");

        //初始化参数
        CodeNumberInfoValidator codeNumberInfoValidator = new CodeNumberInfoValidator();
        codeNumberInfoValidator.setId(UUID.uuid32());
        codeNumberInfoValidator.setSrcIdStatus("001");//正常状态
        codeNumberInfoValidator.setUseType("SELF_USE");//自用
        codeNumberInfoValidator.setAccessPoint("GROUP");
        codeNumberInfoValidator.setSrcIdSource("OWN");//自有

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "add");
        view.addObject("codeNumberInfoValidator", codeNumberInfoValidator);

        return view;

    }


    /**
     * 码号编辑
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/code_number/code_number_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询数据
        ResponseData<CodeNumberInfoValidator> data = codeNumberService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "edit");
        view.addObject("codeNumberInfoValidator", data.getData());

        return view;
    }

    /**
     * 码号保存
     *
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated CodeNumberInfoValidator codeNumberInfoValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("configure/code_number/code_number_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("codeNumberInfoValidator", codeNumberInfoValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            codeNumberInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            codeNumberInfoValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            codeNumberInfoValidator.setUpdatedTime(new Date());
            codeNumberInfoValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //保存数据
        ResponseData data = codeNumberService.save(codeNumberInfoValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("CODE_NUMBER", codeNumberInfoValidator.getId(), "add".equals(op) ? codeNumberInfoValidator.getCreatedBy() : codeNumberInfoValidator.getUpdatedBy(), op, "add".equals(op) ? "添加码号" : "修改码号", JSON.toJSONString(codeNumberInfoValidator));
        }

        //记录日志
        log.info("[码号管理][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(codeNumberInfoValidator));

        view.setView(new RedirectView("/configure/code/number/list", true, false));
        return view;

    }

    /**
     * 码号查看
     *
     * @return
     */
    @RequestMapping(value = "/view/center/{id}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/code_number/code_number_view_center");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        view.addObject("id", id);

        return view;

    }

    /**
     * 码号详情
     *
     * @return
     */
    @RequestMapping(value = "/view/base/{id}", method = RequestMethod.GET)
    public ModelAndView detail(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/code_number/code_number_view_base");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询数据
        ResponseData<CodeNumberInfoValidator> data = codeNumberService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        view.addObject("info", data.getData());

        return view;
    }

}
